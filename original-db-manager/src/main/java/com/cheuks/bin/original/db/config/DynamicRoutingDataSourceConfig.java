package com.cheuks.bin.original.db.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.db.config.model.DbConfigModel;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel.DataSourceModel;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel.FilterModel;

public class DynamicRoutingDataSourceConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		BeanDefinition dbConfigModel = getDbConfigModel(parserContext);
		dbConfigModel.getPropertyValues().add(DbConfigModel.DYNAMIC_ROUTING_DATA_SOURCE_MODEL, doParse(element, parserContext));
		return this;
	}

	DynamicRoutingDataSourceModel doParse(Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		NodeList subList;
		Node node;
		Node tempNode;
		DynamicRoutingDataSourceModel dynamicRoutingDataSourceModel = new DynamicRoutingDataSourceModel();

		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (DynamicRoutingDataSourceModel.HANDLER.equals(node.getNodeName()) || DynamicRoutingDataSourceModel.HANDLER.equals(node.getLocalName())) {
				if (!node.hasChildNodes())
					break;
				subList = node.getChildNodes();
				for (int j = 0, subLen = subList.getLength(); j < subLen; j++) {
					if (!node.hasChildNodes())
						break;
					tempNode = subList.item(j);
					if (DynamicRoutingDataSourceModel.FILTER.equals(tempNode.getNodeName()) || DynamicRoutingDataSourceModel.FILTER.equals(tempNode.getLocalName())) {
						dynamicRoutingDataSourceModel.setHandler(new FilterModel(((Element) node).getAttribute(ContentType.FilterModelConfigType.ID), ((Element) node).getAttribute(ContentType.FilterModelConfigType.EXPRESSION)));
					}
				}
			} else if (DynamicRoutingDataSourceModel.DATA_SOURCES.equals(node.getNodeName()) || DynamicRoutingDataSourceModel.DATA_SOURCES.equals(node.getLocalName())) {
				if (!node.hasChildNodes())
					break;
				subList = node.getChildNodes();
				List<DataSourceModel> datasources = new ArrayList<DynamicRoutingDataSourceModel.DataSourceModel>();
				for (int j = 0, subLen = subList.getLength(); j < subLen; j++) {
					if (!node.hasChildNodes())
						break;
					tempNode = subList.item(j);
					if (ContentType.DataSourceConfigType.DATA_SOURCE.equals(tempNode.getNodeName()) || ContentType.DataSourceConfigType.DATA_SOURCE.equals(tempNode.getLocalName())) {
						datasources.add(new DataSourceModel(StringUtil.generateRegexString(((Element) node).getAttribute(ContentType.DataSourceConfigType.PATTERN)), ((Element) node).getAttribute(ContentType.DataSourceConfigType.DATA_SOURCE)));
					}
				}
				dynamicRoutingDataSourceModel.setDatasources(datasources);
			}
		}
		element.getAttribute(ContentType.TransactionManagerConfigType.ID);
		element.getAttribute(ContentType.TransactionManagerConfigType.REF);
		element.getAttribute(ContentType.TransactionManagerConfigType.CLAZZ);
		return dynamicRoutingDataSourceModel;
	}
}
