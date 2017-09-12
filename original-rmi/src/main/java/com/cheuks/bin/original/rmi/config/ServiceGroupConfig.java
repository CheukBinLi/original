package com.cheuks.bin.original.rmi.config;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.rmi.config.model.ServiceModel;

public class ServiceGroupConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME), element, parserContext);
		return this;
	}

	private void doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		ServiceGroupModel serviceGroup = getServiceGroup(parserContext, applicationName);
		ServiceModel serviceModel;
		Node node;
		Element tempElement;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_SERVICE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SERVICE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				serviceModel = new ServiceModel();
				serviceModel.setId(tempElement.getAttribute("id"));
				serviceModel.setInterfaceName(tempElement.getAttribute("interface"));
				serviceModel.setRefClass(tempElement.getAttribute("class"));
				serviceModel.setRef(tempElement.getAttribute("ref"));
				serviceModel.setDescribe(tempElement.getAttribute("version"));
				serviceModel.setVersion(tempElement.getAttribute("version"));
				serviceModel.setMultiInstance(Boolean.valueOf(tempElement.getAttribute("multiInstance")));
				// serviceModels.add(serviceModel);
				serviceGroup.getServices().put(serviceModel.getId(), serviceModel);
			}
		}

	}

	@SuppressWarnings("unchecked")
	protected ServiceGroupModel getServiceGroup(ParserContext parserContext, String applicationName) {
		Map<String, ServiceGroupModel> serviceGroups;
		ServiceGroupModel serviceGroup;
		BeanDefinition bean;
		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP)) {
			bean = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
			serviceGroups = (Map<String, ServiceGroupModel>) bean.getPropertyValues().get(ServiceGroup.SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG);
		} else {
//			bean = new RootBeanDefinition(ServiceGroup.class);
			serviceGroups = new ConcurrentSkipListMap<String, ServiceGroupModel>();
//			bean.getPropertyValues().add(ServiceGroup.SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG, serviceGroups);
			registerBeanDefinition(parserContext, ServiceGroup.class, RMI_CONFIG_BEAN_SERVICE_GROUP, CollectionUtil.newInstance().toMap(ServiceGroup.SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG, serviceGroups));
		}
		if (null == (serviceGroup = serviceGroups.get(applicationName))) {
			serviceGroups.put(applicationName, serviceGroup = new ServiceGroupModel(applicationName, true));
		}
		return serviceGroup;
	}

	// List<ServiceModel> getServiceGroupList(ParserContext parserContext, String applicationName) {
	// BeanDefinition beanDefinition = null;
	// if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP)) {
	// beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
	// } else {
	// synchronized (this) {
	// if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP))
	// beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
	// else if (null == beanDefinition) {
	// beanDefinition = new RootBeanDefinition(ServiceGroup.class);
	// parserContext.getRegistry().registerBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP, beanDefinition);
	// }
	// }
	// }
	// ServiceGroup serviceGroup = beanDefinition.getPropertyValues().get("SERVICE_GROUP_CONFIG_MODEL_FIELD_SERVICE_GROUP_CONFIG");
	// if (null == serviceGroup) {
	// serviceGroup = new ConcurrentSkipListMap<String, List<ServiceModel>>();
	// beanDefinition.getPropertyValues().add(RMI_CONFIG_BEAN_SERVICE_GROUP, serviceGroup);
	// }
	// List<ServiceModel> result = serviceGroup.get(applicationName);
	// if (null == result) {
	// result = new ArrayList<ServiceModel>();
	// serviceGroup.put(applicationName, result);
	// }
	// return result;
	// return null;
	// }

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
