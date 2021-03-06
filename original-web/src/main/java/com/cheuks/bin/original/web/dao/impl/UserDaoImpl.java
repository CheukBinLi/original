package com.cheuks.bin.original.web.dao.impl;

import com.cheuks.bin.original.web.entity.User;
import com.cheuks.bin.original.web.dao.UserDao;
import com.cheuks.bin.original.web.dao.impl.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.DBAdapter;
import com.cheuks.bin.original.common.dbmanager.dao.AbstractDao;

@Component
public class UserDaoImpl extends AbstractDao<User, String> implements UserDao {

	@Autowired
	private DBAdapter<?> dBAdapter;

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public DBAdapter<?> getDBAdapter() {
		return dBAdapter;
	}

}
