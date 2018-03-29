package com.cheuks.bin.original.test.tolist;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

public class T {

	private final static Map<String, Map<String, Field>> FIELD_CACHE = new ConcurrentSkipListMap<String, Map<String, Field>>();

	public Map<String, String> converyToString(List<?> list, Class<?> type) throws Throwable {
		if (null == list || list.isEmpty() || null == type)
			return null;
		Map<String, Field> fields = getFields(type);
		Map<String, String> result = new HashMap<String, String>();
		String value;
		Object temp;
		Object o;
		for (int i = 0, len = list.size(), tag = len - 1; i < len; i++) {
			o = list.get(i);
			for (Entry<String, Field> en : fields.entrySet()) {
				value = result.get(en.getKey());
				temp = null == (temp = en.getValue().get(o)) ? "\"\"" : "\"" + temp + "\"";
				result.put(en.getKey(), null == value ? "[" + temp.toString() : value + "," + temp + (i == tag ? "]" : ""));
			}
		}
		return result;
	}

	protected Map<String, Field> getFields(final Class<?> type) {
		Map<String, Field> result = FIELD_CACHE.get(type.getName());
		if (null == result) {
			FIELD_CACHE.put(type.getName(), result = scanField(type));
		}
		return result;
	}

	protected Map<String, Field> scanField(Class<?> type) {
		Field[] fields = type.getDeclaredFields();
		Ignore ignore;
		Map<String, Field> result = new HashMap<String, Field>();
		for (Field f : fields) {
			ignore = f.getAnnotation(Ignore.class);
			if (null != ignore)
				continue;
			f.setAccessible(true);
			result.put(f.getName(), f);
		}
		return result;
	}

//	public static void main(String[] args) throws JsonProcessingException, Throwable {
//		List<MPreCertificateRowDetail> list = new ArrayList<MPreCertificateRowDetail>();
//		list.add(new MPreCertificateRowDetail("1", "x1", null, null, null, null));
//		list.add(new MPreCertificateRowDetail("2", "x2", null, null, null, null));
//
//		T t = new T();
//		ObjectMapper objectMapper = new ObjectMapper();
//		System.err.println(objectMapper.writeValueAsString(t.converyToString(list, MPreCertificateRowDetail.class)));
//	}

}
