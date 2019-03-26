package com.cheuks.bin.original.common.util.web;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cheuks.bin.original.common.util.conver.StringUtil;

public interface DefaultResultCode {

	Pattern PATTERN = Pattern.compile("\\{\\}|\\{.\\}");

	default String formatMsg(String format, String[] params) {
		return format(format, params);
	}
	static String format(String format, String[] params) {
		if (StringUtil.isBlank(format) || null == params || params.length < 1)
			return format;
		Matcher matcher = PATTERN.matcher(format);
		StringBuffer result = new StringBuffer();
		LinkedList<String> link = new LinkedList<String>(Arrays.asList(params));
		while (matcher.find()) {
			matcher.appendReplacement(result, link.size() > 0 ? link.removeFirst() : "@_@");
		}
		matcher.appendTail(result);
		return result.toString();
	}

	String SUCCESS = "0";// 成功
	String SUCCESS_MSG = "SUCCESS";
	String FAIL = "-1";// 失败
	String FAIL_MSG = "FAIL";
}
