package com.cheuks.bin.original.rmi;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;

import com.cheuks.bin.original.common.annotation.rmi.RmiConsumerAnnotation;
import com.cheuks.bin.original.common.annotation.rmi.RmiProviderAnnotation;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.rmi.RmiInvokeClient;
import com.cheuks.bin.original.common.rmi.model.ClassBean;
import com.cheuks.bin.original.common.util.conver.ConverType;
import com.cheuks.bin.original.common.util.scan.Scan;
import com.cheuks.bin.original.common.util.scan.ScanSimple;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroupModel;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroupModel;
import com.cheuks.bin.original.rmi.config.model.ReferenceModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;
import com.cheuks.bin.original.rmi.config.model.ServiceModel;
import com.cheuks.bin.original.rmi.model.MethodBean;
import com.cheuks.bin.original.rmi.net.netty.NettyRmiInvokeClientImpl;
import com.cheuks.bin.original.rmi.unit.Reflection;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;

public final class GenerateRmiBeanFactory implements RmiContant {

	private static final Logger LOG = LoggerFactory.getLogger(GenerateRmiBeanFactory.class);

	private Scan scan;

	private static GenerateRmiBeanFactory INSTANCE;

	public static final GenerateRmiBeanFactory instance() {
		if (null == INSTANCE) {
			synchronized (GenerateRmiBeanFactory.class) {
				if (null == INSTANCE) {
					INSTANCE = new GenerateRmiBeanFactory();
				}
			}
		}
		return INSTANCE;
	}

	private ClassPool pool = ClassPool.getDefault();
	{
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}

	private ConverType converType = new ConverType();

	private final String suffixName = "$proxyClass";

	public String getSuffixName(String nick) {
		if (null != nick)
			return "$" + nick + suffixName;
		return suffixName;
	}

	public void referenceGroupHandle(ParserContext parserContext, ReferenceGroupModel referenceGroupModel, String service) throws Throwable {
		// 过滤
		String id = null;
		Class<?> tempClass;
		String version;
		boolean multiInstance;
		ReferenceModel referenceConfig;
		// 第二层
		for (Entry<String, ReferenceModel> en : referenceGroupModel.getReferenceGroup().entrySet()) {
			referenceConfig = en.getValue();
			multiInstance = referenceConfig.getMultiInstance();
			tempClass = Class.forName(referenceConfig.getInterfaceName().replace("/", ".").replace(".class", ""));

			id = referenceConfig.getId();
			version = referenceConfig.getVersion();
			// // 注册名
			// serviceName = client.serviceImplementation();

			final ClassBean classBean = new ClassBean(tempClass, id, version, multiInstance);
			classBean.setInterfaceClassFile(tempClass);
			// 应用名
			classBean.setRegistrationServiceName(referenceGroupModel.getApplicationName());
			// 生成代理类
			classBean.setProxyClassFile(classRefactor(classBean, RmiInvokeClient.class, NettyRmiInvokeClientImpl.class, service));

			// 注册
			BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition(classBean.getProxyClassFile());

			BeanDefinition networkClient = parserContext.getRegistry().getBeanDefinition(BEAN_RMI_INVOKE_CLIENT);

			// bean.addPropertyValue("rmiClientInvokeMethod", defaultListableBeanFactory.getBean(NettyRmiInvokeClientImpl.class));
			bean.addPropertyValue("rmiClientInvokeMethod", networkClient);
			if (multiInstance)
				bean.setScope("prototype");
			// defaultListableBeanFactory.registerBeanDefinition(id, bean.getRawBeanDefinition());
			parserContext.getRegistry().registerBeanDefinition(id, bean.getRawBeanDefinition());

			if (LOG.isDebugEnabled())
				LOG.debug("RmiClient:" + classBean.getProxyClassFile().getName() + " ||  register:" + classBean.getRegistrationServiceName());
			// 注解/xml
		}
	}

