package com.cheuks.bin.orginal.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class CreateFile {

	public static void create(Class<?> c, Class<?> idType, boolean SimpleName, boolean isSignleFloder) throws IOException, TemplateException {

		// dao
		// daoImpl
		// service
		// service
		// controller

		Configuration config = new Configuration(Configuration.VERSION_2_3_0);
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + CreateFile.class.getPackage().getName().replaceAll("[.]", "/") + "/";
		System.err.println(path);
		System.err.println(CreateFile.class.getPackage().getName());
		String gen = System.getProperty("user.dir") + "/gen";
		FileTemplateLoader fileTemplateLoader = new FileTemplateLoader(new File(path));
		config.setTemplateLoader(fileTemplateLoader);
		String[] flvs = { "Dao", "DaoImpl", "Service", "ServiceImpl", "query", "Controller" };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityFullName", c.getName());
		map.put("entityParent", c.getPackage().getName().substring(0, c.getPackage().getName().lastIndexOf(".")));
		map.put("entitySimpleName", c.getSimpleName());
		map.put("entityName", SimpleName ? c.getSimpleName() : c.getName());

		map.put("entityPackage", c.getPackage().getName().substring(0, c.getPackage().getName().lastIndexOf(".")));
		map.put("idType", SimpleName ? idType.getSimpleName() : idType.getName());
		map.put("idTypeSimpleName", idType.getSimpleName());
		map.put("idTypeNick", converType(idType));
		map.put("tag", "#");
		map.put("dollar", "$");
		map.put("params", getFieldWidthGetSetting(c));
		FileWriter writer;
		File genFile;
		String lastPath;
		for (String str : flvs) {
			// gen/user_Dao.java
			genFile = new File(gen + (isSignleFloder ? "" : "/" + c.getSimpleName()));
			lastPath = isSignleFloder ? (gen + "/" + c.getSimpleName() + str) : String.format("%s/%s/%s%s.java", gen, c.getSimpleName(), c.getSimpleName(), str);
			if (!genFile.exists())
				genFile.mkdirs();
			if (!str.equals("query"))
				writer = new FileWriter(lastPath + ".java");
			else
				writer = new FileWriter(lastPath + ".xml");
			config.getTemplate(str.toLowerCase() + ".flv").process(map, writer);
			writer.flush();
			writer.close();
		}
	}

	private static String converType(Class<?> c) {
		if (c.equals(Integer.class))
			return "int";
		else if (c.equals(String.class))
			return "String";
		else if (c.equals(Long.class))
			return "long";
		else if (c.equals(Float.class))
			return "float";
		else if (c.equals(Double.class))
			return "double";
		else if (c.equals(Byte.class))
			return "byte";
		else if (c.equals(String.class))
			return "String";
		return null;
	}

	private static List<String> getFieldWidthGetSetting(Class<?> c) {
		Field[] fields = c.getDeclaredFields();
		Map<String, Field> map = new HashMap<String, Field>();
		List<String> list = new ArrayList<String>();
		for (Field f : fields) {
			if (f.getModifiers() == Modifier.PRIVATE && f.getModifiers() != Modifier.STATIC && f.getModifiers() != Modifier.TRANSIENT) {
				map.put(f.getName(), f);
			}
		}
		Method[] methods = c.getDeclaredMethods();
		String get;
		for (Method m : methods) {
			if (m.getModifiers() == Modifier.PUBLIC && m.getModifiers() != Modifier.STATIC && m.getName().startsWith("get")) {
				get = toLowerCaseFirstOne(m.getName().substring(3));
				if (map.containsKey(get)) {
					list.add(get);
				}
			}
		}
		return list;
	}

	public static String toUpperCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}

	public static String toLowerCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}

	public static void main(String[] args) throws IOException, TemplateException {
		// System.err.println(CreateFile.class.getPackage().getName());
		// System.err.println(CreateFile.class.getPackage().getName().substring(0, CreateFile.class.getPackage().getName().lastIndexOf(".")));
		// CreateFile.create(Dictionary.class, Integer.class, true);
		// CreateFile.create(HouseInfo.class, String.class, true);
		// CreateFile.create(HouseSteward.class, String.class, true);
		// CreateFile.create(Order.class, String.class, true);
		// CreateFile.create(UserInfo.class, String.class, true);
		// CreateFile.create(Notice.class, String.class, true);
		// CreateFile.create(User.class, String.class, true);
		// CreateFile.create(Dictionary.class, Integer.class, true);
		// CreateFile.create(HouseInfo.class, String.class, true);
		// CreateFile.create(HouseSteward.class, String.class, true);
		// CreateFile.create(Order.class, String.class, true);
		// CreateFile.create(UserInfo.class, String.class, true);
		// CreateFile.create(Notice.class, String.class, true);
		// CreateFile.create(Account.class, Integer.class, true, true);
		// CreateFile.create(AccountGroup.class, Integer.class, true, true);
		// CreateFile.create(AccountGroupAuthority.class, Integer.class, true, true);
		// CreateFile.create(Authority.class, Integer.class, true, true);
		// CreateFile.create(Dict.class, Integer.class, true, true);
		// CreateFile.create(OOO.class, Integer.class, true, true);
		// CreateFile.create(Fundraising.class, Integer.class, true, true);
		// CreateFile.create(FundraisingLog.class, Integer.class, true, true);
		// CreateFile.create(WeiXinInfo.class, String.class, true, true);
		// CreateFile.create(UnifiedOrder.class, String.class, true, true);
	}

}
