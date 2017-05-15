package com.cheuks.bin.original.rmi.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiContent;
import com.cheuks.bin.original.rmi.config.model.ServiceModel;

public class ServiceGroupConfig extends AbstractConfig implements RmiContent {

	private static final long serialVersionUID = 1L;

	//	private static Map<String, List<ServiceModel>> serviceGroup = new ConcurrentSkipListMap<String, List<ServiceModel>>();

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME), element, parserContext);
		return this;
	}

	private void doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();

		List<ServiceModel> serviceModels = getServiceGroupList(parserContext, applicationName);
		ServiceModel serviceModel;
		if (null == serviceModels)
			serviceModels = new ArrayList<ServiceModel>();
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
				serviceModels.add(serviceModel);
			}
		}
	}

	@SuppressWarnings("unchecked")
	List<ServiceModel> getServiceGroupList(ParserContext parserContext, String applicationName) {
		BeanDefinition beanDefinition = null;
		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP)) {
			beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
		} else {
			synchronized (this) {
				if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP))
					beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP);
				else if (null == beanDefinition) {
					beanDefinition = new RootBeanDefinition(ServiceGroup.class);
					parserContext.getRegistry().registerBeanDefinition(RMI_CONFIG_BEAN_SERVICE_GROUP, beanDefinition);
				}
			}
		}
		Map<String, List<ServiceModel>> serviceGroup = (Map<String, List<ServiceModel>>) beanDefinition.getPropertyValues().get(RMI_CONFIG_BEAN_SERVICE_GROUP);
		if (null == serviceGroup) {
			serviceGroup = new ConcurrentSkipListMap<String, List<ServiceModel>>();
			beanDefinition.getPropertyValues().add(RMI_CONFIG_BEAN_SERVICE_GROUP, serviceGroup);
		}
		List<ServiceModel> result = serviceGroup.get(applicationName);
		if (null == result) {
			result = new ArrayList<ServiceModel>();
			serviceGroup.put(applicationName, result);
		}
		return result;
	}

	public static class ServiceGroup implements Serializable {
		private static final long serialVersionUID = 1L;

		private Map<String, List<ServiceModel>> serviceGroup;

		public Map<String, List<ServiceModel>> getServiceGroup() {
			return serviceGroup;
		}

		public ServiceGroup setServiceGroup(Map<String, List<ServiceModel>> serviceGroup) {
			this.serviceGroup = serviceGroup;
			return this;
		}

	}
}
