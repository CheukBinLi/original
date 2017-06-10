package com.cheuks.bin.original.common.dbmanager;

import java.io.Serializable;
import java.util.List;

public class BasePage<entity> implements Serializable {

	private static final long serialVersionUID = 5487996691466803063L;

	private List<entity> list;
	private int pageNumber;
	private int pageSize;
	private int total;
	private int totalPage;

	public List<entity> getList() {
		return list;
	}

	public BasePage<entity> setList(List<entity> list) {
		this.list = list;
		return this;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public BasePage<entity> setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public BasePage<entity> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getTotal() {
		return total;
	}

	public BasePage<entity> setTotal(int total) {
		this.total = total;
		return this;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public BasePage<entity> setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		return this;
	}

	public BasePage(List<entity> list, int pageNumber, int pageSize, int total, int totalPage) {
		super();
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPage = totalPage;
	}

	public BasePage() {
		super();
	}

}
