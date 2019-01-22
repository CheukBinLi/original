package com.cheuks.bin.original.rmi.config;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiBeanFactory;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.rmi.GenerateRmiBeanFactory;
import com.cheuks.bin.original.rmi.config.model.ServiceModel;
import com.cheuks.bin.original.rmi.model.MethodBean;
import com.cheuks.bin.original.rmi.net.netty.server.NettyServer;

public class ServiceGroupConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		String applicationName = element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME);
		ServiceGroupModel serviceGroup = doParser(applicationName, element, parserContext);
		doGenerate(parserContext, serviceGroup, applicationName);
		return this;
	}

	private ServiceGroupModel doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		ServiceGroupModel serviceGroup = getServiceGroup(parserContext, applicationName);
		ServiceModel serviceModel;
		Node node;
		Element tempElement;
		String id;
		String tempValue;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_SERVICE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SERVICE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				serviceModel = new ServiceModel();
				id = tempElement.getAttribute("id");
				serviceModel.setVersion(tempElement.getAttribute("version"));
				serviceModel.setRef(tempValue = tempElement.getAttribute("ref"));
				if (StringUtil.isEmpty(id) && !StringUtil.isEmpty(tempValue)) {
					id = StringUtil.toLowerCaseFirstOne(tempValue.substring(tempValue.lastIndexOf(".") + 1) + "_v" + serviceModel.getVersion());
				}
				serviceModel.setRefClass(tempValue = tempElement.getAttribute("class"));
				if (StringUtil.isEmpty(id) && !StringUtil.isEmpty(tempValue)) {
					id = StringUtil.toLowerCaseFirstOne(tempValue.substring(tempValue.lastIndexOf(".") + 1) + "_v" + serviceModel.getVersion());
				}
				serviceModel.setInterfaceName(tempValue = tempElement.getAttribute("interface"));
				if (StringUtil.isEmpty(id) && !StringUtil.isEmpty(tempValue)) {
					id = StringUtil.toLowerCaseFirstOne(tempValue.substring(tempValue.lastIndexOf(".") + 1) + "_v" + serviceModel.getVersion());
				}
				serviceModel.setId(id);
				serviceModel.setDescribe(tempElement.getAttribute("describe"));
				serviceModel.setMultiInstance(Boolean.valueOf(tempElement.getAttribute("multiInstance")));
				// serviceModels.add(serviceModel);
				serviceGroup.getServices().put(serviceModel.getId(), serviceModel);
			}
		}
		return serviceGroup;
	}

	@SuppressWarnings("unchecked")
	void doGenerate(ParserContext parserContext, ServiceGroupModel serviceGroupModel, String applicationName) {

		if (!parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP)) {
			throw new NullPointerException("The configuration sequence must be <rmi:config> in first.");
		}
		// 调用工厂
		BeanDefinition rmiBeanFactory = parserContext.getRegistry().getBeanDefinition(BEAN_RMI_BEAN_FACTORY);
		Map<String, MethodBean> methodBeans = (Map<String, MethodBean>) rmiBeanFactory.getPropertyValues().get(RmiBeanFactory.POOL_OBJECT_FIELD_NAME);
		if (null == methodBeans) {
			rmiBeanFactory.getPropertyValues().add(RmiBeanFactory.POOL_OBJECT_FIELD_NAME, methodBeans = new ConcurrentSkipListMap<String, MethodBean>());
		}
		// 服务端
		if (!parserContext.getRegistry().containsBeanDefinition(BEAN_RMI_NETWORK_SERVER)) {
			Map<String, Object> property = CollectionUtil.toMap(RMI_CONFIG_BEAN_CONFIG_GROUP, getConfig(parserContext, RMI_CONFIG_BEAN_CONFIG_GROUP), BEAN_CACHE_SERIALIZE, getConfig(parserContext, BEAN_CACHE_SERIALIZE), BEAN_RMI_BEAN_FACTORY, rmiBeanFactory, BEAN_LOAD_BALANCE_FACTORY,
					getConfig(parserContext, BEAN_LOAD_BALANCE_FACTORY));

			registerBeanDefinition(parserContext, NettyServer.class, BEAN_RMI_NETWORK_SERVER, property, "start");
		}

		// 生成注入
		try {
			GenerateRmiBeanFactory.instance().serviceGroupHandle(parserContext, serviceGroupModel, methodBeans, applicationName);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	ServiceGroupModel getServiceGroup(ParserContext parserContext, String applicationName) {
		Map<String, ServiceGroupModel> serviceGroups;
		ServiceGroupModel serviceGroup;
		BeanDefinition bean;
		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP)) {
			bean = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
			serviceGroups = (Map<String, ServiceGroupModel>) bean.getPropertyValues().get(ServiceGroup.SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG);
		} else {
			serviceGroups = new ConcurrentSkipListMap<String, ServiceGroupModel>();
			registerBeanDefinition(parserContext, ServiceGroup.class, RMI_CONFIG_BEAN_SERVICE_GROUP, CollectionUtil.toMap(ServiceGroup.SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG, serviceGroups), null);
			// rmiConfigGroup注入
			BeanDefinition rmiConfigGroup = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP);
			rmiConfigGroup.getPropertyValues().add(RMI_CONFIG_BEAN_CONFIG_SERVICE_GROUP, parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP));
		}
		if (null == (serviceGroup = serviceGroups.get(applicationName))) {
			serviceGroups.put(applicationName, serviceGroup = new ServiceGroupModel(applicationName, true));
		}
		return serviceGroup;
	}

	public static class ServiceGroupModel implements Serializable {
		private static final long serialVersionUID = 1L;

		public static final String SERVICE_GROUP_FIELD_APPLICATION_NAME = "applicationName";

		public static final String SERVICE_GROUP_FIELD_SERVICES = "services";

		private String applicationName;

		private Map<String, ServiceModel> services;

		public String getApplicationName() {
			return applicationName;
		}

		public ServiceGroupModel setApplicationName(String applicationName) {
			this.applicationName = applicationName;
			return this;
		}

		public Map<String, ServiceModel> getServices() {
			return services;
		}

		public ServiceGroupModel setServices(Map<String, ServiceModel> services) {
			this.services = services;
			return this;
		}

		public ServiceGroupModel(String applicationName, boolean isInit) {
			this(applicationName);
			if (isInit) {
				services = new ConcurrentSkipListMap<String, ServiceModel>();
			}
		}

		public ServiceGroupModel(String applicationName) {
			this.applicationName = applicationName;
		}

	}

	public static class ServiceGroup implements Serializable {

		private static final long serialVersionUID = 6837902294743194757L;

		public static final String SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG = "serviceGroupConfig";

		private Map<String, ServiceGroupModel> serviceGroupConfig;

		public Map<String, ServiceGroupModel> getServiceGroupConfig() {
			return serviceGroupConfig;
		}

		public ServiceGroup setServiceGroupConfig(Map<String, ServiceGroupModel> serviceGroupConfig) {
			this.serviceGroupConfig = serviceGroupConfig;
			return this;
		}

	}
}
