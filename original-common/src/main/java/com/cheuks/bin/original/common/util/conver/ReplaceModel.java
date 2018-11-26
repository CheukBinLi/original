package com.cheuks.bin.original.common.util.conver;

public class ReplaceModel {

	private String oldValue;
	private String newValue;

	public String replace(String content) {
		if (null == content)
			return content;
		return content.replace(oldValue, newValue);
	}

	public String getOldValue() {
		return oldValue;
	}

	public ReplaceModel setOldValue(String oldValue) {
		this.oldValue = oldValue;
		return this;
	}

	public String getNewValue() {
		return newValue;
	}

	public ReplaceModel setNewValue(String newValue) {
		this.newValue = newValue;
		return this;
	}

	public ReplaceModel(String oldValue, String newValue) {
		super();
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ReplaceModel() {
		super();
	}

}
