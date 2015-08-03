package com.ospinet.app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class CustomHttpClient {
	static InputStream is = null;
	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 100 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;

	/**
	 * Get our single instance of our HttpClient object.
	 * 
	 * @return an HttpClient object with connection parameters set
	 */

	private static HttpClient getHttpClient() {

		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();

			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}

		return mHttpClient;
	}

	public static DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);
		return client;
	}

	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @param postParameters
	 *            The parameters to send via the request
	 * @return The result of the request
	 * @throws Exception
	 */

	public static String executeHttpPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			request.setEntity(formEntity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	public static String executeHttpPostForImg(String url,
			ArrayList<NameValuePair> postParameters, String name, byte[] data)
			throws Exception {

		BufferedReader in = null;

		try {

			HttpClient client = getHttpClient();
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(

			postParameters);

			/*ContentBody cd = new InputStreamBody(new ByteArrayInputStream(data), "abc.jpg");
			entity.addPart("filename", cd);
*/
			
			/*ByteArrayBody bab = new ByteArrayBody(data, name + ".jpg");
			entity.addPart("filename", bab);
			*/
			for (int index = 0; index < postParameters.size(); index++) {
				if (postParameters.get(index).getName()
						.equalsIgnoreCase("filename")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(postParameters.get(index).getName(),
							new FileBody(new File(postParameters.get(index)
									.getValue())));
				} else {
					// Normal string data
					entity.addPart(
							postParameters.get(index).getName(),
							new StringBody(postParameters.get(index).getValue()));
				}
			}

			request.setEntity(entity);

			HttpResponse response = getThreadSafeClient().execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()

			.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();

			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				}

				catch (HttpResponseException e) {

					Log.e("log_tag", "httpresponse exception " + e.toString());
				}

				catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

	/**
	 * 
	 * Performs an HTTP GET request to the specified url.
	 * 
	 * 
	 * 
	 * @param url
	 * 
	 *            The web address to post the request to
	 * 
	 * @return The result of the request
	 * 
	 * @throws Exception
	 */

	public static String executeHttpGet(String url) throws Exception {

		BufferedReader in = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet();

			httpGet.setURI(new URI(url));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity()

					.getContent()));

			StringBuffer sb = new StringBuffer("");

			String line = "";

			String NL = System.getProperty("line.separator");

			while ((line = in.readLine()) != null) {

				sb.append(line + NL);

			}

			in.close();

			String result = sb.toString();
			Log.d("heyy", result);
			return result;

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e) {

					Log.e("log_tag", "Error converting result " + e.toString());

					e.printStackTrace();

				}

			}

		}

	}

}