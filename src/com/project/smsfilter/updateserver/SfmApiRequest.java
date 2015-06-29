package com.project.smsfilter.updateserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import perlmine.sfm.controllerspdc.utils.MyLog;
import android.annotation.SuppressLint;

public class SfmApiRequest implements SfmApiUrlDefine {

	/**
	 * 
	 */
	private static final String USER_AUTHEN = "services";
	private static final String PASSWORD_AUTHEN = "123456";

	/**
	 * 
	 */
	private static final String CONTENT_TYPE_HEAPDER_FORM = "application/x-www-form-urlencoded";
	private static final String CONTENT_TYPE_HEAPDER_JSON = "application/json";

	/**
	 * 
	 */
	private static final int TIME_OUT = 15 * 1000; // 15 seconds

	/**
	 * Method
	 */
	private static final String CHECK_VERSION_API_METHOD = "checkversion?type=android";
	private static final String LOGIN_API_METHOD = "login";
	private static final String LOGOUT_API_METHOD = "logout";
	private static final String FORCE_LOGOUT_API_METHOD = "forcelogout";
	private static final String GET_USERS_METHOD = "getusers";
	private static final String SAVE_HISTORY_METHOD = "savehistory";
	private static final String FORGOT_PASSWORD_METHOD = "forgotpassword";
	private static final String UPDATE_VERSION_METHOD = "updateversion";
	private static final String GET_MISSIONS_METHOD = "getmission";
	private static final String SYNC_DATA_API_METHOD = "syncdata";
	private static final String GET_POINTS_METHOD = "getpoints";
	private static final String SYNC_POINT = "syncpoint";
	private static final String SYNC_WORKING_TIME = "syncworkingtime";

	/**
	 * Parameter
	 */
	private static final String USERNAME_PARAMETER = "username";
	private static final String PASSWORD_PARAMETER = "password";
	private static final String API_TOKEN_PARAMETER = "apiToken";
	private static final String DEVICE_ID_PARAMETER = "deviceid";
	// private static final String DATA_TO_SYNC_PARAMETER = "dataToSync";
	private static final String DEVICE_TYPE_PARAMETER = "deviceType";
	private static final String DEVICE_TOKEN_PARAMETER = "deviceToken";
	private static final String STATUS_PARAMETER = "status";
	private static final String NEW_VERSION_PARAMETER = "new_version";
	private static final String TYPE_PARAMETER = "type";

	/**
	 * 
	 */
	private static ArrayList<NameValuePair> mPostParameters;

	private static Header getAuthenticateHeader() {
		return BasicScheme.authenticate(new UsernamePasswordCredentials(USER_AUTHEN, PASSWORD_AUTHEN), "UTF-8", false);
	}

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
	private static String get(String uri) {

		long startTime = System.currentTimeMillis();

		BufferedReader in = null;
		String responseString = "";

		try {

			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new URI(uri));
			httpGet.addHeader("Content-Type", CONTENT_TYPE_HEAPDER_FORM);
			httpGet.addHeader(getAuthenticateHeader());

			HttpResponse response = getHttpClient().execute(httpGet);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = in.readLine();
			while (line != null) {
				responseString += line;
				line = in.readLine();
			}
			// }

			MyLog.iGeneral(String.format("Total time: %d ms - Response %s - CODE: %d: %s", System.currentTimeMillis()
					- startTime, uri, response.getStatusLine().getStatusCode(), responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eGeneral(String.format("Total time: %d ms - Error in http connection ", System.currentTimeMillis()
					- startTime, e.toString()));
		}

		return responseString;
	}

	/**
	 * get string content from uri by POST method
	 * 
	 * @param uri
	 *            uri of api server
	 * @return string response
	 */
	@SuppressLint("DefaultLocale")
	private static String post(String uri) {

		long startTime = System.currentTimeMillis();
		BufferedReader in = null;
		String responseString = "";

		try {

			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(uri));
			httpPost.addHeader("Content-Type", CONTENT_TYPE_HEAPDER_FORM);
			httpPost.addHeader(getAuthenticateHeader());
			httpPost.setEntity(new UrlEncodedFormEntity(mPostParameters));
			HttpResponse response = getHttpClient().execute(httpPost);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = in.readLine();
			while (line != null) {
				responseString += line;
				line = in.readLine();
			}
			// }

			MyLog.iGeneral(String.format("Total time: %d ms - Response %s - CODE: %d: %s", System.currentTimeMillis()
					- startTime, uri, response.getStatusLine().getStatusCode(), responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eGeneral(String.format("Total time: %d ms - Error in http connection: %s", System.currentTimeMillis()
					- startTime, e.toString()));
		}

		return responseString;
	}

	/**
	 * api get last version from server
	 * 
	 * @return last sersion name on server
	 */
	public static String getApiVersion() {
		return get(SfmApiUrlDefine.BASE_URL + CHECK_VERSION_API_METHOD);
	}

	/**
	 * api login
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @return json string response include user data, token
	 */
	public static String login(String username, String password, String gcmRegId) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(USERNAME_PARAMETER, username));
		mPostParameters.add(new BasicNameValuePair(PASSWORD_PARAMETER, password));
		mPostParameters.add(new BasicNameValuePair(DEVICE_TYPE_PARAMETER, "Google"));
		mPostParameters.add(new BasicNameValuePair(DEVICE_TOKEN_PARAMETER, gcmRegId));

		return post(SfmApiUrlDefine.BASE_URL + LOGIN_API_METHOD);
	}

