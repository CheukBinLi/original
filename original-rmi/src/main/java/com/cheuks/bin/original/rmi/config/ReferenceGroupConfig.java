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
import com.cheuks.bin.original.rmi.config.model.ReferenceModel;

public class ReferenceGroupConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME), element, parserContext);
		return this;
	}

	private void doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		ReferenceGroupModel referenceGroupModel = getServiceGroupModel(parserContext, applicationName);
		ReferenceModel referenceModel;
		Node node;
		Element tempElement;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			System.err.println(RMI_CONFIG_ELEMENT_REFERENCE + "==" + node.getNodeName() + "  ||  " + node.getLocalName());
			if (RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				referenceModel = new ReferenceModel();
				referenceModel.setId(tempElement.getAttribute("id"));
				referenceModel.setInterfaceName(tempElement.getAttribute("interface"));
				referenceModel.setVersion(tempElement.getAttribute("version"));
				referenceModel.setMultiInstance(Boolean.valueOf(element.getAttribute("multiInstance")));
				// referenceModels.add(referenceModel);
				referenceGroupModel.getReferenceGroup().put(referenceModel.getId(), referenceModel);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected ReferenceGroupModel getServiceGroupModel(ParserContext parserContext, String applicationName) {

		BeanDefinition beanDefinition = null;
		Map<String, ReferenceGroupModel> referenceGroup;

		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP)) {
			beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP);
			referenceGroup = (Map<String, ReferenceGroupModel>) beanDefinition.getPropertyValues().get(ReferenceGroup.REFERENCE_GROUP_FIELD_REFERENCE_GROUP);
		} else {
			// synchronized (this) {
			// if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP)) {
			// beanDefinition = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP);
			// referenceGroup = (Map<String, ReferenceGroupModel>) beanDefinition.getPropertyValues().get(ReferenceGroup.REFERENCE_GROUP_FIELD_REFERENCE_GROUP);
			// } else {
			referenceGroup = new ConcurrentSkipListMap<String, ReferenceGroupModel>();
			// beanDefinition = new RootBeanDefinition(ReferenceGroup.class);
			// parserContext.getRegistry().registerBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP, beanDefinition);
			// beanDefinition.getPropertyValues().add(ReferenceGroup.REFERENCE_GROUP_FIELD_REFERENCE_GROUP, referenceGroup = new ConcurrentSkipListMap<String, ReferenceGroupModel>());
			// parserContext.getRegistry().registerBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP, beanDefinition);
			registerBeanDefinition(parserContext, ReferenceGroup.class, RMI_CONFIG_BEAN_REFERENCE_GROUP, CollectionUtil.newInstance().toMap(ReferenceGroup.REFERENCE_GROUP_FIELD_REFERENCE_GROUP, referenceGroup));
			// }
			// }
		}

		ReferenceGroupModel referenceGroupModel = referenceGroup.get(applicationName);
		if (null == referenceGroupModel) {
			referenceGroup.put(applicationName, referenceGroupModel = new ReferenceGroupModel(applicationName, true));
		}

		return referenceGroupModel;
	}

	public static class ReferenceGroupModel implements Serializable {
		private static final long serialVersionUID = 1L;

		private Map<String, ReferenceModel> referenceGroup;

		private String applicationName;

		public Map<String, ReferenceModel> getReferenceGroup() {
			return referenceGroup;
		}

		public ReferenceGroupModel setReferenceGroup(Map<String, ReferenceModel> referenceGroup) {
			this.referenceGroup = referenceGroup;
			return this;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public ReferenceGroupModel setApplicationName(String applicationName) {
			this.applicationName = applicationName;
			return this;
		}

		public ReferenceGroupModel(String applicationName) {
			super();
			this.applicationName = applicationName;
		}

		public ReferenceGroupModel(String applicationName, boolean isInit) {
			this(applicationName);
			if (isInit)
				this.referenceGroup = new ConcurrentSkipListMap<String, ReferenceModel>();
		}

	}

	public static class ReferenceGroup implements Serializable {
		private static final long serialVersionUID = 1L;

		private final static String REFERENCE_GROUP_FIELD_REFERENCE_GROUP = "referenceGroup";

		private Map<String, ReferenceGroupModel> referenceGroup;

		public Map<String, ReferenceGroupModel> getReferenceGroup() {
			return referenceGroup;
		}

		public ReferenceGroup setReferenceGroup(Map<String, ReferenceGroupModel> referenceGroup) {
			this.referenceGroup = referenceGroup;
			return this;
		}

	}
}
