package com.cheuks.bin.original.rmi.config;

import java.net.InetAddress;
import java.util.Map;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.rmi.config.model.ProtocolModel;
import com.cheuks.bin.original.rmi.config.model.RegistryModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;

public class RmiConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element, parserContext);
		return this;
	}

	private void doParser(Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		Node node;
		Element tempElement;
		Map<String, Object> property;
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_PROTOCOL.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_PROTOCOL.equals(node.getLocalName())) {
				tempElement = (Element) node;

				property = CollectionUtil.newInstance().toMap(
						"localName", tempElement.getAttribute("localName"), 
						"localAddress", tempElement.getAttribute("localAddress"),
						"port", Integer.valueOf(tempElement.getAttribute("port")), 
						"netWorkThreads", Integer.valueOf(tempElement.getAttribute("netWorkThreads")), 
						"handleThreads",Integer.valueOf(tempElement.getAttribute("handleThreads")),
						"charset", tempElement.getAttribute("charset"),
						"frameLength", tempElement.getAttribute("frameLength"),
						"heartbeat", Integer.valueOf(tempElement.getAttribute("heartbeat")),
						"packetSize",Integer.valueOf(tempElement.getAttribute("packetSize")),
						"callBackTimeOut", Integer.valueOf(tempElement.getAttribute("callBackTimeOut")),
						"refSerialize", tempElement.getAttribute("refSerialize"), 
						"refRmiBeanFactory",tempElement.getAttribute("refRmiBeanFactory"),
						"refServerMessageHandleFactory", tempElement.getAttribute("refServerMessageHandleFactory"), 
						"refClientMessageHandleFactory", tempElement.getAttribute("refClientMessageHandleFactory"));

				try {
					String tempValue;
					if (null == (tempValue = tempElement.getAttribute("localName")) || tempValue.length() < 1) {
						property.put("localName", InetAddress.getLocalHost().getHostName());
					}
					if (null == (tempValue = tempElement.getAttribute("localAddress")) || tempValue.length() < 1) {
						property.put("localAddress", checkInterface().getHostAddress());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				registerBeanDefinition(parserContext, ProtocolModel.class, RMI_CONFIG_BEAN_PROTOCOL, property);
			} else if (RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getLocalName())) {
				tempElement = (Element) node;
				property = CollectionUtil.newInstance().toMap(
						"serverAddress", tempElement.getAttribute("serverAddress"), 
						"maxRetries",Integer.valueOf(tempElement.getAttribute("maxRetries")));
				
				registerBeanDefinition(parserContext, RegistryModel.class, RMI_CONFIG_BEAN_REGISTRY, property);
			} else if (RMI_CONFIG_ELEMENT_SCAN.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SCAN.equals(node.getLocalName())) {
				tempElement = (Element) node;
				property = CollectionUtil.newInstance().toMap("packagePath", tempElement.getAttribute("scan"));
				registerBeanDefinition(parserContext, ScanModel.class, RMI_CONFIG_BEAN_SCAN, property);
			}
		}
	}

}
