package com.project.smsfilter.updateserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;

import com.project.smsfilter.utilities.MyLog;

public class ApiRequest {

	private static final String FILE_NAME = "sms_data.csv";
	private static final String BASE_URL = "http://demo.janeto.com:21000/huuky/files/";

	private static final String GET_URL = BASE_URL + FILE_NAME;

	/*
	 *
	 */
	private static final int TIME_OUT = 30 * 1000; // 15 seconds

	private static HttpClient getHttpClient() {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		return httpclient;
	}

	/**
	 * get string content from uri by GET method
	 * 
	 * @param uri
	 *            uri of api server
	 * @return string response
	 */
	@SuppressLint("DefaultLocale")
	public static String getDataFromServer() {

		long startTime = System.currentTimeMillis();

		BufferedReader in = null;
		String responseString = "";

		try {

			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(GET_URL));

			HttpResponse response = getHttpClient().execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = in.readLine();
				while (line != null) {
					responseString += line;
					line = in.readLine();
				}
			} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_REQUEST_TIMEOUT) {
				responseString = "TIME_OUT";
			}
			MyLog.iLog(String.format("Total time: %d ms - Response %s - CODE: %d: %s", System.currentTimeMillis()
					- startTime, GET_URL, response.getStatusLine().getStatusCode(), responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eLog(String.format("Total time: %d ms - Error in http connection %s ", System.currentTimeMillis()
					- startTime, e.toString()));
			if (e instanceof ConnectTimeoutException) {
				responseString = "TIME_OUT";
			}
		}

		return responseString;
	}

}