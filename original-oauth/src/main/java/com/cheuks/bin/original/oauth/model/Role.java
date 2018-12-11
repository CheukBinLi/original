package com.cheuks.bin.original.oauth.model;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = 6934763213719225849L;

	private long id;
	private String name;
	private String remark;
	private Collection<Granted> granteds;

	public Role setId(long id) {
		this.id = id;
		return this;
	}

	public Role setName(String name) {
		this.name = name;
		return this;
	}

	public Role setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Role setGranteds(Collection<Granted> granteds) {
		this.granteds = granteds;
		return this;
	}

}
