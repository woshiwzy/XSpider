package com.common.net.httpclient;

import com.common.net.HttpRequester;
import com.common.util.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class RequesterHttpClient {

	private static HttpParams httpParams;
	private static HttpClient httpClient;

//	static {
//		createHttpClient();
//	}

	public static String doGet(String url, HashMap<String, String> params) throws ClientProtocolException, IOException {
		/* 建立HTTPGet对象 */
		createHttpClient();
		String paramStr = "";
		for(Entry<String, String> entry:params.entrySet()){
			 String key=entry.getKey();
			 String val=entry.getValue();
			 paramStr += paramStr = "&" + key + "=" + val;
		}
		if (!paramStr.equals("")) {
			paramStr = paramStr.replaceFirst("&", "?");
			url += paramStr;
		}
		HttpGet httpRequest = new HttpGet(url);
		String strResult = "doGetError";
		/* 发送请求并等待响应 */
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		dealResult(httpResponse);
		return strResult;
	}

	public static String doPost(String url, List<NameValuePair> params) throws ParseException, IOException {
		createHttpClient();
		/* 建立HTTPPost对象 */
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		return dealResult(httpResponse);
	}

	public static String doPost(String url, HashMap<String, String> mapParams) throws ClientProtocolException, IOException {
		createHttpClient();
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : mapParams.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (!StringUtils.isEmpty(value)) {
				params.add(new BasicNameValuePair(key, value));
			}
		}
		/* 添加请求参数到请求对象 */
		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse httpResponse = httpClient.execute(httpRequest);
		return dealResult(httpResponse);
	}

	private static String dealResult(HttpResponse httpResponse) throws ParseException, IOException {
		String strResult = "";
		if (null != httpResponse) {
			/* 若状态码为200 ok */
			int status = httpResponse.getStatusLine().getStatusCode();
			switch (status) {
			case 200:
				strResult = EntityUtils.toString(httpResponse.getEntity());
				break;
			default:
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
				break;
			}
		}
		return strResult;
	}

	private static HttpClient createHttpClient() {
		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
		httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, HttpRequester.TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams,HttpRequester.TIME_OUT);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, false);
		// 设置 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 创建一个 HttpClient 实例
		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

}
