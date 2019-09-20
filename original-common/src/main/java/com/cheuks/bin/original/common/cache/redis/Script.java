package com.cheuks.bin.original.common.cache.redis;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cheuks.bin.original.common.cache.redis.RedisExcecption;
import com.cheuks.bin.original.common.util.conver.StringUtil;

public class Script implements Serializable{

	private static final long serialVersionUID = 7380536752738652726L;

	protected static final Pattern PATTERN = Pattern.compile("\\@\\{[0-9,a-z,A-Z]+\\}");

	/***
	 * 脚本名
	 */
	private String name;
	/***
	 * {application:@{pay}}-lock:{function}-{tenant}  转换为  {application:pay}-lock:{order}-{110}
	 */
	private String slotName;
	/***
	 * 脚本
	 */
	private String script;
	
	private final String[] defaultWrap = new String[]{"{", "}"};

	private String[] wrap;

	protected String[] selectWrap() {
		return null == wrap ? defaultWrap : wrap;
	}

	public String format(String... params) throws RedisExcecption {
		String[] wrap = selectWrap();
		return format(wrap[0], wrap[1], params);
	}

	public String format(String before, String after, String[] params) throws RedisExcecption {
		return format(before, after, this.slotName, params);
	}

	public static String format(String name, String[] params) throws RedisExcecption {
		return format("", "", name, params);
	}

	public static String format(String before, String after, String name, String[] params) throws RedisExcecption {
		before = StringUtil.isBlank(before) ? "" : before;
		after = StringUtil.isBlank(after) ? "" : after;
		if (StringUtil.isAllBlank(name))
			throw new RedisExcecption("script name can't be null.");
		Matcher matcher = PATTERN.matcher(name);
		StringBuffer result = new StringBuffer();
		int count = 0;
		while (matcher.find()) {
			matcher.appendReplacement(result, before + params[count++] + after);
		}
		matcher.appendTail(result);
		return result.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Script))
			return false;
		Script temp = (Script) obj;
		return temp.name.equals(temp.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	public String getName() {
		return name;
	}

	public String getSlotName() {
		return slotName;
	}

	public String getScript() {
		return script;
	}

	public Script setName(String name) {
		this.name = name;
		return this;
	}

	public Script setSlotName(String slotName) {
		this.slotName = slotName;
		return this;
	}

	public Script setScript(String script) {
		this.script = script;
		return this;
	}
	
	
	public String[] getWrap() {
		return wrap;
	}

	public Script setWrap(String left, String right) {
		if (StringUtil.isAllBlank(left, right)) {
			return this;
		}
		this.wrap = new String[]{left, right};
		return this;
	}

	public static void main(String[] args) throws RedisExcecption {
		String a="{application:@{pay}}-lock:@{function}-@{tenant}"
		System.err.println(Script.format("[","}",a, new String[] {"mmx","order","1"}));
	}

}
