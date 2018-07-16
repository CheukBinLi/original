package com.cheuks.bin.original.common.util.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class HttpClientUtil {

	protected HttpClientUtil() {
	}

	private static HttpClientUtil INSTANCE;

	public static final HttpClientUtil newInstance() {
		if (null == INSTANCE) {
			synchronized (HttpClientUtil.class) {
				if (null == INSTANCE) {
					INSTANCE = new HttpClientUtil();
				}
			}
		}
		return INSTANCE;
	}

	/***
	 * 文件上传到微信服务器
	 *
	 * @param url
	 * @param fileName
	 * @param inputStream
	 * @throws Exception
	 */
	public ByteArrayOutputStream sendFile(String url, String fileName, InputStream inputStream) throws Exception {
		return sendFile(url, fileName, inputStream, null);
	}

	public ByteArrayOutputStream sendFile(String url, String fileName, InputStream inputStream, Map<String, String> header) throws Exception {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		URL urlObj = new URL(url);
		boolean isHttps = url.toLowerCase().contains("https:");
		HttpURLConnection con = null;
		InputStream in;
		try {
			if (isHttps)
				con = (HttpsURLConnection) urlObj.openConnection();
			else
				con = (HttpURLConnection) urlObj.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			if (null != header) {
				for (Entry<String, String> item : header.entrySet()) {
					con.setRequestProperty(item.getKey(), item.getValue());
				}
			}
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			OutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(head);
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = inputStream.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			inputStream.close();
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
			out.write(foot);
			out.flush();
			out.close();

			in = con.getInputStream();
			while ((bytes = in.read(bufferOut)) != -1) {
				result.write(bufferOut, 0, bytes);
			}
			in.close();
		} finally {
			if (null != con)
				con.disconnect();
		}
		return result;
	}

	/***
	 * get请求
	 * 
	 * @param urlPath
	 * @param timeOut
	 * @param onlyRequest
	 * @return
	 * @throws IOException
	 */
	public HttpResponseModel GET(String urlPath, int timeOut, boolean onlyRequest, boolean onlyResponseData) throws IOException {
		return GET(urlPath, timeOut, onlyRequest, onlyResponseData, null);
	}

	public HttpResponseModel GET(String urlPath, int timeOut, boolean onlyRequest, boolean onlyResponseData, Map<String, String> header) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		ByteArrayOutputStream out;
		HttpResponseModel result;
		boolean isHttps = urlPath.toLowerCase().contains("https:");
		try {
			url = new URL(urlPath);
			if (isHttps)
				con = (HttpsURLConnection) url.openConnection();
			else
				con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			if (null != header) {
				for (Entry<String, String> item : header.entrySet()) {
					con.setRequestProperty(item.getKey(), item.getValue());
				}
			}
			con.connect();
			if (onlyRequest)
				return null;
			result = new HttpResponseModel(con.getResponseCode(), onlyResponseData ? null : con.getHeaderFields(), out = new ByteArrayOutputStream());
			in = con.getInputStream();
			byte[] buffer = new byte[512];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			in.close();
		} finally {
			if (null != con)
				con.disconnect();
		}
		return result;
	}

	/***
	 * POST 请求
	 * 
	 * @param urlPath
	 * @param parameterStr
	 * @param timeOut
	 * @param onlyRequest
	 * @return
	 * @throws IOException
	 */
	public HttpResponseModel POST(String urlPath, String parameterStr, int timeOut, boolean onlyRequest, boolean onlyResponseData) throws IOException {
		return POST(urlPath, parameterStr, timeOut, onlyRequest, onlyResponseData, null);
	}

	public HttpResponseModel POST(String urlPath, String parameterStr, int timeOut, boolean onlyRequest, boolean onlyResponseData, Map<String, String> header) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		OutputStream out = null;
		ByteArrayOutputStream data;
		HttpResponseModel result;
		boolean isHttps = urlPath.toLowerCase().contains("https:");
		try {
			url = new URL(urlPath);
			if (isHttps)
				con = (HttpsURLConnection) url.openConnection();
			else
				con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setReadTimeout(timeOut);
			if (null != header) {
				for (Entry<String, String> item : header.entrySet()) {
					con.setRequestProperty(item.getKey(), item.getValue());
				}
			}
			con.connect();
			out = con.getOutputStream();
			out.write(parameterStr.getBytes("UTF-8"));
			out.flush();
			if (onlyRequest)
				return null;
			result = new HttpResponseModel(con.getResponseCode(), onlyResponseData ? null : con.getHeaderFields(), data = new ByteArrayOutputStream());
			in = con.getInputStream();
			byte[] buffer = new byte[512];
			int length;
			while ((length = in.read(buffer)) != -1) {
				data.write(buffer, 0, length);
			}
			in.close();
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != in) {
				in.close();
			}
			con.disconnect();
		}
		return result;
	}

}
