package com.cheuks.bin.original.db.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheuks.bin.original.common.util.conver.StringUtil;
import com.cheuks.bin.original.db.config.ContentType.TransactionManagerConfigType;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel.PatternModel;
import com.cheuks.bin.original.db.config.model.TransactionManagerModel.SessionFactoryModel;

public class TransactionManagerConfig extends AbstractConfig {

	private static final long serialVersionUID = 1L;

	private StringUtil stringUtil = StringUtil;

	@Override
	public AbstractConfig makeConfig(Element element, ParserContext parserContext) {
		BeanDefinition dbConfigModel = getDbConfigModel(parserContext);
		TransactionManagerModel transactionManagerModel;
		dbConfigModel.getPropertyValues().add(TransactionManagerConfigType.TRANSACTION_MANAGER, transactionManagerModel = doParse(element, parserContext));
		// 初始化
		BeanDefinition transactionManager;
		transactionManager = stringUtil.isEmpty(transactionManagerModel.getRef()) ? new RootBeanDefinition() : getConfig(parserContext, transactionManagerModel.getRef());
		BeanDefinition sessionFactory;
		sessionFactory = stringUtil.isEmpty(transactionManagerModel.getSessionFactory().getRef()) ? new RootBeanDefinition(transactionManagerModel.getSessionFactory().getClazz()) : getConfig(parserContext, transactionManagerModel.getSessionFactory().getRef());

		// doParse
		// 生成所有对象
		return this;
	}

	TransactionManagerModel doParse(Element element, ParserContext parserContext) {
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
}
