package com.cheuks.bin.original.common.util.conver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValueReferenceProvider {
	private Map<String, ValueReference> valueReference;
	//	private List<ValueReference> valueReference;

	public ValueReferenceProvider(ValueReference... values) {
		if (null == this.valueReference) {
			return;
		}
		//		this.valueReference = new LinkedList<ValueReference>(Arrays.asList(values));
		this.valueReference = new ConcurrentHashMap<String, ValueReference>(values.length);
		for (ValueReference value : values) {
			this.valueReference.put(value.getClassInfo().getClazz().getName(), value);
		}
	}

	public ValueReference getValueReference(Class<?> clazz) {
		if (null == this.valueReference) {
			return null;
		}
		return valueReference.get(clazz.getName());
	}
}
