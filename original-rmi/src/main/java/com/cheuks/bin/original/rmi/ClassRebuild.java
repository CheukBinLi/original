package com.cheuks.bin.original.rmi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cheuks.bin.original.common.util.conver.StringUtil;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.StringMemberValue;

public class ClassRebuild {

	private final ClassPool pool = ClassPool.getDefault();
	{
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}
	private final String suffixName = "$proxyClass";

	public String getSuffixName(String nick) {
		if (null != nick)
			return "$" + nick + suffixName;
		return suffixName;
	}

	public Class<?> build(final Class<?> clazz, ModifyMethod modifyMethod, List<FieldModel> fieldmodels, String suffix) throws Throwable {
		return build(clazz, modifyMethod, fieldmodels, null, suffix);
	}

	public Class<?> build(final Class<?> clazz, ModifyMethod modifyMethod, List<FieldModel> fieldmodels, String className, String suffix) throws Throwable {

		boolean isInterface = clazz.isInterface();
		final String orginalClassName = (null == className ? clazz.getName() : clazz.getPackage().getName() + "." + className) + getSuffixName(suffix);

		CtClass orginalClass = pool.get(clazz.getName());
		CtClass newClass = pool.makeClass(orginalClassName);

		if (isInterface)
			newClass.addInterface(orginalClass);
		else
			newClass.setSuperclass(orginalClass);

		addField(newClass, fieldmodels);

		CtMethod[] orginalClassMethods = orginalClass.getDeclaredMethods();
		String methodString;
		String superBody;
		String temp;
		String param;
		for (final CtMethod m : orginalClassMethods) {
			param = m.getParameterTypes().length > 0 ? "$$" : "";
			superBody = isInterface ? "" : "super." + m.getName() + "(" + param + ");";
			//			methodString = generateMethod(m, null == modifyMethod ? superBody : "try{ " + modifyMethod.appCodeToBefore(m.getName(), param), (superBody = null == (temp = modifyMethod.overrideSuperMethod(m.getName(), param)) ? superBody : temp) + " }finally{" + modifyMethod.appCodeToAfter(m.getName(), param) + "}");
			methodString = generateMethod(m, null == modifyMethod ? "" : modifyMethod.appCodeToBefore(m.getName(), param), (superBody = null == (temp = null == modifyMethod ? null : modifyMethod.overrideSuperMethod(m.getName(), param)) ? superBody : temp), null == modifyMethod ? "" : modifyMethod.appCodeToAfter(m.getName(), param));
			newClass.addMethod(CtNewMethod.make(methodString, newClass));
		}
		newClass.writeFile("C:/Users/BIN/Desktop");
		return newClass.toClass();
	}

	private final void addField(final CtClass clazz, List<FieldModel> fields) throws CannotCompileException, NotFoundException {

		if (null == clazz || null == fields)
			return;
		String filed;
		String temp;
		CtClass xAnnotation;
		List<String> annotationAttribute;
		AnnotationsAttribute annotationsAttribute;
		javassist.bytecode.annotation.Annotation annotation;
		CtMethod getting;
		CtMethod setting;

		for (FieldModel item : fields) {
			filed = String.format("%s %s %s%s", null == (temp = item.modifier) ? "" : temp, item.returnType.getName(), item.name, null == (temp = item.instance) ? ";" : "=" + temp + (temp.endsWith(";") ? "" : ";"));

			CtField newField = CtField.make(filed, clazz);
			if (null != item.annotations) {
				for (Entry<Class<?>, List<String>> subItem : item.annotations.entrySet()) {
					xAnnotation = pool.get(subItem.getKey().getCanonicalName());
					annotationsAttribute = new AnnotationsAttribute(clazz.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
					annotation = new javassist.bytecode.annotation.Annotation(clazz.getClassFile().getConstPool(), xAnnotation);
					if (null != (annotationAttribute = subItem.getValue())) {
						for (String attribute : annotationAttribute) {
							String[] keyValue = attribute.split("=");
							if (keyValue.length < 2) {
								continue;
							}
							annotation.addMemberValue(keyValue[0].trim(), new StringMemberValue(keyValue[1].trim(), clazz.getClassFile().getConstPool()));
						}
					}
					annotationsAttribute.addAnnotation(annotation);
					newField.getFieldInfo().addAttribute(annotationsAttribute);
				}
			}
			clazz.addField(newField);
			if (item.createGetting) {
				getting = CtMethod.make(String.format("public %s get%s(){return this.%s;}", item.returnType.getName(), StringUtil.newInstance().toUpperCaseFirstOne(item.name), item.name), clazz);
				clazz.addMethod(getting);
			}
			if (item.createSetting) {
				setting = CtMethod.make(String.format("public void set%s(%s %s){this.%s=%s;}", StringUtil.newInstance().toUpperCaseFirstOne(item.name), item.returnType.getName(), item.name, item.name, item.name), clazz);
				clazz.addMethod(setting);
			}
		}

	}

	private String generateMethod(CtMethod m, String before, String overrideSuper, String after) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append("public ").append(m.getReturnType().getName()).append(" ").append(m.getName()).append("(");
		CtClass[] params = m.getParameterTypes();
		if (null != params)
			for (int i = 0, len = params.length; i < len; i++) {
				sb.append(params[i].getName()).append(" p_" + i);
				if (i + 1 < len)
					sb.append(",");
			}
		sb.append(")");
		CtClass[] throwses = m.getExceptionTypes();
		if (null != throwses && throwses.length > 0) {
			sb.append("throws ");
			for (int i = 0, len = throwses.length; i < len; i++) {
				sb.append(throwses[i].getName());
				if (i + 1 < len)
					sb.append(",");
			}
		}

		sb.append("{");
		if (!StringUtil.newInstance().isEmpty(after)) {
			sb.append("try{");
		}
		if (null != before) {
			sb.append(before);
		}
		if (null != overrideSuper) {
			sb.append(overrideSuper).append((!overrideSuper.endsWith(";") && !overrideSuper.endsWith("}")) ? ";" : "");
		}
		//after
		if (!StringUtil.newInstance().isEmpty(after)) {
			sb.append("}finally{").append(after).append("}");
		}
		sb.append("}");
		return sb.toString();
	}

