package com.cheuks.bin.spring.boot.data.jpa.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cheuks.bin.spring.boot.data.jpa.model.DemoModel;

public interface DemoService<T extends Serializable> {

	Long add(T t) throws RuntimeException;

	List<T> getList();

	Page<DemoModel> getPage(int page, int size);
}
