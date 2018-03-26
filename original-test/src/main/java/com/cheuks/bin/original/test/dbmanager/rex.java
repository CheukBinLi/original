package com.cheuks.bin.original.test.dbmanager;

public class rex {

	public static void main(String[] args) {
		String a = "AgxetAloadnnx";
		String b = "loadXXBA";
		String rex = "get|load*";
		rex = "^*" + rex.replaceAll("\\|", ".*\\$|\\^.*") + "*$";
		rex = rex.replaceAll("\\.\\*", "\\*");
		rex = rex.replaceAll("\\*|\\*\\*", "(\\.*)?");
		
		System.err.println(rex);
		System.err.println(b.matches("get.*|load.*"));
		System.err.println(a.matches(".*get.*.*"));
		System.err.println(a.matches(rex));
	}

}
