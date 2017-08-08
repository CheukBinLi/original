package com.cheuks.bin.original.common.web.common.model;

import java.io.Serializable;

public class UploadFileModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6690196203348431635L;

	private String name;
	private Long size;
	private String path;

	public UploadFileModel() {
		super();
	}

	public UploadFileModel(String name, Long size, String path) {
		super();
		this.name = name;
		this.size = size;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public UploadFileModel setName(String name) {
		this.name = name;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public UploadFileModel setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getPath() {
		return path;
	}

	public UploadFileModel setPath(String path) {
		this.path = path;
		return this;
	}

}
