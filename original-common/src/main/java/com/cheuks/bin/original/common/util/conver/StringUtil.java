package com.cheuks.bin.original.common.util.conver;

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
}
