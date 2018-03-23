package com.cheuks.bin.original.db.config.spring.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.criteria.Root;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.dbmanager.QueryFactory;
import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.common.util.scan.ScanFile;
import com.cheuks.bin.original.common.util.scan.ScanSimple;
import com.cheuks.bin.original.db.config.ContentType;
import com.cheuks.bin.original.db.config.ContentType.DynamicRoutingDataSourceConfigType;
import com.cheuks.bin.original.db.config.ContentType.HibernateSessionFactoryConfigType;
import com.cheuks.bin.original.db.config.ContentType.TransactionManagerConfigType;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel.DataSourceModel;
import com.cheuks.bin.original.db.config.model.DynamicRoutingDataSourceModel.FilterModel;
import com.cheuks.bin.original.db.config.model.HibernateSessionFactoryModel;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel.PatternModel;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel.SessionFactoryModel;
import com.cheuks.bin.original.db.manager.hibernate.BeetlQueryFactory;

public class DbBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private StringUtil stringUtil = StringUtil.newInstance();

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		// if (txAttributes.size() == 1) {
		// // Using attributes source.
		// Element attributeSourceElement = txAttributes.get(0);
		// RootBeanDefinition attributeSourceDefinition = parseAttributeSource(attributeSourceElement, parserContext);
		// builder.addPropertyValue("transactionAttributeSource", attributeSourceDefinition);
		// }
		// else {
		// // Assume annotations source.
		// builder.addPropertyValue("transactionAttributeSource",
		// new RootBeanDefinition("org.springframework.transaction.annotation.AnnotationTransactionAttributeSource"));
		// }

		NodeList list = element.getChildNodes();
		Node node;
		DynamicRoutingDataSourceModel dynamicRoutingDataSourceModel;
		HibernateSessionFactoryModel hibernateSessionFactoryModel;
		TransactionManagerModel transactionManagerModel = null;

		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (TransactionManagerConfigType.TRANSACTION_MANAGER.equals(node.getNodeName()) || TransactionManagerConfigType.TRANSACTION_MANAGER.equals(node.getLocalName())) {
				transactionManagerModel = transactionManagerModelDoParse(element, parserContext);
			} else if (HibernateSessionFactoryConfigType.HIBERNATE_SESSION_FACTORY.equals(node.getNodeName()) || HibernateSessionFactoryConfigType.HIBERNATE_SESSION_FACTORY.equals(node.getLocalName())) {
				hibernateSessionFactoryModel = hibernateSessionFactoryModelDoParse(element, parserContext);
			} else if (DynamicRoutingDataSourceConfigType.DYANMIC_ROUTING_DATA_SOURCE.equals(node.getNodeName()) || DynamicRoutingDataSourceConfigType.DYANMIC_ROUTING_DATA_SOURCE.equals(node.getLocalName())) {
				dynamicRoutingDataSourceModel = dynamicRoutingDataSourceModelDoParse(element, parserContext);
			}
		}
		if (null == transactionManagerModel)
			return;

		// 初始化所有sessionFactory

		// 动态路由

		// 初始化transactionManager
		BeanDefinition transactionManager;
		transactionManager = stringUtil.isEmpty(transactionManagerModel.getRef()) ? new RootBeanDefinition() : parserContext.getRegistry().getBeanDefinition(transactionManagerModel.getRef());
		BeanDefinition sessionFactory;
		sessionFactory = stringUtil.isEmpty(transactionManagerModel.getSessionFactory().getRef()) ? new RootBeanDefinition(transactionManagerModel.getSessionFactory().getClazz()) : parserContext.getRegistry().getBeanDefinition(transactionManagerModel.getSessionFactory().getRef());
		// doParse
		// 生成所有对象

	}

	DynamicRoutingDataSourceModel dynamicRoutingDataSourceModelDoParse(Element element, ParserContext parserContext) {
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
						datasources.add(new DataSourceModel(StringUtil.newInstance().generateRegexString(((Element) node).getAttribute(ContentType.DataSourceConfigType.PATTERN)), ((Element) node).getAttribute(ContentType.DataSourceConfigType.DATA_SOURCE)));
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

	HibernateSessionFactoryModel hibernateSessionFactoryModelDoParse(Element element, ParserContext parserContext) {
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

	TransactionManagerModel transactionManagerModelDoParse(Element element, ParserContext parserContext) {
		NodeList list = element.getChildNodes();
		Node node;
		TransactionManagerModel transactionManagerModel = new TransactionManagerModel();
		List<PatternModel> patterns = new ArrayList<PatternModel>();
		for (int i = 0, len = list.getLength(); i < len; i++) {
			node = list.item(i);
			if (TransactionManagerConfigType.PATTERNS.equals(node.getNodeName()) || TransactionManagerConfigType.PATTERNS.equals(node.getLocalName())) {
				patterns.add(new PatternModel(((Element) node).getAttribute(TransactionManagerConfigType.PATTERN), ((Element) node).getAttribute(TransactionManagerConfigType.ISOLATION), ((Element) node).getAttribute(TransactionManagerConfigType.NO_ROLLBACK_FOR),
						Long.valueOf(((Element) node).getAttribute(TransactionManagerConfigType.TIMEOUT)), ((Element) node).getAttribute(TransactionManagerConfigType.ROLLBACK_FOR), ((Element) node).getAttribute(TransactionManagerConfigType.READ_ONLY),
						((Element) node).getAttribute(TransactionManagerConfigType.PROPAGATION)));
			} else if (TransactionManagerConfigType.SESSION_FACTORY.equals(node.getNodeName()) || TransactionManagerConfigType.SESSION_FACTORY.equals(node.getLocalName())) {
				transactionManagerModel.setSessionFactory(new SessionFactoryModel(((Element) node).getAttribute(TransactionManagerConfigType.ID), ((Element) node).getAttribute(TransactionManagerConfigType.REF), ((Element) node).getAttribute(TransactionManagerConfigType.CLAZZ)));
			}
		}
		transactionManagerModel.setPatterns(patterns);
		transactionManagerModel.setId(element.getAttribute(TransactionManagerConfigType.ID));
		transactionManagerModel.setRef(element.getAttribute(TransactionManagerConfigType.REF));
		transactionManagerModel.setClazz(element.getAttribute(TransactionManagerConfigType.CLAZZ));
		return transactionManagerModel;
	}

	boolean initHibernateSessionFactory(ParserContext parserContext, HibernateSessionFactoryModel hibernateSessionFactoryModel) {

		BeanDefinition hibernateSessionFactory = stringUtil.isEmpty(hibernateSessionFactoryModel.getRef()) ? new RootBeanDefinition(hibernateSessionFactoryModel.getClazz()) : parserContext.getRegistry().getBeanDefinition(hibernateSessionFactoryModel.getRef());
		if (null == hibernateSessionFactory)
			return false;
		Properties hibernateProperties = new Properties();
		String tempValue;
		if (!stringUtil.isEmpty(tempValue = hibernateSessionFactoryModel.getDialect()))
			hibernateProperties.put(HibernateSessionFactoryConfigType.DIALECT, tempValue);
		if (!stringUtil.isEmpty(tempValue = hibernateSessionFactoryModel.getCacheConfigurationFileResourcePath()))
			hibernateProperties.put(HibernateSessionFactoryConfigType.CACHE_CONFIGURATION_FILE_RESOURCE_PATH, tempValue);
		if (!stringUtil.isEmpty(tempValue = hibernateSessionFactoryModel.getCacheRegionFactoryClass()))
			hibernateProperties.put(HibernateSessionFactoryConfigType.CACHE_REGION_FACTORY_CLASS, tempValue);
		if (!stringUtil.isEmpty(tempValue = hibernateSessionFactoryModel.getValidationMode()))
			hibernateProperties.put(HibernateSessionFactoryConfigType.VALIDATION_MODE, tempValue);
		hibernateProperties.put(HibernateSessionFactoryConfigType.USE_SECOND_CACHE, hibernateSessionFactoryModel.getUseSecondCache());
		hibernateProperties.put(HibernateSessionFactoryConfigType.AUTO_RECONNECT, hibernateSessionFactoryModel.getAutoReconnect());
		hibernateProperties.put(HibernateSessionFactoryConfigType.SHOW_SQL, hibernateSessionFactoryModel.getShowSql());
		hibernateProperties.put(HibernateSessionFactoryConfigType.FORMAT_SQL, hibernateSessionFactoryModel.getFormatSql());
		hibernateProperties.put(HibernateSessionFactoryConfigType.HDM2DDL, hibernateSessionFactoryModel.getHbm2ddl());

		// com.cheuks.bin.original.db.manager.hibernate.BeetlQueryFactory
		BeanDefinition queryFactory;
		if (!stringUtil.isEmpty(hibernateSessionFactoryModel.getRef())) {
			queryFactory = parserContext.getRegistry().getBeanDefinition(hibernateSessionFactoryModel.getRef());
		} else if (!stringUtil.isEmpty(hibernateSessionFactoryModel.getRef())) {
			queryFactory = new RootBeanDefinition(hibernateSessionFactoryModel.getClazz());
		} else {
			queryFactory = new RootBeanDefinition(BeetlQueryFactory.class);
			((RootBeanDefinition) queryFactory).setInitMethodName("scan");
		}
		queryFactory.getPropertyValues().add("index", hibernateSessionFactoryModel.getMapperToScan());
		// com.cheuks.bin.original.common.util.scan.ScanFile
		BeanDefinition scan = parserContext.getRegistry().getBeanDefinition("scan");
		queryFactory.getPropertyValues().add("scan", null == scan ? new RootBeanDefinition(ScanSimple.class) : scan);

		String[] packagesToScan = hibernateSessionFactoryModel.getEntityToScan().split(",");

		return true;
	}

}
