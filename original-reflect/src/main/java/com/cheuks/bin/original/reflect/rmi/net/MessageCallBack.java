package com.cheuks.bin.original.reflect.rmi.net;

/***
 * 消息回调返回结果接口
 * 
 * @author ben
 *
 * @param <Input>
 * @param <Value>
 */
public interface MessageCallBack<Input, Value> extends HandleType {

	void setResult(final Value value);

	Value callBack() throws Exception;
}
