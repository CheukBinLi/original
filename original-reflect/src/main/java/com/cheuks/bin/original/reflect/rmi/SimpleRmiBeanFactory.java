package com.cheuks.bin.original.reflect.rmi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAttributes;

import com.cheuks.bin.original.annotation.RmiClient;
import com.cheuks.bin.original.annotation.RmiServer;
import com.cheuks.bin.original.common.util.ConverType;
import com.cheuks.bin.original.common.util.Scan;
import com.cheuks.bin.original.common.util.ScanSimple;
import com.cheuks.bin.original.reflect.Reflection;
import com.cheuks.bin.original.reflect.config.ReferenceConfig;
import com.cheuks.bin.original.reflect.config.ServiceConfig;
import com.cheuks.bin.original.reflect.rmi.model.ClassBean;
import com.cheuks.bin.original.reflect.rmi.model.MethodBean;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;

/***
 * 初始实现
 * 
 * @author ben
 *
 */
public class SimpleRmiBeanFactory implements RmiBeanFactory, ApplicationContextAware {

	private final static Logger LOG = LoggerFactory.getLogger(SimpleRmiBeanFactory.class);

	private ApplicationContext applicationContext;

	private DefaultListableBeanFactory defaultListableBeanFactory;

	private volatile boolean isServer;

	private ClassPool pool = ClassPool.getDefault();

	{
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}

	private String suffixName = "$proxyClass";

	private Scan scan;

	private Reflection reflection = Reflection.newInstance();
	private volatile boolean isActivate;

	private final Map<String, MethodBean> METHOD_BEAN = new ConcurrentHashMap<String, MethodBean>();

	public synchronized boolean isActivate() {
		return isActivate;
	}

	public synchronized void init(Map<String, Object> args) {
		if (null == args)
			return;
		try {
			String scanPath = (String) args.get("scan");
			Object tempValue = args.get("isServer");
			isServer = Boolean.valueOf(null == tempValue ? false : (Boolean) tempValue);
			scanRegistered(scanPath);
			xmlRegistered();
		} catch (Throwable e) {
			LOG.error(null, e);
		}
		isActivate = true;
	}

	public <T> T getBean(Class<T> c) throws Throwable {
		return applicationContext.getBean(c);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String serviceName) throws Throwable {
		Object result = applicationContext.getBean(serviceName);
		return null == result ? null : (T) result;
	}

	public MethodBean getMethod(String code) throws Throwable {
		return METHOD_BEAN.get(code);
	}

