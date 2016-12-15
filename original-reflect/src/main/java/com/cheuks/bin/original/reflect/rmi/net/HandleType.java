package com.cheuks.bin.original.reflect.rmi.net;

public interface HandleType {

	/***
	 * 心跳类型
	 */
	public final static int HEAR_BEAT = 0XAAAA;
	/***
	 * 远程调用请求类型
	 */
	public final static int RMI_REQUEST = 0XAABA;
	/***
	 * 远程调用响应类型
	 */
	public final static int RMI_RESPONSE = 0XAABB;
}
