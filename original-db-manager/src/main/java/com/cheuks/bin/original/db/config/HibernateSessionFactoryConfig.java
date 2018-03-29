package com.cheuks.bin.original.db.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.db.config.ContentType.HibernateSessionFactoryConfigType;
import com.cheuks.bin.original.db.config.model.DbConfigModel;
import com.cheuks.bin.original.db.config.model.HibernateSessionFactoryModel;

public class HibernateSessionFactoryConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		BeanDefinition dbConfigModel = getDbConfigModel(parserContext);
		dbConfigModel.getPropertyValues().add(DbConfigModel.HIBERNATE_SESSION_FACTORY_MODEL, doParse(element, parserContext));
		return this;
	}

	HibernateSessionFactoryModel doParse(Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		NodeList subList;
		Node node;
		Element tempElement;
		Node tempNode;
		NodeList tempList;
		HibernateSessionFactoryModel hibernateSessionFactoryModel = new HibernateSessionFactoryModel();
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (HibernateSessionFactoryConfigType.RESOURCE.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.RESOURCE.equals(node.getLocalName())) {
				if (!node.hasChildNodes())
					break;
				subList = node.getChildNodes();
				for (int j = 0, subLen = subList.getLength(); j < subLen; j++) {
					if (!node.hasChildNodes())
						break;
					tempNode = subList.item(j);
					/** entitys节点 */
					if (HibernateSessionFactoryConfigType.ENTITYS.equals(tempNode.getNodeName()) || HibernateSessionFactoryConfigType.ENTITYS.equals(tempNode.getLocalName())) {
						tempList = tempNode.getChildNodes();
						Set<String> entitys = new HashSet<String>();
						for (int k = 0, lenJ = tempList.getLength(); k < lenJ; k++) {
							tempElement = (Element) tempList.item(k);
							if (HibernateSessionFactoryConfigType.ENTITY_TO_SCAN.equals(tempNode.getNodeName()) || HibernateSessionFactoryConfigType.ENTITY_TO_SCAN.equals(tempNode.getLocalName())) {
								hibernateSessionFactoryModel.setEntityToScan(tempElement.getAttribute(HibernateSessionFactoryConfigType.PACKAGES_TO_SCAN));
							}
							/** entityToScan节点 */
							else if (HibernateSessionFactoryConfigType.ENTITY.equals(tempNode.getNodeName()) || HibernateSessionFactoryConfigType.ENTITY.equals(tempNode.getLocalName())) {
								entitys.add(tempElement.getAttribute(HibernateSessionFactoryConfigType.RESOURCE));
							}
						}
						hibernateSessionFactoryModel.setEntitys(entitys);
					} else if (HibernateSessionFactoryConfigType.MAPPERS.equals(tempNode.getNodeName()) || HibernateSessionFactoryConfigType.MAPPERS.equals(tempNode.getLocalName())) {
						tempNode = subList.item(j);
						tempList = tempNode.getChildNodes();
						Set<String> mapper = new HashSet<String>();
						for (int k = 0, lenJ = tempList.getLength(); k < lenJ; k++) {
							tempElement = (Element) tempList.item(k);
							if (HibernateSessionFactoryConfigType.ENTITY_TO_SCAN.equals(tempNode.getNodeName()) || HibernateSessionFactoryConfigType.ENTITY_TO_SCAN.equals(tempNode.getLocalName()))
								hibernateSessionFactoryModel.setEntityToScan(tempElement.getAttribute(HibernateSessionFactoryConfigType.PACKAGES_TO_SCAN));
							else
								mapper.add(tempElement.getAttribute(HibernateSessionFactoryConfigType.RESOURCE));
						}
						hibernateSessionFactoryModel.setMappers(mapper);
					}
				}
			} else {
				if (HibernateSessionFactoryConfigType.AUTO_RECONNECT.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.AUTO_RECONNECT.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setAutoReconnect(Boolean.valueOf(((Element) node).getAttribute(HibernateSessionFactoryConfigType.AUTO_RECONNECT)));
				} else if (HibernateSessionFactoryConfigType.CACHE_CONFIGURATION_FILE_RESOURCE_PATH.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.CACHE_CONFIGURATION_FILE_RESOURCE_PATH.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setCacheConfigurationFileResourcePath(((Element) node).getAttribute(HibernateSessionFactoryConfigType.CACHE_CONFIGURATION_FILE_RESOURCE_PATH));
				} else if (HibernateSessionFactoryConfigType.CACHE_REGION_FACTORY_CLASS.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.CACHE_REGION_FACTORY_CLASS.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setCacheRegionFactoryClass(HibernateSessionFactoryConfigType.CACHE_REGION_FACTORY_CLASS);
				} else if (HibernateSessionFactoryConfigType.DIALECT.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.DIALECT.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setDialect(HibernateSessionFactoryConfigType.DIALECT);
				} else if (HibernateSessionFactoryConfigType.FORMAT_SQL.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.FORMAT_SQL.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setFormatSql(Boolean.valueOf(((Element) node).getAttribute(HibernateSessionFactoryConfigType.FORMAT_SQL)));
				} else if (HibernateSessionFactoryConfigType.HDM2DDL.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.HDM2DDL.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setHbm2ddl(Boolean.valueOf(((Element) node).getAttribute(HibernateSessionFactoryConfigType.HDM2DDL)));
				} else if (HibernateSessionFactoryConfigType.SHOW_SQL.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.SHOW_SQL.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setShowSql(Boolean.valueOf(((Element) node).getAttribute(HibernateSessionFactoryConfigType.SHOW_SQL)));
				} else if (HibernateSessionFactoryConfigType.USE_SECOND_CACHE.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.USE_SECOND_CACHE.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setUseSecondCache(Boolean.valueOf(((Element) node).getAttribute(HibernateSessionFactoryConfigType.USE_SECOND_CACHE)));
				} else if (HibernateSessionFactoryConfigType.VALIDATION_MODE.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.VALIDATION_MODE.equals(node.getLocalName())) {
					hibernateSessionFactoryModel.setValidationMode(((Element) node).getAttribute(HibernateSessionFactoryConfigType.VALIDATION_MODE));
				}
			}
		}
		return hibernateSessionFactoryModel;
	}

}