	/**
	 * log out
	 * 
	 * @param apiToken
	 * @return
	 */
	public static String logOut(String apiToken) {

		// add parameters
		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(API_TOKEN_PARAMETER, apiToken));

		return post(SfmApiUrlDefine.BASE_URL + LOGOUT_API_METHOD);
	}

	/**
	 * force log out when the users have logged on another devices
	 * 
	 * @param username
	 * @param password
	 * @param deviceId
	 * @return
	 */
	public static String forceLogOut(String username, String password, String deviceId, String gcmRegId) {

		// add parameters
		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(USERNAME_PARAMETER, username));
		mPostParameters.add(new BasicNameValuePair(PASSWORD_PARAMETER, password));
		mPostParameters.add(new BasicNameValuePair(DEVICE_ID_PARAMETER, deviceId));
		mPostParameters.add(new BasicNameValuePair(DEVICE_TYPE_PARAMETER, "Google"));
		mPostParameters.add(new BasicNameValuePair(DEVICE_TOKEN_PARAMETER, gcmRegId));

		return post(SfmApiUrlDefine.BASE_URL + FORCE_LOGOUT_API_METHOD);
	}

	/**
	 * sync data
	 * 
	 */
	public static String syncData(String jsonDataToSync) {

		long startTime = System.currentTimeMillis();
		String uri = SfmApiUrlDefine.BASE_URL + SYNC_DATA_API_METHOD;

		BufferedReader in = null;
		String responseString = "";

		try {

			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(uri));
			httpPost.addHeader("Content-Type", CONTENT_TYPE_HEAPDER_JSON);
			httpPost.addHeader(getAuthenticateHeader());
			httpPost.setEntity(new StringEntity(jsonDataToSync, "UTF8"));

			HttpResponse response = getHttpClient().execute(httpPost);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = in.readLine();
			while (line != null) {
				responseString += line;
				line = in.readLine();
			}
			// }
			MyLog.iGeneral("Response syncData-" + uri + ": " + responseString);

			MyLog.iGeneral(String.format(Locale.US, "Total time: %d ms - Response syncData - %s- CODE: %s:  %s",
					System.currentTimeMillis() - startTime, uri, response.getStatusLine().getStatusCode(),
					responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eGeneral("Error in http connection " + e.toString());
		}

		return responseString;
	}

	/**
	 * get list user
	 * 
	 * @param apiToken
	 * @return
	 */
	public static String getUsers(String apiToken) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(API_TOKEN_PARAMETER, apiToken));

		return post(SfmApiUrlDefine.BASE_URL + GET_USERS_METHOD);
	}

	/**
	 * save push notification status
	 * 
	 * @return
	 */
	public static String saveHistory(String apiToken, boolean status) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(API_TOKEN_PARAMETER, apiToken));
		mPostParameters.add(new BasicNameValuePair(STATUS_PARAMETER, status ? "1" : "0"));

		return post(SfmApiUrlDefine.BASE_URL + SAVE_HISTORY_METHOD);
		// return post("http://controles-pdc.solutions30.sutrix.com/webservice/notification_api/savehistory");
	}

	/**
	 * forgot password
	 * 
	 * @return
	 */
	public static String forgotPassword(String username) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(USERNAME_PARAMETER, username));

		return post(SfmApiUrlDefine.BASE_URL + FORGOT_PASSWORD_METHOD);
	}

	/**
	 * 
	 * @param newVersion
	 * @return
	 */
	public static String updateVersion(String newVersion) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(NEW_VERSION_PARAMETER, newVersion));
		mPostParameters.add(new BasicNameValuePair(TYPE_PARAMETER, "android"));

		return post(SfmApiUrlDefine.BASE_URL + UPDATE_VERSION_METHOD);
	}

	public static String getMissions(String apiToken) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(API_TOKEN_PARAMETER, apiToken));

		return post(SfmApiUrlDefine.BASE_URL + GET_MISSIONS_METHOD);
	}

	/**
	 * 
	 * @param apiToken
	 * @return
	 */
	public static String getPoints(String apiToken) {

		mPostParameters = new ArrayList<NameValuePair>();
		mPostParameters.add(new BasicNameValuePair(API_TOKEN_PARAMETER, apiToken));

		return post(SfmApiUrlDefine.BASE_URL + GET_POINTS_METHOD);
	}

	/**
	 * sync syncPoints
	 * 
	 */
	public static String syncPoints(String jsonDataToSync) {

		long startTime = System.currentTimeMillis();
		String uri = SfmApiUrlDefine.BASE_URL + SYNC_POINT;

		BufferedReader in = null;
		String responseString = "";

		try {

			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(uri));
			httpPost.addHeader("Content-Type", CONTENT_TYPE_HEAPDER_JSON);
			httpPost.addHeader(getAuthenticateHeader());
			httpPost.setEntity(new StringEntity(jsonDataToSync, "UTF8"));

			HttpResponse response = getHttpClient().execute(httpPost);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = in.readLine();
			while (line != null) {
				responseString += line;
				line = in.readLine();
			}
			// }
			MyLog.iGeneral("Response syncPoints-" + uri + ": " + responseString);

			MyLog.iGeneral(String.format(Locale.US, "Total time: %d ms - Response syncPoints - %s- CODE: %s:  %s",
					System.currentTimeMillis() - startTime, uri, response.getStatusLine().getStatusCode(),
					responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eGeneral("Error in http connection " + e.toString());
		}

		return responseString;
	}

	/**
	 * 
	 * @param jsonDataToSync
	 * @return
	 */
	private static String postWithBody(String uri, String jsonDataToSync) {

		long startTime = System.currentTimeMillis();

		BufferedReader in = null;
		String responseString = "";

		try {

			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(uri));
			httpPost.addHeader("Content-Type", CONTENT_TYPE_HEAPDER_JSON);
			httpPost.addHeader(getAuthenticateHeader());
			httpPost.setEntity(new StringEntity(jsonDataToSync, "UTF8"));

			HttpResponse response = getHttpClient().execute(httpPost);
			// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = in.readLine();
			while (line != null) {
				responseString += line;
				line = in.readLine();
			}
			// }
			MyLog.iGeneral("Response postWithBody-" + uri + ": " + responseString);

			MyLog.iGeneral(String.format(Locale.US, "Total time: %d ms - Response postWithBody - %s- CODE: %s:  %s",
					System.currentTimeMillis() - startTime, uri, response.getStatusLine().getStatusCode(),
					responseString));
		} catch (Exception e) {
			responseString = "";
			MyLog.eGeneral("Error in http connection " + e.toString());
		}

		return responseString;
	}

	/**
	 * sync Working Time
	 * 
	 */
	public static String syncWorkingTime(String jsonDataToSync) {
		return postWithBody(SfmApiUrlDefine.BASE_URL + SYNC_WORKING_TIME, jsonDataToSync);
	}

}