	public void serviceGroupHandle(ParserContext parserContext, ServiceGroupModel serviceGroupModel, final Map<String, MethodBean> METHOD_BEAN, String application) throws Throwable {

		// DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		// 过滤
		String tempValue;
		String id = null;
		Class<?> tempInterface;
		String version;
		ServiceModel serviceConfig;
		Method[] methods;
		for (Entry<String, ServiceModel> en : serviceGroupModel.getServices().entrySet()) {
			serviceConfig = en.getValue();
			// 注解/xml
			id = serviceConfig.getId();
			serviceConfig.getInterfaceName();
			String beanRef = id;
			// 已注册，跳过
			if (parserContext.getRegistry().containsBeanDefinition(beanRef))
				continue;
			version = serviceConfig.getVersion();
			BeanDefinition serviceImpl;
			// multiInstance = tempServiceConfig.isMultiInstance();
			if (null != (beanRef = tempValue = serviceConfig.getRef()) && parserContext.getRegistry().containsBeanDefinition(tempValue)) {
				serviceImpl = parserContext.getRegistry().getBeanDefinition(beanRef);
			} else {
				serviceImpl = new RootBeanDefinition(Class.forName(serviceConfig.getRefClass()));
				parserContext.getRegistry().registerBeanDefinition(id, serviceImpl);
			}
			tempInterface = Class.forName(serviceConfig.getInterfaceName());

			// 注册
			final ClassBean classBean = new ClassBean();
			classBean.setRegistrationServiceName(application).setId(id).setVersion(version);
			classBean.setProxyClassFile(serviceImpl.getClass());
			classBean.setOriginalClassFile(tempInterface);
			classBean.setInterfaceClassFile(tempInterface);
			// 分解Method
			// 服务端
			methods = classBean.getOriginalClassFile().getDeclaredMethods();
			for (Method m : methods) {
				final MethodBean bean = MethodBean.builder(classBean, m);
				METHOD_BEAN.put(bean.getMd5Code(), bean);
				// rmiBeanFactory.putMethod(bean.getMd5Code(), bean);
			}
			if (LOG.isDebugEnabled())
				LOG.debug("RmiServer:" + classBean.getProxyClassFile().getName() + "||   register:" + classBean.getRegistrationServiceName());
		}
	}

	public ReferenceGroupModel scanByReferenceGroupHandle(ScanModel scanModel) throws Throwable {
		if (null == scan) {
			scan = new ScanSimple();
		}
		Map<String, Set<String>> clazzes = scan.doScan(scanModel.getPackagePath());
		ReferenceGroupModel referenceGroupModel = new ReferenceGroupModel(scanModel.getServiceName(), true);
		// 过滤
		Class<?> tempClass;
		Set<String> classPaths;
		String className;
		Iterator<String> it;
		RmiConsumerAnnotation consumer;
		ReferenceModel referenceModel;
		// 扫描CLASS
		for (Entry<String, Set<String>> en : clazzes.entrySet()) {
			classPaths = en.getValue();
			if (null != classPaths) {
				it = classPaths.iterator();
				while (it.hasNext()) {
					className = it.next();
					if (className.endsWith("class")) {
						tempClass = Class.forName(className.replace("/", ".").replace(".class", ""));
						// 注解/xml
						if (null != (consumer = tempClass.getDeclaredAnnotation(RmiConsumerAnnotation.class))) {
							// Rmi注解
							referenceModel = new ReferenceModel();
							referenceModel.setId(converType.isEmpty(consumer.id(), converType.toLowerCaseFirstOne(tempClass.getSimpleName())));
							referenceModel.setInterfaceName(tempClass.getName());
							referenceModel.setMultiInstance(false);
							referenceModel.setVersion(converType.isEmpty(consumer.version(), scanModel.getVersion()));
							referenceGroupModel.getReferenceGroup().put(referenceModel.getId(), referenceModel);
						}
					}
				}
			}
		}

		return referenceGroupModel;
	}

