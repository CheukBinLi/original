package com.cheuks.bin.original.common.dbmanager;

import java.io.Serializable;

public class BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
