package com.cheuks.bin.original.db.manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/***
 * *
 * 
 * Copyright 2016 CHEUK.BIN.LI Individual All
 * 
 * ALL RIGHT RESERVED
 * 
 * CREATE ON 2016年4月28日
 * 
 * EMAIL:20796698@QQ.COM
 * 
 * 
 * @author CHEUK.BIN.LI
 * 
 * @see 注意：必须注入:
 *      <p>
 *      SessionFactory sessionFactory
 *      <p>
 *      QueryFactory queryFactory;
 *
 */
public class HibernateDBAdapter extends AbstractHibernateDBAdapter {

	private SessionFactory sessionFactory;

	public HibernateDBAdapter setSessionFactory(String name) {
		return this;
	}

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public HibernateDBAdapter setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		return this;
	}

}