	public final String convery4CodeByCtClass(String objectName, CtClass t) {
		if (null == t)
			return null;
		String typeName = t.getSimpleName();
		if (("void").equals(typeName))
			return objectName;
		if (("int").equals(typeName))
			return "((Integer)" + objectName + ").intValue()";
		else if (("boolean").equals(typeName)) {
			return "((Boolean)" + objectName + ").booleanValue()";
		} else if (("short").equals(typeName)) {
			return "((Short)" + objectName + ").shortValue()";
		} else if (("byte").equals(typeName)) {
			return "((Byte)" + objectName + ").byteValue()";
		} else if (("long").equals(typeName)) {
			return "((Long)" + objectName + ").longValue()";
		} else if (("char").equals(typeName)) {
			return "((Character)" + objectName + ").charValue()";
		} else if (("double").equals(typeName)) {
			return "((Double)" + objectName + ").doubleValue()";
		}
		return String.format("(%s)%s", t.getName(), objectName);
	}

	public boolean isVoid(CtMethod method) throws NotFoundException {
		return "void".equals(method.getReturnType().getSimpleName());
	}

	public static interface ModifyMethod {

		/***
		 * 
		 * @param paramsTag
		 *            方法体里所有的参数代代替码/$1代表每个参数$2为第二个参数.......
		 * @return
		 */
		default String appCodeToBefore(String methodName, String paramsTag) {
			return "";
		}

		default String overrideSuperMethod(String methodName, String paramsTag) {
			//			return "super." + methodName + "(" + paramsTag + ");";
			return null;
		}

		/***
		 * 
		 * @param paramsTag
		 *            方法体里所有的参数代代替码/$1代表每个参数$2为第二个参数.......
		 * @return
		 */
		default String appCodeToAfter(String methodName, String paramsTag) {
			return "";
		}
	}

	public static class FieldModel {

		private Map<Class<?>, List<String>> annotations;
		private String modifier;
		private Class<?> returnType;
		private String name;
		private String instance;
		private boolean createGetting;
		private boolean createSetting;

		public FieldModel appendAnnotation(Class<?> annotation, String... attributes) {
			if (null == annotation)
				return this;
			List<String> list = null;
			if (null == annotations) {
				annotations = new HashMap<Class<?>, List<String>>();
			}
			if (null != attributes && attributes.length > 0) {
				list = annotations.get(annotation);
				if (null == list) {
					list = new LinkedList<String>();
				}
				list.addAll(Arrays.asList(attributes));
			}
			annotations.put(annotation, list);
			return this;
		}

		public Map<Class<?>, List<String>> getAnnotations() {
			return annotations;
		}

		public void setAnnotations(Map<Class<?>, List<String>> annotations) {
			this.annotations = annotations;
		}

		public String getModifier() {
			return modifier;
		}

		public void setModifier(String modifier) {
			this.modifier = modifier;
		}

		public Class<?> getReturnType() {
			return returnType;
		}

		public void setReturnType(Class<?> returnType) {
			this.returnType = returnType;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getInstance() {
			return instance;
		}

		public void setInstance(String instance) {
			this.instance = instance;
		}

		public boolean isCreateGetting() {
			return createGetting;
		}

		public void setCreateGetting(boolean createGetting) {
			this.createGetting = createGetting;
		}

		public boolean isCreateSetting() {
			return createSetting;
		}

		public void setCreateSetting(boolean createSetting) {
			this.createSetting = createSetting;
		}

		public FieldModel(String modifier, Class<?> returnType, String name, String instance, boolean createGetting, boolean createSetting) {
			super();
			this.modifier = modifier;
			this.returnType = returnType;
			this.name = name;
			this.instance = instance;
			this.createGetting = createGetting;
			this.createSetting = createSetting;
		}
	}

}
