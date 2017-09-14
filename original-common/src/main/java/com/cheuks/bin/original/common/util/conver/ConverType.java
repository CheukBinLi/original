package com.cheuks.bin.original.common.util.conver;

@SuppressWarnings("unchecked")
public class ConverType extends ObjectFill {

	private static final ConverType newInstance = new ConverType();

	public static final ConverType newInstance() {
		return newInstance;
	}

	public final <T> T forceConvery(Object o) {
		if (null == o) {
			return null;
		}
		return (T) o;
	}

	public final <T> T convery(Object o) {
		if (null == o) {
			return null;
		}
		return (T) convery(o, o.getClass());
	}

	public final <T> T convery(Object o, Class<T> t) {
		if (null == t)
			return null;
		String typeName = t.getSimpleName();
		if ("void".equals(typeName))
			return (T) o;
		if ("int".equals(typeName) || "Integer".equals(typeName))
			return (T) ((Integer) o);
		else if ("boolean".equals(typeName) || "Boolean".equals(typeName)) {
			return (T) ((Boolean) o);
		} else if ("float".equals(typeName) || "Float".equals(typeName)) {
			return (T) ((Float) o);
		} else if ("boolean".equals(typeName) || "Boolean".equals(typeName)) {
			return (T) ((Boolean) o);
		}
		return (T) o;
	}

	public String toUpperCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}

	public String toLowerCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		int a = (Integer) ConverType.newInstance.convery(1);
		int[] b = new int[0];

		Object o = new int[0];
		int[] c = (int[]) o;

		System.out.println(a);
		System.out.println(c);
		System.out.println(b.getClass().getSimpleName());
	}

}
