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
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroup;
import com.cheuks.bin.original.rmi.config.model.ReferenceModel;

public class ReferenceGroupConfig extends AbstractConfig implements RmiContent {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME), element, parserContext);
		return this;
	}

	private void doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		List<ReferenceModel> referenceModels = getServiceGroupList(parserContext, applicationName);
		ReferenceModel referenceModel;
		if (null == referenceModels)
			referenceModels = new ArrayList<ReferenceModel>();
		Node node;
		Element tempElement;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_SERVICE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SERVICE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				referenceModel = new ReferenceModel();
				referenceModel.setId(tempElement.getAttribute("id"));
				referenceModel.setInterfaceName(tempElement.getAttribute("interface"));
				referenceModel.setVersion(tempElement.getAttribute("version"));
				referenceModels.add(referenceModel);
			}
		}
	}

	@SuppressWarnings("unchecked")
	List<ReferenceModel> getServiceGroupList(ParserContext parserContext, String applicationName) {
		BeanDefinition beanDefinition = null;
		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP)) {
			beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP);
		} else {
			synchronized (this) {
				if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP))
					beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP);
				else if (null == beanDefinition) {
					beanDefinition = new RootBeanDefinition(ServiceGroup.class);
					parserContext.getRegistry().registerBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP, beanDefinition);
				}
			}
		}
		Map<String, List<ReferenceModel>> referenceGroup = (Map<String, List<ReferenceModel>>) beanDefinition.getPropertyValues().get(RMI_CONFIG_BEAN_REFERENCE_GROUP);
		if (null == referenceGroup) {
			referenceGroup = new ConcurrentSkipListMap<String, List<ReferenceModel>>();
			beanDefinition.getPropertyValues().add(RMI_CONFIG_BEAN_REFERENCE_GROUP, referenceGroup);
		}
		List<ReferenceModel> result = referenceGroup.get(applicationName);
		if (null == result) {
			result = new ArrayList<ReferenceModel>();
			referenceGroup.put(applicationName, result);
		}
		return result;
	}

	public static class ReferenceGroup implements Serializable {
		private static final long serialVersionUID = 1L;

		private Map<String, List<ReferenceModel>> referenceGroup;

		public Map<String, List<ReferenceModel>> getReferenceGroup() {
			return referenceGroup;
		}

		public ReferenceGroup setReferenceGroup(Map<String, List<ReferenceModel>> referenceGroup) {
			this.referenceGroup = referenceGroup;
			return this;
		}

	}
}
