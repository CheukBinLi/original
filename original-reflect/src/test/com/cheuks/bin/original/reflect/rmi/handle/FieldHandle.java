package com.cheuks.bin.original.reflect.rmi.handle;

import java.util.Map;

import javassist.CtClass;
import javassist.CtField;

public interface FieldHandle {

	void decorationField(final CtClass clazz, final CtField field, final Map<String, String> configuration);

}
