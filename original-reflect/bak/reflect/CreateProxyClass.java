package com.cheuks.bin.original.reflect;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.cache.CacheFactory;
import com.cheuks.bin.original.common.util.ConverType;
import com.cheuks.bin.original.reflect.AAA;
import com.cheuks.bin.original.reflect.test;
import com.cheuks.bin.original.reflect.rmi.RmiClient;
import com.cheuks.bin.original.reflect.rmi.SimpleRmiClient;
import com.cheuks.bin.original.reflect.rmi.handle.FieldHandle;
import com.cheuks.bin.original.reflect.rmi.handle.MethodHandle;
import com.cheuks.bin.original.reflect.rmi.handle.RmiClientConfigurationHandle;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.compiler.Javac.CtFieldWithInit;
import javassist.runtime.Desc;

public class CreateProxyClass {

	private final static Logger LOG = LoggerFactory.getLogger(CreateProxyClass.class);

	private ClassPool pool = ClassPool.getDefault();

	private String suffixName = "$proxyClass";

	private volatile Class<?> tempClass;

	private List<FieldHandle> fieldHandles;

	private List<MethodHandle> methodHandles;

	// 普通类
	public void classModified(final Class<?> orginal, final Class<?> rmiClientInterface, final Class<?> rmiClientImpl) throws Throwable {

		boolean isInterface = orginal.isInterface();

		CtClass orginalClass = pool.get(orginal.getName());
		CtClass newClass = pool.makeClass(orginal.getName() + getSuffixName());

		CtClass rmiClient = pool.get(rmiClientInterface.getName());
		CtClass rmiClientImp = pool.get(rmiClientImpl.getName());

		// 读取host,serverInterface,version

		Map<String, Object> xmlConfiguration = null;
		Map<String, Object> configuration = new RmiClientConfigurationHandle().getConfiguration(orginalClass, xmlConfiguration);

		// 继承/实现
		if (isInterface)
			newClass.addInterface(orginalClass);
		else
			newClass.setSuperclass(orginalClass);
		// 添加RmiClient接口
		newClass.addInterface(rmiClient);

		// 实现 rmiclient里的方法
		CtMethod[] methods = rmiClientImp.getDeclaredMethods();
		for (CtMethod m : methods) {
			CtMethod tempMethod = CtNewMethod.copy(m, newClass, null);
			newClass.addMethod(tempMethod);
		}

		CtField[] fields = rmiClientImp.getDeclaredFields();
		// for (CtField f : fields) {
		// CtFieldWithInit.make(src, declaring)
		// newClass.addField(f);
		// }
		// private ConverType converType = ConverType.newInstance();

		CtField converType = CtField.make("private com.cheuks.bin.original.common.util.ConverType converType = com.cheuks.bin.original.common.util.ConverType.newInstance();", newClass);
		newClass.addField(converType);

		// 方法重现
		CtMethod[] orginalClassMethods = orginalClass.getDeclaredMethods();
		StringBuilder methodString = new StringBuilder();
		for (final CtMethod m : orginalClassMethods) {
			methodString.setLength(0);
			// 方法加工
			// if (null != getMethodHandles())
			// for (final MethodHandle mh : getMethodHandles()) {
			// methodString.append(mh.decorationmethod(newClass, m,
			// configuration));
			// }

			// 转换模版: ((Integer) 1).intValue();
			methodString.append(generateMethod(m, null, convery4CodeByCtClass("rmiInvoke(\"" + m.getName() + "\",$args)", m.getReturnType())));
			CtMethod exM = CtNewMethod.make(methodString.toString(), newClass);

			// newClass.addMethod(CtNewMethod.make(methodString.toString(),
			// newClass));
			newClass.addMethod(exM);
		}

		newClass.writeFile("/Users/ben/Downloads/wwwwwwww/1.class");
		tempClass = newClass.toClass();
	}

	public String getSuffixName() {
		return suffixName;
	}

	public CreateProxyClass setSuffixName(String suffixName) {
		this.suffixName = suffixName;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		CreateProxyClass createProxyClass = new CreateProxyClass();
		createProxyClass.classModified(CacheFactory.class, RmiClient.class, SimpleRmiClient.class);
		RmiClient rmiClient = (RmiClient) createProxyClass.tempClass.newInstance();
		System.out.println(rmiClient.rmiInvoke(null, null));
		CacheFactory cf = (CacheFactory) createProxyClass.tempClass.newInstance();
		System.out.println(cf.size());

	}

	public String generateMethod(CtMethod m, String body, String returnBody) throws NotFoundException {
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
		if (null != body)
			sb.append(body);
		if (!isVoid(m))
			if (null != returnBody)
				sb.append("return " + returnBody).append(";");
			else
				sb.append("return null;");
		else if (null != returnBody)
			sb.append(returnBody).append(";");
		sb.append("}");
		if (LOG.isDebugEnabled())
			LOG.debug(sb.toString());
		return sb.toString();
	}

	public boolean isVoid(CtMethod method) throws NotFoundException {
		return "void".equals(method.getReturnType().getSimpleName());
	}

	public List<FieldHandle> getFieldHandles() {
		return fieldHandles;
	}

	public CreateProxyClass setFieldHandles(List<FieldHandle> fieldHandles) {
		this.fieldHandles = fieldHandles;
		return this;
	}

	public List<MethodHandle> getMethodHandles() {
		return methodHandles;
	}

	public CreateProxyClass setMethodHandles(List<MethodHandle> methodHandles) {
		this.methodHandles = methodHandles;
		return this;
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
			return "((Boolean" + objectName + ").booleanValue()";
		} else if (("float").equals(typeName)) {
			return "((Float" + objectName + ").floatValue()";
		}
		return String.format("(%s)%s", t.getName(), objectName);
	}
}
