package com.cheuks.bin.original.common.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.cheuks.bin.original.common.util.conver.StringUtil;

public class SignUtil {

	public static enum SignType {
									MD5,
									HMACSHA256
	}

	/***
	 * 
	 * @param data
	 *            数据集
	 * @param head
	 *            头部数据
	 * @param tail
	 *            尾部数据
	 * @param signType
	 *            签名
	 * @param ignores
	 *            忽略字段
	 * @param key
	 *            公匙
	 * @return
	 * @throws Exception
	 */
	public static String generateSignature(final Map<String, Object> data, String head, String tail, SignType signType, String key, boolean underscoreCamel, String... ignores) throws Exception {
		Set<String> keySet = data.keySet();
		Set<String> ignore = (null == ignores || ignores.length < 1) ? null : new HashSet<>(Arrays.asList(ignores));
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder(StringUtil.newInstance().isEmpty(head) ? "" : head);
		Object value;
		for (String k : keyArray) {
			// 参数值为空，则不参与签名
			if (null == (value = data.get(k)) || (null != ignore && ignore.contains(k))) {
				continue;
			}
			sb.append(underscoreCamel ? StringUtil.newInstance().toLowerCaseUnderscoreCamel(k) : k).append("=").append(value.toString().trim()).append("&");
		}
		if (StringUtil.newInstance().isEmpty(tail)) {
			sb.setLength(sb.length() - 1);
		} else {
			sb.append(tail);
		}
		if (SignType.MD5.equals(signType)) {
			return MD5(sb.toString()).toUpperCase();
		} else if (SignType.HMACSHA256.equals(signType)) {
			return HMACSHA256(sb.toString(), key);
		} else {
			throw new Exception(String.format("Invalid sign_type: %s", signType));
		}
	}

	public static String HMACSHA256(String data, String key) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	public static String MD5(String data) throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

}
