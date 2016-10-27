package com.qingyu.fanya.webview;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;


import android.util.Log;

public class HttpUtil {

	public static void main(String[] args) {
		
		String makeHttpRequest = HttpUtil.makeHttpRequest("http://www.kaasworld.com:9000/", null);
		System.out.println(makeHttpRequest);
	}
	/**
	 * @param url
	 * @param params
	 * @return
	 */
	public static String makeHttpRequest(String url, HashMap<String, String> params) {

		StringBuilder sbParams = new StringBuilder();
		StringBuilder result = new StringBuilder();
		String charset = "UTF-8";
		HttpURLConnection conn = null;

		URL urlObj = null;
		DataOutputStream wr = null;

		int i = 0;
		if(params != null){
			for (String key : params.keySet()) {
				try {
					if (i != 0) {
						sbParams.append("&");
					}
					sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), charset));
	
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				i++;
			}
		}

		Log.d("HTTP Request", "params: " + sbParams.toString());

		try {
			urlObj = new URL(url);

			conn = (HttpURLConnection) urlObj.openConnection();

			conn.setDoOutput(true);

			conn.setRequestMethod("POST");

			conn.setRequestProperty("Accept-Charset", charset);

			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);

			conn.connect();

			String paramsString = sbParams.toString();

			wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(paramsString);
			wr.flush();
			wr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// Receive the response from the server
			InputStream in = new BufferedInputStream(conn.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			Log.d("HTTP Request", "result: " + result.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		conn.disconnect();

		return result.toString();
/*		// try parse the string to a JSON object
 * 		JSONObject jObj = null;
		try {
			jObj = new JSONObject(result.toString());
		} catch (JSONException e) {
			Log.e("HTTP Request", "Error parsing data " + e.toString());
		}

		// return JSON Object
		return jObj;*/
	}
	
}
