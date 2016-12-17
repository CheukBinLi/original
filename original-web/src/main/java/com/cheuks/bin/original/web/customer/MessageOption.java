package com.cheuks.bin.original.web.customer;

public interface MessageOption {
	public static enum SenderType {
		CLUSTER, SYSTEM, CUSTOMER_SERVICE
	}

	public static enum MessagePackageType {
		CLUSTER_REFRESH, // 集群列表刷新
		WAITING_FOR_ACCESS, // 接入等待
		HEART_BEAT, // 心跳
		REQUEST, // 请求
		RESPONSE// 回复
	}

	public static enum ContainerType {
		CLUSTER
	}
}
