package com.cheuks.bin.original.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpClient {

	public String Post(String urlAddress, String parameterStr, int timeOut, boolean isHttps, boolean onlyRequest) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		OutputStream out = null;
		StringBuilder result = new StringBuilder();
		try {
			url = new URL(urlAddress);
			url = new URL(urlAddress);
			if (isHttps)
				con = (HttpsURLConnection) url.openConnection();
			else
				con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setReadTimeout(timeOut);
			con.connect();
			out = con.getOutputStream();
			out.write(parameterStr.getBytes("UTF-8"));
			out.flush();
			if (onlyRequest)
				return null;
			in = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempRead = null;
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != in) {
				in.close();
			}
			con.disconnect();
		}
		return result.toString();
	}

	public String Get(String urlAddress, int timeOut, boolean isHttps, boolean onlyRequest) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		StringBuilder result = new StringBuilder();
		try {
			url = new URL(urlAddress);
			if (isHttps)
				con = (HttpsURLConnection) url.openConnection();
			else
				con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			con.connect();
			if (onlyRequest)
				return null;
			in = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempRead = null;
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
			br.close();
			in.close();
		} finally {
			con.disconnect();
		}
		return result.toString();
	}

	public String getHtml(String urlAddress, int timeOut, boolean isHttps) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
		StringBuilder result = new StringBuilder();
		try {
			url = new URL(urlAddress);
			if (isHttps)
				con = (HttpsURLConnection) url.openConnection();
			else
				con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setReadTimeout(timeOut);
			con.connect();
			in = con.getInputStream();
			String tempRead = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((tempRead = br.readLine()) != null) {
				result.append(tempRead);
			}
			br.close();
			in.close();
		} finally {
			con.disconnect();
		}
		return result.toString();
	}
}
