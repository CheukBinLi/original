package com.cheuks.bin.original.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.annotation.RmiClient;
import com.cheuks.bin.original.common.util.Encryption;
import com.cheuks.bin.original.common.util.Scan;
import com.cheuks.bin.original.common.util.ScanSimple;
import com.cheuks.bin.original.reflect.Reflection;
import com.cheuks.bin.original.reflect.rmi.ClassBean;
import com.cheuks.bin.original.reflect.rmi.MethodBean;
import com.cheuks.bin.original.reflect.rmi.SimpleRmiClient;
import com.cheuks.bin.original.reflect.rmi.handle.FieldHandle;
import com.cheuks.bin.original.reflect.rmi.handle.MethodHandle;
import com.cheuks.bin.original.reflect.rmi.handle.RmiClientConfigurationHandle;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class RmiServiceFactory2 {

	private final static Logger LOG = LoggerFactory.getLogger(RmiServiceFactory2.class);

	private ClassPool pool = ClassPool.getDefault();

	private String suffixName = "$proxyClass";

	private List<FieldHandle> fieldHandles;

	private List<MethodHandle> methodHandles;

	private Scan scan;

	private String rmiClientInterface;

	private String rmiClientImpl;

	private Reflection reflection = Reflection.newInstance();
	private Encryption encryption = Encryption.newInstance();

	private static final Map<String, MethodBean> BEAN = new ConcurrentHashMap<String, MethodBean>();
	private static final List<ClassBean> CLASS_BEAN = new ArrayList<ClassBean>();

	// private String genericBeanId(final MethodBean bean) {
	// // 类名+版本号+方法
	// String result;
	// StringBuilder sb = new StringBuilder();
	// sb.append(bean.getClassFile().getName());
	// sb.append(":").append(bean.getVersion());
	// sb.append(":").append(reflection.genericMethod(bean.getCurrentMethod()));
	// bean.setMd5Code(result = encryption.MD5(sb.toString()));
	// return result;
	// }

	public void scanRegistered(String path) throws Throwable {
		String[] paths = path.split(",");
		if (null == scan)
			scan = new ScanSimple();
		Map<String, Set<String>> classes = scan.doScan(path);
		// 过滤
		Class<?> tempClass;
		String version;
		boolean multiInstance;
		Set<String> classPaths;
		String className;
		Iterator<String> it;
		RmiClient client;
		ClassBean classBean;
		MethodBean bean;
		Annotation tempAnnotation;
		String serviceName;
		Method[] methods;
		// 扫描CLASS
		for (String p : paths) {
			classPaths = classes.get(p);
			if (null != classPaths) {
				it = classPaths.iterator();
				while (it.hasNext()) {
					className = it.next();
					if (className.endsWith("class")) {
						tempClass = Class.forName(className.replace("/", ".").replace(".class", ""));
						tempAnnotation = tempClass.getDeclaredAnnotation(RmiClient.class);
						if (null != tempAnnotation) {
							// Rmi注解
							client = (RmiClient) tempAnnotation;

							version = client.version();
							multiInstance = client.multiInstance();
							// 注册名
							serviceName = client.serviceImplementation();
							// serviceName = serviceName.length() < 1 ? tempClass.getName() : serviceName;

							CLASS_BEAN.add(classBean = new ClassBean(tempClass, serviceName, version));
							System.out.println(tempClass.getName());
							// 生成代理类
							classBean.setProxyClassFile(classRefactor(tempClass, com.cheuks.bin.original.reflect.rmi.RmiClient.class, SimpleRmiClient.class));
							// 分解Method
							//
							// 服务端
							// methods = classBean.getProxyClassFile().getDeclaredMethods();
							// for (Method m : methods) {
							// bean = MethodBean.builder(classBean, m);
							// BEAN.put(bean.getMd5Code(), bean);
							// }
						}
					}
				}
			}
		}

	}

	// 普通类
	public Class<?> classRefactor(final Class<?> orginal, final Class<?> rmiClientInterface, final Class<?> rmiClientImpl) throws Throwable {

		boolean isInterface = orginal.isInterface();
		final String orginalClassName = orginal.getName();

		CtClass orginalClass = pool.get(orginalClassName);
		CtClass newClass = pool.makeClass(orginalClassName + getSuffixName());

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

		// 方法重现
		CtMethod[] orginalClassMethods = orginalClass.getDeclaredMethods();
		// StringBuilder methodString = new StringBuilder();
		String methodString;
		for (final CtMethod m : orginalClassMethods) {
			// 转换模版: ((Integer) 1).intValue();
			methodString = generateMethod(m, null, convery4CodeByCtClass("rmiInvoke(\"" + m.getName() + "\",$args)", m.getReturnType()));
			newClass.addMethod(CtNewMethod.make(methodString, newClass));
		}

		newClass.writeFile("/Users/ben/Downloads/wwwwwwww/1.class");

		return newClass.toClass();
	}

	public String getSuffixName() {
		return suffixName;
	}

	public RmiServiceFactory2 setSuffixName(String suffixName) {
		this.suffixName = suffixName;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		// RmiServiceFactory createProxyClass = new RmiServiceFactory();
		// createProxyClass.classRefactor(CacheFactory.class, RmiClient.class, SimpleRmiClient.class);
		// RmiClient rmiClient = (RmiClient) createProxyClass.tempClass.newInstance();
		// // System.out.println(rmiClient.rmiInvoke(null, null));
		// CacheFactory cf = (CacheFactory) createProxyClass.tempClass.newInstance();
		// System.out.println(cf.size());

		// RmiServiceFactory rsf = new RmiServiceFactory();
		// ClassPool pool = ClassPool.getDefault();
		// CtClass tempClass = pool.get(RmiClient.class.getName());
		// CtMethod[] methods = tempClass.getDeclaredMethods();
		// for (CtMethod m : methods)
		// System.out.println(rsf.generateMethodName(m));

		new RmiServiceFactory2().scanRegistered("com.cheuks.bin.*");

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

	public RmiServiceFactory2 setFieldHandles(List<FieldHandle> fieldHandles) {
		this.fieldHandles = fieldHandles;
		return this;
	}

	public List<MethodHandle> getMethodHandles() {
		return methodHandles;
	}

	public RmiServiceFactory2 setMethodHandles(List<MethodHandle> methodHandles) {
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

	public String generateMethodName(CtMethod method) throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getSimpleName()).append(" ").append(method.getName()).append("(");
		generateMethodParams(method, sb);
		sb.append(");");
		// return Encryption.newInstance().MD5(sb.toString());
		return sb.toString();
	}

	private void generateMethodParams(CtMethod method, final StringBuilder builder) throws NotFoundException {
		CtClass[] classes = method.getParameterTypes();
		int length = builder.length();
		for (CtClass c : classes)
			builder.append(c.getSimpleName()).append(",");
		if (length < builder.length())
			builder.setLength(builder.length() - 1);
	}

}
