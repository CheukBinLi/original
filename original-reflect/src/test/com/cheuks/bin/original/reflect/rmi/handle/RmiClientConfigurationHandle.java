package com.cheuks.bin.original.reflect.rmi.handle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cheuks.bin.original.annotation.RmiClient;

import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class RmiClientConfigurationHandle implements ConfigurationHandle {

	public Class<?> getAnnotationClass() {
		return RmiClient.class;
	}

	public Map<String, Object> getConfiguration(CtClass ctClass, Map<String, Object> xmlConfiguration) {
		Map<String, Object> configuration = new HashMap<String, Object>();
		AttributeInfo attribute = ctClass.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
		String key;
		MemberValue value;
		if (null != attribute) {
			AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) attribute;
			Annotation rmiClient = annotationsAttribute.getAnnotation(getAnnotationClass().getName());
			if (null != rmiClient.getMemberNames()) {
				Iterator<String> it = rmiClient.getMemberNames().iterator();
				while (it.hasNext()) {
					key = it.next();
					value = rmiClient.getMemberValue(key);
					if (value instanceof StringMemberValue)
						configuration.put(key, ((StringMemberValue) value).getValue());
					if (value instanceof IntegerMemberValue)
						configuration.put(key, ((IntegerMemberValue) value).getValue());
					if (value instanceof BooleanMemberValue)
						configuration.put(key, ((BooleanMemberValue) value).getValue());
					if (value instanceof LongMemberValue)
						configuration.put(key, ((LongMemberValue) value).getValue());
				}
			}
		}
		return configuration;
	}

}
