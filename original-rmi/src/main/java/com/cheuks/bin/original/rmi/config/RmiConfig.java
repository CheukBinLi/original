package com.cheuks.bin.original.rmi.config;

import java.net.InetAddress;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.cache.FstCacheSerialize;
import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.util.conver.CollectionUtil;
import com.cheuks.bin.original.rmi.DefaultRmiBeanFactory;
import com.cheuks.bin.original.rmi.SimpleRmiService;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroup;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroup;
import com.cheuks.bin.original.rmi.config.model.ProtocolModel;
import com.cheuks.bin.original.rmi.config.model.RegistryModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;
import com.cheuks.bin.original.rmi.net.ConsulLoadBalanceFactory;
import com.cheuks.bin.original.rmi.net.P2pLoadBalanceFactory;
import com.cheuks.bin.original.rmi.net.ZookeeperLoadBalanceFactory;

public class RmiConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		if (parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP))
			return this;
		RmiConfigGroup rmiConfigGroup = doParser(element, parserContext);
		registerBeanDefinition(parserContext, RmiConfigGroup.class, RMI_CONFIG_BEAN_CONFIG_GROUP, CollectionUtil.newInstance().toMap("registryModel", rmiConfigGroup.getRegistryModel(), "protocolModel", rmiConfigGroup.getProtocolModel(), "scanModel", rmiConfigGroup.scanModel));
		doGenerate(parserContext, rmiConfigGroup);
		return this;
	}

	private void doGenerate(ParserContext parserContext, final RmiConfigGroup rmiConfigGroup) {
		// 初始化
		String tempValue;
		if (null != (tempValue = rmiConfigGroup.getProtocolModel().getRefSerialize()) && parserContext.getRegistry().containsBeanDefinition(tempValue)) {
			parserContext.getRegistry().registerBeanDefinition(BEAN_CACHE_SERIALIZE, parserContext.getRegistry().getBeanDefinition(tempValue));
		} else {
			registerBeanDefinition(parserContext, FstCacheSerialize.class, BEAN_CACHE_SERIALIZE, null);
		}
		// 调用工厂 rmiBeanFactory
		if (null != (tempValue = rmiConfigGroup.getProtocolModel().getRefRmiBeanFactory()) && parserContext.getRegistry().containsBeanDefinition(tempValue)) {
			// 别名
			parserContext.getRegistry().registerBeanDefinition(BEAN_RMI_BEAN_FACTORY, parserContext.getRegistry().getBeanDefinition(tempValue));
		} else {
			if (!parserContext.getRegistry().containsBeanDefinition(BEAN_RMI_BEAN_FACTORY))
				registerBeanDefinition(parserContext, DefaultRmiBeanFactory.class, BEAN_RMI_BEAN_FACTORY, null);
		}
		// loadBalanceFactory
		if (!parserContext.getRegistry().containsBeanDefinition(BEAN_LOAD_BALANCE_FACTORY)) {
			// 协议过滤(后继)
			String address = rmiConfigGroup.getRegistryModel().getServerAddress();
			String[] addresses;
			Class<?> loadBalanceFactory = null;
			if (address.contains("://")) {
				addresses = address.split("://");
				if ("zookeeper".equals(addresses[0].toLowerCase())) {
					loadBalanceFactory = ZookeeperLoadBalanceFactory.class;
				} else if ("consul".equals(addresses[0].toLowerCase())) {
					loadBalanceFactory = ConsulLoadBalanceFactory.class;
				} else if ("p2p".equals(addresses[0].toLowerCase())) {
					loadBalanceFactory = P2pLoadBalanceFactory.class;
				}
				address = addresses[1];
			} else {
				loadBalanceFactory = P2pLoadBalanceFactory.class;
			}
			//
			registerBeanDefinition(parserContext, loadBalanceFactory, BEAN_LOAD_BALANCE_FACTORY, CollectionUtil.newInstance().toMap("url", address));
		}
		// simpleRmiService初始化
		if (!parserContext.getRegistry().containsBeanDefinition(BEAN_RMI_SERVICE_INIT)) {
			registerBeanDefinition(parserContext, SimpleRmiService.class, BEAN_RMI_SERVICE_INIT, null);
		}
	}

	private RmiConfigGroup doParser(Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		Node node;
		Element tempElement;
		// Map<String, Object> property;
		RmiConfigGroup rmiConfigGroup = new RmiConfigGroup();
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_PROTOCOL.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_PROTOCOL.equals(node.getLocalName())) {
				tempElement = (Element) node;
				ProtocolModel protocolModel = new ProtocolModel();
				protocolModel.setLocalName(tempElement.getAttribute("localName"));
				protocolModel.setLocalAddress(tempElement.getAttribute("localAddress"));
				protocolModel.setPort(Integer.valueOf(tempElement.getAttribute("port")));
				protocolModel.setNetWorkThreads(Integer.valueOf(tempElement.getAttribute("netWorkThreads")));
				protocolModel.setHandleThreads(Integer.valueOf(tempElement.getAttribute("handleThreads")));
				protocolModel.setCharset(tempElement.getAttribute("charset"));
				protocolModel.setFrameLength(Integer.valueOf(tempElement.getAttribute("frameLength")));
				protocolModel.setHeartbeat(Integer.valueOf(tempElement.getAttribute("heartbeat")));
				protocolModel.setPacketSize(Integer.valueOf(tempElement.getAttribute("packetSize")));
				protocolModel.setCallBackTimeOut(Integer.valueOf(tempElement.getAttribute("callBackTimeOut")));
				protocolModel.setRefSerialize(tempElement.getAttribute("refSerialize"));
				protocolModel.setRefRmiBeanFactory(tempElement.getAttribute("refRmiBeanFactory"));
				protocolModel.setRefClientMessageHandleFactory(tempElement.getAttribute("refServerMessageHandleFactory"));
				protocolModel.setRefClientMessageHandleFactory(tempElement.getAttribute("refClientMessageHandleFactory"));

				try {
					String tempValue;
					if (null == (tempValue = tempElement.getAttribute("localName")) || tempValue.length() < 1) {
						protocolModel.setLocalName(InetAddress.getLocalHost().getHostName());
					}
					if (null == (tempValue = tempElement.getAttribute("localAddress")) || tempValue.length() < 1) {
						protocolModel.setLocalAddress(checkInterface().getHostAddress());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// registerBeanDefinition(parserContext, ProtocolModel.class, RMI_CONFIG_BEAN_PROTOCOL, property);
				rmiConfigGroup.setProtocolModel(protocolModel);

			} else if (RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getLocalName())) {
				tempElement = (Element) node;
				RegistryModel registryModel = new RegistryModel();
				registryModel.setServerAddress(tempElement.getAttribute("serverAddress"));
				registryModel.setMaxRetries(Integer.valueOf(tempElement.getAttribute("maxRetries")));

				// registerBeanDefinition(parserContext, RegistryModel.class, RMI_CONFIG_BEAN_REGISTRY, property);
				rmiConfigGroup.setRegistryModel(registryModel);
			} else if (RMI_CONFIG_ELEMENT_SCAN.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SCAN.equals(node.getLocalName())) {
				tempElement = (Element) node;
				ScanModel scanModel = new ScanModel().setPackagePath(tempElement.getAttribute("scan"));
				// registerBeanDefinition(parserContext, ScanModel.class, RMI_CONFIG_BEAN_SCAN, property);
				rmiConfigGroup.setScanModel(scanModel);
			}
		}
		return rmiConfigGroup;
	}

	public static class RmiConfigGroup {

		private RegistryModel registryModel;
		private ProtocolModel protocolModel;
		private ScanModel scanModel;
		private ServiceGroup serviceGroup;
		private ReferenceGroup referenceGroup;

		public RegistryModel getRegistryModel() {
			return registryModel;
		}
		public RmiConfigGroup setRegistryModel(RegistryModel registryModel) {
			this.registryModel = registryModel;
			return this;
		}
		public ProtocolModel getProtocolModel() {
			return protocolModel;
		}
		public RmiConfigGroup setProtocolModel(ProtocolModel protocolModel) {
			this.protocolModel = protocolModel;
			return this;
		}
		public ScanModel getScanModel() {
			return scanModel;
		}
		public RmiConfigGroup setScanModel(ScanModel scanModel) {
			this.scanModel = scanModel;
			return this;
		}
		public ServiceGroup getServiceGroup() {
			return serviceGroup;
		}
		public RmiConfigGroup setServiceGroup(ServiceGroup serviceGroup) {
			this.serviceGroup = serviceGroup;
			return this;
		}
		public ReferenceGroup getReferenceGroup() {
			return referenceGroup;
		}
		public RmiConfigGroup setReferenceGroup(ReferenceGroup referenceGroup) {
			this.referenceGroup = referenceGroup;
			return this;
		}

	}
}
