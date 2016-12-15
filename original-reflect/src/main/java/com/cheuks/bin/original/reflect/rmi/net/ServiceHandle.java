package com.cheuks.bin.original.reflect.rmi.net;

/**
 * 消息处理接口
 * 
 * @author ben
 *
 * @param <Input>
 *            ChannelHandlerContext
 * @param <Value>
 *            TransmissionModel
 */
public interface ServiceHandle<Input, Value> extends HandleType {

	void invoke(Input i, Value v);

}
