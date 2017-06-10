package com.cheuks.bin.original.common.dbmanager;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable, Cloneable, LogicStatus {

	private static final long serialVersionUID = 1L;

	private Integer logicStatus = NORMAL;//数据逻辑状态
	private Date createDateTime;
	private Date lastUpdateDateTime = new Date();

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Integer getLogicStatus() {
		return logicStatus;
	}

	public BaseEntity setLogicStatus(Integer logicStatus) {
		this.logicStatus = logicStatus;
		return this;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public BaseEntity setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
		return this;
	}

	public Date getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}

	public BaseEntity setLastUpdateDateTime(Date lastUpdateDateTime) {
		this.lastUpdateDateTime = lastUpdateDateTime;
		return this;
	}

}
