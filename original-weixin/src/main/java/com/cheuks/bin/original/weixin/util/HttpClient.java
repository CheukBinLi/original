package com.cheuks.bin.original.weixin.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

	public byte[] GetByte(String urlAddress, int timeOut, boolean isHttps, boolean onlyRequest) throws IOException {
		HttpURLConnection con = null;
		URL url = null;
		InputStream in = null;
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
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			in.close();
			return out.toByteArray();
		} finally {
			con.disconnect();
		}
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

	public static void main(String[] args) throws IOException {
		HttpClient hc = new HttpClient();
		// System.out.println(hc.Post("http://api.map.baidu.com/geodata/v3/column/list", "ak=3giFYwPqNvbNaQ3xDIEvflAb9dpHgRth&geotable_id=158084", 10000, false, false));
		//
		// System.out.println(hc.Get("http://api.map.baidu.com/geodata/v3/column/list?ak=3giFYwPqNvbNaQ3xDIEvflAb9dpHgRth&geotable_id=158084&key=desc", 10000, false, false));
		//
		// StringBuilder sb = new StringBuilder();
		// sb.append("name=test1").append("&geotype=3").append("&is_published=1").append("&ak=3giFYwPqNvbNaQ3xDIEvflAb9dpHgRth");
		//
		// System.out.println(hc.Post("http://api.map.baidu.com/geodata/v3/geotable/create", sb.toString(), 10000, false, false));
		// System.out.println(hc.Get("http://api.map.baidu.com/geodata/v3/geotable/list?" + sb.toString(), 10000, false, false));
		//
		// StringBuilder column = new StringBuilder();
		// column.append("name=垃圾值").append("&key=desc").append("&type=3").append("&max_length=2048").append("&is_sortfilter_field=0").append("&is_search_field=0");
		// column.append("&is_index_field=0").append("&is_unique_field=0").append("&ak=3giFYwPqNvbNaQ3xDIEvflAb9dpHgRth").append("&geotable_id=158084");
		// System.out.println(hc.Post("http://api.map.baidu.com/geodata/v3/column/create", column.toString(), 10000, false, false));

		// System.out.println(hc.Get("http://mmbiz.qpic.cn/mmbiz_jpg/PbHUpoOYWLvkoaHcPIX19q0TRETMMrAsp9S92IFo1hHGickjwtj90iczpicjGYjFf1GicX63ibaYSW34qBrdnOgvDpw/0?0.5662205564412119", 5000, false, false));
		File image = new File("/Users/ben/Desktop/image.jpg");
		if (!image.exists()) {
			image.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(image);
		out.write(hc.GetByte("http://mmbiz.qpic.cn/mmbiz_jpg/PbHUpoOYWLvkoaHcPIX19q0TRETMMrAsp9S92IFo1hHGickjwtj90iczpicjGYjFf1GicX63ibaYSW34qBrdnOgvDpw/0?0.5662205564412119", 5000, false, false));
		out.close();
	}

	/***
	 * 文件上传到微信服务器
	 * 
	 * @param url
	 * @param fileName
	 * @param fileType
	 * @param inputStream
	 * @throws Exception
	 */
	public static void sendFile(String url, String fileName, InputStream inputStream) throws Exception {
		String result = null;
		/**
		 * 第一部分
		 */
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存
		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		// 请求正文信息
		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");
		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);
		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(inputStream);
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
