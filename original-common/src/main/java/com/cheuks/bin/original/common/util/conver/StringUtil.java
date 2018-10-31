package com.cheuks.bin.original.common.util.conver;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 
 * @Title: original-common
 * @Description: 字段串工具
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年12月19日 上午9:32:04
 *
 */
public class StringUtil extends ConverType {

	private static StringUtil INSTANCE;

	protected StringUtil() {
	}

	public static StringUtil newInstance() {
		if (null == INSTANCE) {
			synchronized (StringUtil.class) {
				if (null == INSTANCE) {
					INSTANCE = new StringUtil();
				}
			}
		}
		return INSTANCE;
	}

	/***
	 * @see
	 *      <p>
	 *      生成正则字符串 例:
	 *      <p>
	 *      传入: get|load|read
	 *      <p>
	 *      返回 : ^.*get.*$|^.*load.*$|^.*read.*$
	 * @param value
	 * @return
	 */
	public String generateRegexString(String value) {
		if (null == value || value.length() < 1)
			return value;
		String result = value;
		result = "^*" + result.replaceAll("\\|", ".*\\$|\\^.*") + "*$";
		result = result.replaceAll("\\.\\*", "\\*");
		result = result.replaceAll("\\*|\\*\\*", "(\\.*)?");
		return result;
	}

	public int concatCount(String content, String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	public String toUpperCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}

	public String toLowerCaseFirstOne(String name) {
		if (name.length() < 1)
			return name;
		char[] ch = name.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}

	public boolean isEmpty(String str) {
		if (null == str || str.length() < 1)
			return true;
		return false;
	}

	public String isEmpty(String str, String defaultValue) {
		if (null == str || str.length() < 1)
			return defaultValue;
		return str;
	}

	public String isEmpty(String str, String defaultValue, boolean isDefaultValueToUpperCaseFirstOne) {
		if (null == str || str.length() < 1)
			if (null == str || str.length() < 1)
				return isDefaultValueToUpperCaseFirstOne ? toLowerCaseFirstOne(defaultValue) : defaultValue;
		return str;
	}

	/***
	 * 
	 * @param str
	 * @return
	 */
	public String toLowerCaseUnderscoreCamel(String str) {
		if (isEmpty(str))
			return "";
		StringBuilder result = new StringBuilder();
		for (Character item : str.toCharArray()) {
			result.append((65 <= item && 90 >= item) ? "_" + (char) (item + 32) : item);
		}
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.newInstance().concatCount(
				"file:/F:/Sync/JavaProject/original-3.0/original-prototype/original-prototype.spring.cloud/original-prototype.spring.cloud.eureka-server/target/original-prototype.spring.cloud.eureka-server-0.0.1-SNAPSHOT.jar!/BOOT-INF/lib/original-cache-0.0.1-SNAPSHOT.jar!/lua", ".jar!"));

		String a = "file:/F:/Sync/JavaProject/original-3.0/original-prototype/original-prototype.spring.cloud/original-prototype.spring.cloud.eureka-server/target/original-prototype.spring.cloud.eureka-server-0.0.1-SNAPSHOT.jar!/BOOT-INF/lib/original-cache-0.0.1-SNAPSHOT.jar!/lua";
		String[] as = a.split("!");
		System.err.println(Arrays.toString(as));
		System.out.println(a.substring(a.lastIndexOf(".jar!") + 5));

	}
}
