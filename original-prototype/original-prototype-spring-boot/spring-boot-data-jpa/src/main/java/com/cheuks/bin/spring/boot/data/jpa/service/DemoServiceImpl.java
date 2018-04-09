package com.cheuks.bin.spring.boot.data.jpa.service;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cheuks.bin.spring.boot.data.jpa.dao.DemoDao;
import com.cheuks.bin.spring.boot.data.jpa.model.DemoModel;

@Service
public class DemoServiceImpl implements DemoService<DemoModel> {

	@Autowired
	DemoDao demoDao;

	@Autowired
	EntityManager entityManager;

	@Override
	@Transactional
	public Long add(DemoModel t) throws RuntimeException {
		return demoDao.save(t).getId();
	}

	@SuppressWarnings("unchecked")
	public List<DemoModel> getList() {
		return (List<DemoModel>) entityManager.createQuery("from demo").getResultList();
	}

	@SuppressWarnings("unchecked")
	public Page<DemoModel> getPage(int page, int size) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size);
		if (0 == new Random().nextInt(10) % 2) {
			Query query = entityManager.createQuery("from demo");
			// (page-1)*size
			query.setFirstResult(Integer.valueOf(Long.toString(pageable.getOffset())));
			int pageNumber;
			page = page == 0 ? 1 : page;
			// page*size
			query.setMaxResults(0 == (pageNumber = pageable.getPageNumber()) ? page * size : pageNumber);
			System.err.println("使用:entityManager   getOffset:" + Integer.valueOf(Long.toString(pageable.getOffset()))
					+ "  getPageNumber:" + query.getMaxResults());
			Page<DemoModel> result = new PageImpl<DemoModel>(query.getResultList());
			return result;
		}
		System.err.println("使用:demoDao");
		return demoDao.findAll(pageable);
	}

}
