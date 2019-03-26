package com.cheuks.bin.original.oauth.security.token.handle;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Locker implements Serializable {

	private static final long serialVersionUID = 7063497438988248538L;

	private String version;// 版本号
	private Object data;// 数据集

	@SuppressWarnings("unchecked")
	public <T> T getConverData() {
		return (T) this.data;
	}

	public Locker setVersion(String version) {
		this.version = version;
		return this;
	}

	public Locker setData(Object data) {
		this.data = data;
		return this;
	}

}
