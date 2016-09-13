package com.cheuks.bin.original.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

public class HttpClient2 {

	public static ThreadLocal<String> cookie = new ThreadLocal<String>();

	public static void dectory(HttpClient2 con) {
	}

	public String Post(String urlAddress, String parameterStr, boolean writerCookie, int timeOut) {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		OutputStream out = null;
		StringBuffer result = new StringBuffer();
		boolean followRedirects = false;
		int status = 0;
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setFollowRedirects(false);
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setReadTimeout(timeOut);
			con.setRequestProperty("Cookie", cookie.get());
			con.setRequestProperty("Accept-Charset:", "UTF-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
			con.setRequestProperty("Referer", urlAddress);
			con.connect();

			out = con.getOutputStream();
			out.write(parameterStr.getBytes("UTF-8"));
			out.flush();
			status = con.getResponseCode();
			if (status != 200) {
				if (status == 301 || status == 302 || status == 303) {
					followRedirects = true;
				}
			}
			if (writerCookie) {
				String tempCookie = "";
				for (Entry<String, List<String>> en : con.getHeaderFields().entrySet()) {
					if (null != en.getKey() && en.getKey().equalsIgnoreCase("set-cookie")) {
						for (String str : en.getValue()) {
							tempCookie += str.substring(0, str.indexOf(";")) + ";";
						}
					}
				}
				cookie.set(tempCookie);
			}
			if (followRedirects) {
				String newUrl = con.getHeaderField("Location");
				con.disconnect();
				return Get(newUrl, false, timeOut);
			}
			in = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempRead = null;
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
		} catch (Exception e) {
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
			}
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
			}
			con.disconnect();
			dectory(this);
		}
		return result.toString();
	}

	public String Get(String urlAddress, boolean writerCookie, int timeOut) {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		StringBuffer result = new StringBuffer();
		int status = 0;
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setFollowRedirects(false);
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			con.setRequestProperty("Cookie", cookie.get());
			con.setRequestProperty("Accept-Charset:", "UTF-8");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
			con.setRequestProperty("Referer", urlAddress);
			con.connect();
			status = con.getResponseCode();
			if (writerCookie) {
				String tempCookie = "";
				for (Entry<String, List<String>> en : con.getHeaderFields().entrySet()) {
					if (en.getKey().equalsIgnoreCase("set-cookie")) {
						for (String str : en.getValue()) {
							tempCookie += str + ";";
						}
					}
				}
				cookie.set(tempCookie);
			}
			in = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempRead = null;
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
			br.close();
			in.close();
		} catch (Exception e) {
		} finally {
			con.disconnect();
			dectory(this);
		}
		return result.toString();
	}

	public String getHtml(String urlAddress, int timeOut) {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		StringBuffer result = new StringBuffer();
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			con.setRequestProperty("Cookie", cookie.get());
			con.connect();
			// int status = con.getResponseCode();
			in = con.getInputStream();
			String tempRead = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
			br.close();
			in.close();
		} catch (Exception e) {
			result = null;
		} finally {
			con.disconnect();
			dectory(this);
		}
		return result.toString();
	}

	public boolean downloadImageCode(String urlAddress, boolean writerCookie, String filePath, int timeOut) {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		boolean result = false;
		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setFollowRedirects(false);
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			con.setRequestProperty("Accept-Charset:", "UTF-8");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
			con.connect();
			if (writerCookie) {
				String tempCookie = "";
				for (Entry<String, List<String>> en : con.getHeaderFields().entrySet()) {
					// System.out.println(en.getKey());
					if (null != en.getKey() && en.getKey().equalsIgnoreCase("Set-Cookie")) {
						for (String str : en.getValue()) {
							tempCookie += str + ";";
						}
					}
				}
				cookie.set(tempCookie);
			}
			in = con.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);
			File f = new File(filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
			byte[] b = new byte[1024];
			int length = 0;
			while ((length = bis.read(b)) != -1) {
				bos.write(b, 0, length);
			}
			bos.flush();
			result = true;
			bos.close();
			in.close();
		} catch (Exception e) {
		} finally {
			con.disconnect();
			dectory(this);
		}
		return result;
	}
}
