package com.cheuks.bin.original.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encryption {

	private static final Encryption newInstance = new Encryption();

	public static final Encryption newInstance() {
		return newInstance;
	}

	private MessageDigest MD5;

	private static final Logger LOG = LoggerFactory.getLogger(Encryption.class);

	private Encryption() {
		super();
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOG.error("init", e);
		}
	}

	public synchronized String MD5(String strSrc) {
		String strDes = null;
		try {
			byte[] bt = strSrc.getBytes();
			MD5.update(bt);
			strDes = bytes2Hex(MD5.digest()); // to HexString
			return strDes;
		} finally {
			if (LOG.isDebugEnabled())
				LOG.debug("strSrc:{} MD5:{}", strSrc, strDes);
			MD5.reset();
		}
	}
	
	public synchronized String MD5(byte[] bt) {
		String strDes = null;
		try {
			MD5.update(bt);
			strDes = bytes2Hex(MD5.digest()); // to HexString
			return strDes;
		} finally {
			MD5.reset();
		}
	}

	private String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

}
