package com.cheuks.bin.original.rmi.config;

import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.rmi.RmiContant;
import com.cheuks.bin.original.rmi.GenerateRmiBeanFactory;
import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroupModel;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroupModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;

public class AnnotationDrivenConfig extends AbstractConfig implements RmiContant {

	private static final long serialVersionUID = 1L;

	private final ServiceGroupConfig serviceGroupConfig = new ServiceGroupConfig();

	private final ReferenceGroupConfig referenceGroupConfig = new ReferenceGroupConfig();

	private final GenerateRmiBeanFactory generateRmiBeanFactory = GenerateRmiBeanFactory.instance();

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		doParser(element, parserContext);
		return this;
	}

	private void doParser(Element element, ParserContext parserContext) {

		if (!parserContext.getRegistry().containsBeanDefinition(RMI_CONFIG_BEAN_CONFIG_GROUP)) {
			throw new NullPointerException("The configuration sequence must be <rmi:config> in first.");
		}
		NodeList list = element.getChildNodes();
		ServiceGroupModel serviceGroupModel;
		ReferenceGroupModel referenceGroupModel;
		ScanModel scanModel;
		Node node;
		Element tempElement;
		String applicationName;

		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (RMI_CONFIG_ELEMENT_SERVICE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_SERVICE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				applicationName = tempElement.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME);
				scanModel = new ScanModel();
				scanModel.setServiceName(applicationName);
				scanModel.setPackagePath(tempElement.getAttribute("packagePath"));
				scanModel.setVersion(tempElement.getAttribute("version"));
				scanModel.setMultiInstance(Boolean.valueOf(tempElement.getAttribute("multiInstance")));
				try {
					serviceGroupModel = generateRmiBeanFactory.scanByServiceGroupHandle(scanModel);
					serviceGroupConfig.getServiceGroup(parserContext, applicationName).getServices().putAll(serviceGroupModel.getServices());
					serviceGroupConfig.doGenerate(parserContext, serviceGroupModel, applicationName);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}

			} else if (RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getNodeName()) || RMI_CONFIG_ELEMENT_REFERENCE.equals(node.getLocalName())) {
				tempElement = (Element) node;
				applicationName = tempElement.getAttribute(RMI_CONFIG_ATTRIBUTE_APPLICATION_NAME);
				scanModel = new ScanModel();
				scanModel.setServiceName(applicationName);
				scanModel.setPackagePath(tempElement.getAttribute("packagePath"));
				scanModel.setVersion(tempElement.getAttribute("version"));
				scanModel.setMultiInstance(Boolean.valueOf(tempElement.getAttribute("multiInstance")));
				try {
					referenceGroupModel = generateRmiBeanFactory.scanByReferenceGroupHandle(scanModel);
					referenceGroupConfig.getServiceGroupModel(parserContext, applicationName).getReferenceGroup().putAll(referenceGroupModel.getReferenceGroup());
					// doGenerate(parserContext, referenceGroupModel, applicationName);
					referenceGroupConfig.doGenerate(parserContext, referenceGroupModel, applicationName);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}

		}

	}

}
