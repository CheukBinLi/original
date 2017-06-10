package com.cheuks.bin.original.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.dbmanager.dao.BaseDao;
import com.cheuks.bin.original.common.dbmanager.service.AbstractService;
import com.cheuks.bin.original.web.dao.UserDao;
import com.cheuks.bin.original.web.entity.User;
import com.cheuks.bin.original.web.service.UserService;

@Component
public class UserServiceImpl extends AbstractService<User, String> implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<User, String> getDao() {
		return userDao;
	}

}
