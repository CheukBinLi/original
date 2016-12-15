package com.cheuks.bin.original.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.reflect.test;
import com.cheuks.bin.original.reflect.rmi.RmiClient;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class CreateProxyClass2 {

	private final static Logger LOG = LoggerFactory.getLogger(CreateProxyClass2.class);

	private ClassPool pool = ClassPool.getDefault();

	private String suffixName = "$proxyClass";
	private String invokeImpl = "";

	private volatile Class<?> tempClass;

	// 普通类
	public void xx() throws Throwable {

		Class c = test.class;
		Class client = RmiClient.class;

		CtClass orginalClass = pool.get(c.getName());
		CtClass newClass = pool.makeClass(c.getName() + getSuffixName());
		// CtClass invokeClass = pool.get(i.getName());
		CtClass rmiClient = pool.get(client.getName());
		// 继承
		newClass.setSuperclass(orginalClass);
		// 添加RmiClient接口
		newClass.addInterface(rmiClient);
		// 实现 rmiclient里的方法
		CtMethod[] methods = rmiClient.getDeclaredMethods();
		for (CtMethod m : methods) {
			CtMethod tempMethod = CtNewMethod.make(generateMethod(m, "System.out.println( \"本地生成:哇哈哈\");", null), newClass);

			newClass.addMethod(tempMethod);
		}
		newClass.writeFile("/Users/ben/Downloads/wwwwwwww/1.class");
		tempClass = newClass.toClass();
	}

	public String getSuffixName() {
		return suffixName;
	}

	public CreateProxyClass2 setSuffixName(String suffixName) {
		this.suffixName = suffixName;
		return this;
	}

	public static void main(String[] args) throws Throwable {
		CreateProxyClass2 createProxyClass = new CreateProxyClass2();
		createProxyClass.xx();
		RmiClient rmiClient = (RmiClient) createProxyClass.tempClass.newInstance();
		rmiClient.rmiInvoke(null, null);
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
		if (null != throwses)
			for (int i = 0, len = throwses.length; i < len; i++) {
				sb.append(throwses[i].getName());
				if (i + 1 < len)
					sb.append(",");
			}
		sb.append("{");
		if (null != body)
			sb.append(body);
		if (!"void".equals(m.getReturnType().getSimpleName()))
			if (null != returnBody)
				sb.append("return " + returnBody);
			else
				sb.append("return null;");
		sb.append("}");
		if (LOG.isDebugEnabled())
			LOG.debug(sb.toString());
		return sb.toString();
	}
}
