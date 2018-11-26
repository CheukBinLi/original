package com.cheuks.bin.original.common.util.conver;

import java.util.List;

public class ReplaceProvider {

	private String field;
	private List<ReplaceModel> replaces;
	private String replaceNull;//若被替被的主体为null时，整体内容可以直接替换为此值

	public String replace(String content) {
		if (null == content) {
			return null == replaceNull ? content : replaceNull;
		}
		if (CollectionUtil.newInstance().isEmpty(replaces)) {
			return content;
		}
		for (ReplaceModel item : replaces) {
			content = item.replace(content);
		}
		return content;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<ReplaceModel> getReplaces() {
		return replaces;
	}

	public void setReplaces(List<ReplaceModel> replaces) {
		this.replaces = replaces;
	}

	public String getReplaceNull() {
		return replaceNull;
	}

	public void setReplaceNull(String replaceNull) {
		this.replaceNull = replaceNull;
	}

}