	public void scanRegistered(String path) throws Throwable {
		isActivate = true;
		if (null == scan)
			scan = new ScanSimple();
		Map<String, Set<String>> classes = scan.doScan(path);
		// 过滤
		String id;
		Class<?> tempClass;
		String version;
		boolean multiInstance;
		Set<String> classPaths;
		String className;
		Iterator<String> it;
		RmiClient client;
		RmiServer server;
		Annotation tempAnnotation;
		String serviceName;
		Method[] methods;
		// 扫描CLASS
		// for (String p : paths) {
		for (Entry<String, Set<String>> en : classes.entrySet()) {
			classPaths = en.getValue();
			if (null != classPaths) {
				it = classPaths.iterator();
				while (it.hasNext()) {
					className = it.next();
					try {
						if (className.endsWith("class")) {
							tempClass = Class.forName(className.replace("/", ".").replace(".class", ""));
							// 注解/xml
							if (null != (tempAnnotation = tempClass.getDeclaredAnnotation(RmiClient.class))) {
								// Rmi注解
								client = (RmiClient) tempAnnotation;

								id = (id = client.id()).length() > 0 ? id
										: ConverType.newInstance().toLowerCaseFirstOne(tempClass.getSimpleName());
								version = client.version();
								multiInstance = client.multiInstance();
								// 注册名
								serviceName = client.serviceImplementation();

								final ClassBean classBean = new ClassBean(tempClass, serviceName, version,
										multiInstance);

								// 生成代理类
								classBean.setProxyClassFile(classRefactor(classBean,
										com.cheuks.bin.original.reflect.rmi.RmiClient.class, SimpleRmiClient.class));
								if (!classBean.isMultiInstance())
									classBean.setInstance(classBean.getProxyClassFile().newInstance());

								// 注册
								BeanDefinitionBuilder bean = BeanDefinitionBuilder
										.genericBeanDefinition(classBean.getProxyClassFile());
								if (multiInstance)
									bean.setScope("prototype");
								// defaultListableBeanFactory.registerBeanDefinition(id,
								// bean.getRawBeanDefinition());

								if (LOG.isDebugEnabled())
									LOG.debug("RmiClient:" + classBean.getProxyClassFile().getName() + " ||  register:"
											+ classBean.getRegistrationServiceName());
							} // 注解/xml
							else if (null != (tempAnnotation = tempClass.getDeclaredAnnotation(RmiServer.class))) {
								// Rmi注解
								server = (RmiServer) tempAnnotation;

								version = server.version();
								multiInstance = server.multiInstance();
								// 注册名
								serviceName = server.serviceName();

								final ClassBean classBean = new ClassBean(tempClass, serviceName, version,
										multiInstance);
								classBean.setProxyClassFile(tempClass);
								// 分解Method
								//
								// 服务端
								methods = classBean.getOriginalClassFile().getDeclaredMethods();
								for (Method m : methods) {
									final MethodBean bean = MethodBean.builder(classBean, m);
									METHOD_BEAN.put(bean.getMd5Code(), bean);
								}
								if (LOG.isDebugEnabled())
									LOG.debug("RmiServer:" + classBean.getProxyClassFile().getName() + "||   register:"
											+ classBean.getRegistrationServiceName());
							}
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println(className);
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void xmlRegistered() throws Throwable {
		// ProtocolConfig protocolConfig =
		// applicationContext.getBean(ProtocolConfig.class);
		// ReferenceConfig referenceConfig =
		// applicationContext.getBean(ReferenceConfig.class);
		// ServiceConfig serviceConfig =
		// applicationContext.getBean(ServiceConfig.class);

		// 过滤
		String tempValue;
		String id = null;
		Class<?> tempClass;
		Class<?> tempInterface;
		String version;
		boolean multiInstance;
		ReferenceConfig tempReferenceConfig = null;
		ServiceConfig tempServiceConfig;
		Iterator<ReferenceConfig> referenceConfigIt;
		Iterator<ServiceConfig> serviceConfigIt;
		Method[] methods;
		referenceConfigIt = ReferenceConfig.getBeans().iterator();
		serviceConfigIt = ServiceConfig.getBeans().iterator();
		try {
			while (!isServer && referenceConfigIt.hasNext()) {
				tempReferenceConfig = referenceConfigIt.next();
				tempClass = Class
						.forName(tempReferenceConfig.getInterfaceName().replace("/", ".").replace(".class", ""));
				// 注解/xml
				// Rmi注解

				id = tempReferenceConfig.getId();
				version = tempReferenceConfig.getVersion();
				multiInstance = tempReferenceConfig.isMultiInstance();
				// // 注册名
				// serviceName = client.serviceImplementation();

				final ClassBean classBean = new ClassBean(tempClass, id, version, multiInstance);
				classBean.setInterfaceClassFile(tempClass);
				// 生成代理类
				classBean.setProxyClassFile(classRefactor(classBean,
						com.cheuks.bin.original.reflect.rmi.RmiClient.class, SimpleRmiClient.class));
				// if (!classBean.isMultiInstance())
				// classBean.setInstance(classBean.getProxyClassFile().newInstance());

				// 注册
				BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition(classBean.getProxyClassFile());
				bean.addPropertyValue("rmiClientInvokeMethod", applicationContext.getBean("rmiClientBean"));
				if (multiInstance)
					bean.setScope("prototype");
				defaultListableBeanFactory.registerBeanDefinition(id, bean.getRawBeanDefinition());

				if (LOG.isDebugEnabled())
					LOG.debug("RmiClient:" + classBean.getProxyClassFile().getName() + " ||  register:"
							+ classBean.getRegistrationServiceName());
				// 注解/xml
			}
			while (isServer && serviceConfigIt.hasNext()) {
				tempServiceConfig = serviceConfigIt.next();
				// Class tempInterface =
				// Class.forName(tempReferenceConfig.getInterfaceName());
				Object instance;
				// 注解/xml
				id = tempServiceConfig.getId();
				tempServiceConfig.getInterfaceName();
				String beanRef = id;
				version = tempServiceConfig.getVersion();
				multiInstance = tempServiceConfig.isMultiInstance();
				if (null != (tempValue = tempServiceConfig.getRef()) && tempValue.length() > 1) {
					beanRef = tempServiceConfig.getRef();
					// defaultListableBeanFactory.registerAlias(tempServiceConfig.getId(),
					// tempServiceConfig.getRef());
				} else {
					BeanDefinition beanDefinition = new RootBeanDefinition(
							Class.forName(tempServiceConfig.getRefClass()));
					defaultListableBeanFactory.registerBeanDefinition(id, beanDefinition);
				}
				instance = applicationContext.getBean(beanRef);
				tempInterface = Class.forName(tempServiceConfig.getInterfaceName());

				// 注册名

				Object o = instance.getClass();
				final ClassBean classBean = new ClassBean(instance.getClass(), id, instance, version);
				classBean.setProxyClassFile(instance.getClass());
				classBean.setInterfaceClassFile(tempInterface);
				// 分解Method
				//
				// 服务端
				methods = classBean.getOriginalClassFile().getDeclaredMethods();
				for (Method m : methods) {
					final MethodBean bean = MethodBean.builder(classBean, m);
					METHOD_BEAN.put(bean.getMd5Code(), bean);
				}
				if (LOG.isDebugEnabled())
					LOG.debug("RmiServer:" + classBean.getProxyClassFile().getName() + "||   register:"
							+ classBean.getRegistrationServiceName());
			}
		} catch (ClassNotFoundException e) {
			System.out.println(id);
			e.printStackTrace();
		}

	}

	// 普通类
	public Class<?> classRefactor(final ClassBean classBean, final Class<?> rmiClientInterface,
			final Class<?> rmiClientImpl) throws Throwable {

		boolean isInterface = classBean.getOriginalClassFile().isInterface();
		final String orginalClassName = classBean.getOriginalClassFile().getName();

		CtClass orginalClass = pool.get(orginalClassName);
		CtClass newClass = pool.makeClass(orginalClassName + getSuffixName());
		// 继承/实现
		if (isInterface)
			newClass.addInterface(orginalClass);
		else
			newClass.setSuperclass(orginalClass);

		// 添加注入
		// 添加 rmiclient里的field
		CtField rmiClientInvokeMethod = CtField.make(String.format("public %s rmiClientInvokeMethod;",
				rmiClientInterface.getName(), rmiClientImpl.getName()), newClass);
		CtClass autowiredClazz = pool.get("org.springframework.beans.factory.annotation.Autowired");
		AnnotationsAttribute autoired = new AnnotationsAttribute(newClass.getClassFile().getConstPool(),
				AnnotationsAttribute.visibleTag);
		javassist.bytecode.annotation.Annotation annotation = new javassist.bytecode.annotation.Annotation(
				newClass.getClassFile().getConstPool(), autowiredClazz);
		autoired.addAnnotation(annotation);
		// CtField rmiClientInvokeMethod = CtField.make(String.format("public %s
		// rmiClientInvokeMethod=new %s();", rmiClientInterface.getName(),
		// rmiClientImpl.getName()), newClass);
		rmiClientInvokeMethod.getFieldInfo().addAttribute(autoired);
		newClass.addField(rmiClientInvokeMethod);

		CtMethod getting = CtMethod.make(
				"public com.cheuks.bin.original.reflect.rmi.RmiClient getRmiClientInvokeMethod(){return rmiClientInvokeMethod;}",
				newClass);
		CtMethod setting = CtMethod.make(
				"public void setRmiClientInvokeMethod(com.cheuks.bin.original.reflect.rmi.RmiClient rmiClientInvokeMethod){this.rmiClientInvokeMethod=rmiClientInvokeMethod;}",
				newClass);
		newClass.addMethod(getting);
		newClass.addMethod(setting);

		// 方法重现
		CtMethod[] orginalClassMethods = orginalClass.getDeclaredMethods();
		// StringBuilder methodString = new StringBuilder();
		String methodString;
		for (final CtMethod m : orginalClassMethods) {
			// 转换模版: ((Integer) 1).intValue();
			methodString = generateMethod(m, null,
					convery4CodeByCtClass(
							"rmiClientInvokeMethod.rmiInvoke(\"" + reflection.genericRmiMethodMd5Code(
									classBean.getInterfaceClassFile().getName(), classBean.getVersion(), m) + "\",$args)",
							m.getReturnType()));
			newClass.addMethod(CtNewMethod.make(methodString, newClass));
		}
		// class输出
		newClass.writeFile("D:/Desktop/1");

		return newClass.toClass();
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

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}

	public String getSuffixName() {
		return suffixName;
	}

	public RmiBeanFactory setSuffixName(String suffixName) {
		this.suffixName = suffixName;
		return this;
	}

	public Scan getScan() {
		return scan;
	}

	public RmiBeanFactory setScan(Scan scan) {
		this.scan = scan;
		return this;
	}

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	public static void main(String[] args) {
		System.out.println("@org.springframework.beans.factory.annotation.Autowired\npublic %s rmiClientInvokeMethod;");
	}
}
