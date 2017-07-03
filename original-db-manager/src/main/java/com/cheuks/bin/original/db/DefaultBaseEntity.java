package com.cheuks.bin.original.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.cheuks.bin.original.common.dbmanager.BaseEntity;

@MappedSuperclass
public class DefaultBaseEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	protected Integer logicStatus = NORMAL;//数据逻辑状态
	@Column(updatable = false)
	protected Date createDateTime = new Date();
	protected Date lastUpdateDateTime = new Date();

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
