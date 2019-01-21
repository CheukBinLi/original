package com.cheuks.bin.original.common.util.conver;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

	@Deprecated
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
	public static String generateRegexString(String value) {
		if (null == value || value.length() < 1)
			return value;
		String result = value;
		result = "^*" + result.replaceAll("\\|", ".*\\$|\\^.*") + "*$";
		result = result.replaceAll("\\.\\*", "\\*");
		result = result.replaceAll("\\*|\\*\\*", "(\\.*)?");
		return result;
	}

	public static int concatCount(String content, String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	public static String toUpperCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}

	public static String toLowerCaseFirstOne(String name) {
		if (name.length() < 1)
			return name;
		char[] ch = name.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}

	public static boolean isEmpty(String str) {
		if (null == str || str.length() < 1)
			return true;
		return false;
	}

	public static String isEmpty(String str, String defaultValue) {
		if (null == str || str.length() < 1)
			return defaultValue;
		return str;
	}

	public static String isEmpty(String str, String defaultValue, boolean isDefaultValueToUpperCaseFirstOne) {
		if (null == str || str.length() < 1)
			if (null == str || str.length() < 1)
				return isDefaultValueToUpperCaseFirstOne ? toLowerCaseFirstOne(defaultValue) : defaultValue;
		return str;
	}

	public static boolean isBlank(String str) {
		if (isEmpty(str) || str.trim().length() < 1)
			return true;
		return false;
	}

	public static String isBlank(String str, String defaultValue) {
		return isBlank(str) ? defaultValue : str;
	}

	/***
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerCaseUnderscoreCamel(String str) {
		if (isEmpty(str))
			return "";
		StringBuilder result = new StringBuilder();
		for (Character item : str.toLowerCase().toCharArray()) {
			result.append((65 <= item && 90 >= item) ? "_" + (char) (item + 32) : item);
		}
		return result.toString();
	}

	public static String toLowerUnderscoreCaseCamel(String str) {
		if (isEmpty(str))
			return "";
		StringBuilder result = new StringBuilder();
		boolean nextChange = false;
		for (Character item : str.toLowerCase().toCharArray()) {
			if (item == '_') {
				nextChange = true;
				continue;
			}
			result.append(nextChange ? (char) (item - 32) : item);
			if (nextChange) {
				nextChange = false;
			}
		}
		return result.toString();
	}

	public static Long[] converLongs(String... strs) {
		if (null == strs || strs.length < 1)
			return new Long[0];
		Long[] result = new Long[strs.length];
		for (int i = 0, len = strs.length; i < len; i++) {
			result[i] = Long.valueOf(Long.valueOf(strs[i]));
		}
		return result;
	}

	public static Integer[] converIntegers(String... strs) {
		if (null == strs || strs.length < 1)
			return new Integer[0];
		Integer[] result = new Integer[strs.length];
		for (int i = 0, len = strs.length; i < len; i++) {
			result[i] = Integer.valueOf(Integer.valueOf(strs[i]));
		}
		return result;
	}

	public static String fillPosition(String content, char ch, int len, boolean left) {
		if (null == content || content.length() >= len)
			return content;
		StringBuilder result = new StringBuilder();
		for (int i = content.length(); i < len; i++) {
			result.append(ch);
		}
		return left ? result.toString() + content : content + result.toString();
	}

	public static String fillPositionLeft(String content, char ch, int len) {
		return fillPosition(content, ch, len, true);
	}

	public static String fillPositionRight(String content, char ch, int len) {
		return fillPosition(content, ch, len, false);
	}

	public static byte[] filterCharestBytes(String str, Character... cs) throws UnsupportedEncodingException {
		if (isEmpty(str)) {
			return new byte[0];
		} else if (null == cs || cs.length < 1) {
			return str.getBytes("UTF-8");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Set<Character> filter = new HashSet<>(Arrays.asList(cs));
		Character code;
	      byte[] b = new byte[2]; 
		for (int i = 0; i < str.length(); i++) {
			if (filter.contains(code = str.charAt(i))) {
				b[0] = (byte) ((code & 0xFF00) >> 8); 
				b[1] = (byte) (code & 0xFF); 
				out.write(code);
			}
		}
		return out.toByteArray();
	}

	public static String filterCharest(String str, Character... cs) {
		if (isEmpty(str) || (null == cs || cs.length < 1)) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		Set<Character> filter = new HashSet<>(Arrays.asList(cs));
		Character code;
		for (int i = 0; i < str.length(); i++) {
			if (filter.contains(code = str.charAt(i))) {
				result.append(code);
			}
		}
		return result.toString();
	}

	//	public static void main(String[] args) {
	//		System.out.println(StringUtil.newInstance().concatCount(
	//				"file:/F:/Sync/JavaProject/original-3.0/original-prototype/original-prototype.spring.cloud/original-prototype.spring.cloud.eureka-server/target/original-prototype.spring.cloud.eureka-server-0.0.1-SNAPSHOT.jar!/BOOT-INF/lib/original-cache-0.0.1-SNAPSHOT.jar!/lua", ".jar!"));
	//
	//		String a = "file:/F:/Sync/JavaProject/original-3.0/original-prototype/original-prototype.spring.cloud/original-prototype.spring.cloud.eureka-server/target/original-prototype.spring.cloud.eureka-server-0.0.1-SNAPSHOT.jar!/BOOT-INF/lib/original-cache-0.0.1-SNAPSHOT.jar!/lua";
	//		String[] as = a.split("!");
	//		System.err.println(Arrays.toString(as));
	//		System.out.println(a.substring(a.lastIndexOf(".jar!") + 5));
	//
	//		System.out.println(new StringUtil().toLowerUnderscoreCaseCamel("a_abcde_fghijk_"));
	//		
	//		System.err.println(newInstance().fillPositionLeft("x", '0', 3));
	//		System.err.println(newInstance().fillPositionRight("x", '0', 3));
	//
	//	}
}
