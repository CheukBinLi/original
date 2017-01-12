package com.cheuks.bin.original.search;

import java.util.List;

import com.cheuks.bin.original.annotation.IndexField;

public class EsModel {

	private int id;
	private String name;
	@IndexField(analyzer = "ik_smart")
	private String searchWord;
	private double count;

	private List<MMX> wahaha;

	public int getId() {
		return id;
	}

	public EsModel setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public EsModel setName(String name) {
		this.name = name;
		return this;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public EsModel setSearchWord(String searchWord) {
		this.searchWord = searchWord;
		return this;
	}

	public List<MMX> getWahaha() {
		return wahaha;
	}

	public EsModel setWahaha(List<MMX> wahaha) {
		this.wahaha = wahaha;
		return this;
	}

	public double getCount() {
		return count;
	}

	public EsModel setCount(double count) {
		this.count = count;
		return this;
	}

}
