package com.cheuks.bin.original.web.customer;

public interface MessageOption {
	public static enum SenderType {
		CLUSTER, SYSTEM, CUSTOMER_SERVICE
	}

	public static enum MessagePackageType {
		CLUSTER_REFRESH, // 集群列表刷新
		WAITING_FOR_ACCESS, // 接入等待
		SERVICE_DISTRIBUTION, // 服务分配
		NO_CUSTOMER_SERVICE, // 没有客服
		SEND_TO_SYSTEM, // 直接发送到系统通道
		HEART_BEAT, // 心跳
		REQUEST, // 请求
		RESPONSE, // 回复
		CLOSE// 关闭跟客服的聊天
	}

	public static enum ContainerType {
		CLUSTER
	}

	public static enum MessageType {
		TEXT, IMAGE, VOICE
	}
}
