package com.cheuks.bin.original.rmi.config;

import java.util.Map;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.common.util.CollectionUtil;
import com.cheuks.bin.original.rmi.config.model.ProtocolModel;
import com.cheuks.bin.original.rmi.config.model.RegistryModel;

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
						"address",
						tempElement.getAttribute("address"),
						"port",
						Integer.valueOf(tempElement.getAttribute("port")),
						"netWorkThreads",
						Integer.valueOf(tempElement.getAttribute("netWorkThreads")),
						"handleThreads",
						Integer.valueOf(tempElement.getAttribute("handleThreads")),
						"charset",
						tempElement.getAttribute("charset"),
						"heartbeat",
						Integer.valueOf(tempElement.getAttribute("heartbeat")),
						"packetSize",
						Integer.valueOf(tempElement.getAttribute("packetSize")),
						"callBackTimeOut",
						Integer.valueOf(tempElement.getAttribute("callBackTimeOut")),
						"refSerialize",
						tempElement.getAttribute("refSerialize"),
						"refRmiBeanFactory",
						tempElement.getAttribute("refRmiBeanFactory"),
						"refServerMessageHandleFactory",
						tempElement.getAttribute("refServerMessageHandleFactory"),
						"refClientMessageHandleFactory",
						tempElement.getAttribute("refClientMessageHandleFactory"));

				registerBeanDefinition(parserContext, ProtocolModel.class, RMI_CONFIG_BEAN_PROTOCOL, property);
			} else if (RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REGISTRY.equals(node.getLocalName())) {
				tempElement = (Element) node;
				property = CollectionUtil.newInstance().toMap(
						"address",
						tempElement.getAttribute("address"),
						"maxRetries",
						Integer.valueOf(tempElement.getAttribute("maxRetries")));
				registerBeanDefinition(parserContext, RegistryModel.class, RMI_CONFIG_BEAN_REGISTRY, property);
			}
		}
	}

}