	public ServiceGroupModel scanByServiceGroupHandle(ScanModel scanModel) throws Throwable {
		if (null == scan) {
			scan = new ScanSimple();
		}
		Map<String, Set<String>> clazzes = scan.doScan(scanModel.getPackagePath());
		ServiceGroupModel serviceGroupModel = new ServiceGroupModel(scanModel.getServiceName(), true);
		// 过滤
		Class<?> tempClass;
		Set<String> classPaths;
		String className;
		Iterator<String> it;
		RmiProviderAnnotation provider;
		ServiceModel serviceModel;
		// 扫描CLASS
		for (Entry<String, Set<String>> en : clazzes.entrySet()) {
			classPaths = en.getValue();
			if (null != classPaths) {
				it = classPaths.iterator();
				while (it.hasNext()) {
					className = it.next();
					if (className.endsWith("class")) {
						tempClass = Class.forName(className.replace("/", ".").replace(".class", ""));
						// 注解/xml
						if (null != (provider = tempClass.getDeclaredAnnotation(RmiProviderAnnotation.class))) {
							// Rmi注解
							serviceModel = new ServiceModel();
							serviceModel.setInterfaceName(provider.interfaceClass().getName());
							serviceModel.setVersion(converType.isEmpty(provider.version(), scanModel.getVersion()));
							serviceModel.setId(converType.isEmpty(provider.id(), converType.toLowerCaseFirstOne(tempClass.getSimpleName())));
							serviceModel.setRefClass(tempClass.getName());
							serviceGroupModel.getServices().put(serviceModel.getId(), serviceModel);
						}
					}
				}
			}
		}

		return serviceGroupModel;
	}

	// 普通类
	public Class<?> classRefactor(final ClassBean classBean, final Class<?> rmiClientInterface, final Class<?> rmiClientImpl, String suffix) throws Throwable {

		boolean isInterface = classBean.getOriginalClassFile().isInterface();
		final String orginalClassName = classBean.getOriginalClassFile().getName();

		CtClass orginalClass = pool.get(orginalClassName);
		CtClass newClass = pool.makeClass(orginalClassName + getSuffixName(suffix));
		// 继承/实现
		if (isInterface)
			newClass.addInterface(orginalClass);
		else
			newClass.setSuperclass(orginalClass);

		// 添加注入
		// 添加 rmiclient里的field
		CtField rmiClientInvokeMethod = CtField.make(String.format("public %s rmiClientInvokeMethod;", rmiClientInterface.getName(), rmiClientImpl.getName()), newClass);
		CtClass autowiredClazz = pool.get("org.springframework.beans.factory.annotation.Autowired");
		AnnotationsAttribute autoired = new AnnotationsAttribute(newClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
		javassist.bytecode.annotation.Annotation annotation = new javassist.bytecode.annotation.Annotation(newClass.getClassFile().getConstPool(), autowiredClazz);
		autoired.addAnnotation(annotation);

		rmiClientInvokeMethod.getFieldInfo().addAttribute(autoired);
		newClass.addField(rmiClientInvokeMethod);

		CtMethod getting = CtMethod.make("public com.cheuks.bin.original.common.rmi.RmiInvokeClient getRmiClientInvokeMethod(){return rmiClientInvokeMethod;}", newClass);
		CtMethod setting = CtMethod.make("public void setRmiClientInvokeMethod(com.cheuks.bin.original.common.rmi.RmiInvokeClient rmiClientInvokeMethod){this.rmiClientInvokeMethod=rmiClientInvokeMethod;}", newClass);
		newClass.addMethod(getting);
		newClass.addMethod(setting);

		// 方法重现
		CtMethod[] orginalClassMethods = orginalClass.getDeclaredMethods();
		String methodString;
		for (final CtMethod m : orginalClassMethods) {
			// 转换模版: ((Integer) 1).intValue();
			methodString = generateMethod(m, null,
					convery4CodeByCtClass(
							"rmiClientInvokeMethod.rmiInvoke(\"" + classBean.getRegistrationServiceName() + "\",\"" + Reflection.newInstance().genericRmiMethodMd5Code(classBean.getRegistrationServiceName(), classBean.getInterfaceClassFile().getName(), classBean.getVersion(), m) + "\",$args)",
							m.getReturnType()));
			newClass.addMethod(CtNewMethod.make(methodString, newClass));
		}
		// newClass.writeFile("D:/Desktop/1");

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
		// if (LOG.isDebugEnabled())
		// LOG.debug(sb.toString());
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
			return "((Boolean)" + objectName + ").booleanValue()";
		} else if (("float").equals(typeName)) {
			return "((Float)" + objectName + ").floatValue()";
		}
		return String.format("(%s)%s", t.getName(), objectName);
	}

}
