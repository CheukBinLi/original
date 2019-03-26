package com.cheuks.bin.original.common.util.web;

import java.io.Serializable;
import java.util.List;

public class FileInfo implements Serializable {

	private static final long serialVersionUID = -4198889908051832443L;

	private String md5;
	private String name;
	private String type;
	private byte[] data;

	public boolean isHasFile() {
		return this.data != null && this.data.length > 0;
	}

	public static boolean isHasFile(List<FileInfo> fs) {
		boolean hasFile = false;
		if (fs != null) {
			for (FileInfo file : fs) {
				if (file != null && file.isHasFile()) {
					hasFile = true;
					break;
				}
			}
		}
		return hasFile;
	}

	public FileInfo setMd5(String md5) {
		this.md5 = md5;
		return this;
	}

	public FileInfo setName(String name) {
		this.name = name;
		return this;
	}

	public FileInfo setType(String type) {
		this.type = type;
		return this;
	}

	public FileInfo setData(byte[] data) {
		this.data = data;
		return this;
	}

	public String getMd5() {
		return md5;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}

	public String generateMd5FileName() {
		return this.md5 + "." + type;
	}

	public FileInfo(String md5, String name, String type, byte[] data) {
		super();
		this.md5 = md5;
		this.name = name;
		this.type = type;
		this.data = data;
	}

}
