package com.cheuks.bin.original.test.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8.Fun;

public class MatchTest {

	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
//		builder.append("\\s+"); // at least one space
//		builder.append("\\w+\\([0-9a-zA-z\\._,\\s']+\\)"); // any function call including parameters within the brackets
		builder.append("\\s+[as|AS]+\\s+(([\\w\\.]+))"); // the potential alias

		Pattern pattern = Pattern.compile(builder.toString(), 0);

//		String query = "select a as a1,b as b1 from xx";
		String query = "select m.a as a1,m.b as b1 from xx m";

		Matcher matcher = pattern.matcher(query);

		while (matcher.find()) {

			String alias = matcher.group(1);
			System.out.println(alias);
			// if (StringUtils.hasText(alias)) {
			// result.add(alias);
			// }
		}

	}

}
