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
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.common.util.conver.ConverType;
import com.cheuks.bin.original.common.util.pool.ObjectPoolManager;
import com.cheuks.bin.original.rmi.GenerateRmiBeanFactory;
import com.cheuks.bin.original.rmi.config.model.ReferenceModel;
import com.cheuks.bin.original.rmi.net.netty.NettyRmiInvokeClientImpl;
import com.cheuks.bin.original.rmi.net.netty.client.NettyNetworkClient;

public class ReferenceGroupConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	private final ConverType converType = new ConverType();

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		String applicationName = element.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME);
		ReferenceGroupModel referenceGroupModel = doParser(applicationName, element, parserContext);
		doGenerate(parserContext, referenceGroupModel, applicationName);
		return this;
	}

	private ReferenceGroupModel doParser(String applicationName, Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		ReferenceGroupModel referenceGroupModel = getServiceGroupModel(parserContext, applicationName);
		ReferenceModel referenceModel;
		Node node;
		Element tempElement;
		String id;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				referenceModel = new ReferenceModel();
				referenceModel.setInterfaceName(id = tempElement.getAttribute("interface"));
				id = converType.isEmpty(tempElement.getAttribute("id"), converType.toLowerCaseFirstOne(id.substring(id.lastIndexOf(".") + 1)));
				referenceModel.setId(id);
				referenceModel.setVersion(tempElement.getAttribute("version"));
				referenceModel.setMultiInstance(Boolean.valueOf(element.getAttribute("multiInstance")));
				referenceGroupModel.getReferenceGroup().put(referenceModel.getId(), referenceModel);
			}
		}
		return referenceGroupModel;
	}

	void doGenerate(ParserContext parserContext, ReferenceGroupModel referenceGroupModel, String applicationName) {

		if (!parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP)) {
			throw new NullPointerException("The configuration sequence must be <rmi:config> in first.");
		}

		// objectPoolManager
		if (!parserContext.getRegistry().containsBeanDefinition(BEAN_OBJECT_POOL_MANAGER)) {
			registerBeanDefinition(parserContext, ObjectPoolManager.class, BEAN_OBJECT_POOL_MANAGER, null, null);
		}
		// RmiNetworkClinet
		if (!parserContext.getRegistry().containsBeanDefinition(BEAN_RMI_NETWORK_CLIENT)) {
			Map<String, Object> proprety = CollectionUtil.newInstance().toMap(BEAN_OBJECT_POOL_MANAGER, getConfig(parserContext, BEAN_OBJECT_POOL_MANAGER), RMI_CONFIG_BEAN_CONFIG_GROUP, getConfig(parserContext, RMI_CONFIG_BEAN_CONFIG_GROUP), BEAN_CACHE_SERIALIZE,
					getConfig(parserContext, BEAN_CACHE_SERIALIZE), BEAN_LOAD_BALANCE_FACTORY, getConfig(parserContext, BEAN_LOAD_BALANCE_FACTORY), RMI_CONFIG_BEAN_CONFIG_GROUP, getConfig(parserContext, RMI_CONFIG_BEAN_CONFIG_GROUP));
			registerBeanDefinition(parserContext, NettyNetworkClient.class, BEAN_RMI_NETWORK_CLIENT, proprety, "start");
			// NettyRmiInvokeClientImpl
			if (!parserContext.getRegistry().containsBeanDefinition(BEAN_RMI_INVOKE_CLIENT)) {
				// Map<String, Object> property = CollectionUtil.newInstance().toMap(BEAN_RMI_NETWORK_CLIENT, rmiMainClient);
				registerBeanDefinition(parserContext, NettyRmiInvokeClientImpl.class, BEAN_RMI_INVOKE_CLIENT, null, null);
			}
		}

		// 生成注入
		try {
			GenerateRmiBeanFactory.instance().referenceGroupHandle(parserContext, referenceGroupModel, applicationName);
		} catch (Throwable e) {
			throw new RuntimeException(e);
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
			referenceGroup = new ConcurrentSkipListMap<String, ReferenceGroupModel>();
			registerBeanDefinition(parserContext, ReferenceGroup.class, RMI_CONFIG_BEAN_REFERENCE_GROUP, CollectionUtil.newInstance().toMap(ReferenceGroup.REFERENCE_GROUP_FIELD_REFERENCE_GROUP, referenceGroup), null);
			// rmiConfigGroup注入
			BeanDefinition rmiConfigGroup = parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP);
			rmiConfigGroup.getPropertyValues().add(RMI_CONFIG_BEAN_CONFIG_REFERENCE_GROUP, parserContext.getRegistry().getBeanDefinition(RMI_CONFIG_BEAN_REFERENCE_GROUP));
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
