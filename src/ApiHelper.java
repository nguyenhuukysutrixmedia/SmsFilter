import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class ApiHelper {

	private static final int TIME_OUT = 10 * 1000;

	public static final String SPAM_FILE_NAME = "SpamKey.csv";
	public static final String HAM_FILE_NAME = "HamKey.csv";

	private static final String BASE_URL = "http://demo.janeto.com:21000/huuky/files/";

	// private static final String SPAM_POST_URL = BASE_URL + SPAM_FILE_NAME;
	// private static final String HAM_POST_URL = BASE_URL + SPAM_FILE_NAME;

	private static final String DELETE_URL = BASE_URL + HAM_FILE_NAME;

	private static final String DATA_TYPE = "application/text";

	public static String postFile(String bodyContent, String filename) {

		String responseString = "";
		try {

			HttpClient httpClient = getHttpClient();
			HttpPost postRequest = new HttpPost(BASE_URL + filename);

			StringEntity input = new StringEntity(bodyContent);
			input.setContentType(DATA_TYPE);
			postRequest.setEntity(input);

			HttpResponse response = httpClient.execute(postRequest);

			// if (response.getStatusLine().getStatusCode() != 201) {
			// throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			// }

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				responseString += output + "\n";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}

	/**
	 * 
	 * @return
	 */
	private static HttpClient getHttpClient() {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		HttpClient httpclient = HttpClientBuilder.create().build();
		return httpclient;
	}

	/**
	 * 
	 * @return
	 */
	public static String deleteFile() {

		String responseString = "";
		try {

			HttpClient httpClient = getHttpClient();
			HttpDelete deleteRequest = new HttpDelete(DELETE_URL);
			HttpResponse response = httpClient.execute(deleteRequest);

			// if (response.getStatusLine().getStatusCode() != 201) {
			// throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			// }

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				responseString += output + "\n";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseString;
	}
}
