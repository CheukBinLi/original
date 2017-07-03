package com.cheuks.bin.original.common.dbmanager;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable, Cloneable, LogicStatus {
	private static final long serialVersionUID = 7903406421946677583L;

	public abstract Integer getLogicStatus();

	public abstract BaseEntity setLogicStatus(Integer logicStatus);

	public abstract Date getCreateDateTime();

	public abstract BaseEntity setCreateDateTime(Date createDateTime);

	public abstract Date getLastUpdateDateTime();

	public abstract BaseEntity setLastUpdateDateTime(Date lastUpdateDateTime);

}
