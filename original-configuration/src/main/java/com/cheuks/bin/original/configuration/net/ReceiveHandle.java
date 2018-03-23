package com.cheuks.bin.original.configuration.net;

/***
 * 消息监听
 * 
 * @author BIN
 *
 */
public interface ReceiveHandle<T> {

	void receiveEvent(final T msg);

}
