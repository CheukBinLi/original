package com.cheuks.bin.original.rmi.config;

import java.io.Serializable;

import com.cheuks.bin.original.rmi.config.ReferenceGroupConfig.ReferenceGroup;
import com.cheuks.bin.original.rmi.config.ServiceGroupConfig.ServiceGroup;
import com.cheuks.bin.original.rmi.config.model.ProtocolModel;
import com.cheuks.bin.original.rmi.config.model.RegistryModel;
import com.cheuks.bin.original.rmi.config.model.ScanModel;

public class RmiConfigArg implements Serializable {

	private static final long serialVersionUID = -7238887106582076201L;

	private RegistryModel registryModel;
	private ProtocolModel protocolModel;
	private ScanModel scanModel;
	private ServiceGroup serviceGroup;
	private ReferenceGroup referenceGroup;
	public RegistryModel getRegistryModel() {
		return registryModel;
	}
	public RmiConfigArg setRegistryModel(RegistryModel registryModel) {
		this.registryModel = registryModel;
		return this;
	}
	public ProtocolModel getProtocolModel() {
		return protocolModel;
	}
	public RmiConfigArg setProtocolModel(ProtocolModel protocolModel) {
		this.protocolModel = protocolModel;
		return this;
	}
	public ScanModel getScanModel() {
		return scanModel;
	}
	public RmiConfigArg setScanModel(ScanModel scanModel) {
		this.scanModel = scanModel;
		return this;
	}
	public ServiceGroup getServiceGroup() {
		return serviceGroup;
	}
	public RmiConfigArg setServiceGroup(ServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
		return this;
	}
	public ReferenceGroup getReferenceGroup() {
		return referenceGroup;
	}
	public RmiConfigArg setReferenceGroup(ReferenceGroup referenceGroup) {
		this.referenceGroup = referenceGroup;
		return this;
	}
	public RmiConfigArg(RegistryModel registryModel, ProtocolModel protocolModel, ScanModel scanModel, ServiceGroup serviceGroup, ReferenceGroup referenceGroup) {
		super();
		this.registryModel = registryModel;
		this.protocolModel = protocolModel;
		this.scanModel = scanModel;
		this.serviceGroup = serviceGroup;
		this.referenceGroup = referenceGroup;
	}
	public RmiConfigArg() {
		super();
	}

}
