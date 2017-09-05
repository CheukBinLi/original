package com.cheuks.bin.original.common.rmi.net;

import com.cheuks.bin.original.common.rmi.RmiContant;

/***
 * 消息回调返回结果接口
 * 
 * @author ben
 *
 * @param <Input> channel|ChannelHandlerContext
 * @param <Value> 私有栈MODEL|TransmissionModel
 */
public interface MessageCallBack<Input, Value> extends RmiContant {

	/***
	 * 清空结果
	 */
	void clean();

	/***
	 * 插入结果
	 * 
	 * @param value 结果
	 */
	void setResult(final Value value);

	/***
	 * 回调阻塞
	 * 
	 * @return
	 * @throws Exception
	 */
	Value callBack() throws Exception;
}
