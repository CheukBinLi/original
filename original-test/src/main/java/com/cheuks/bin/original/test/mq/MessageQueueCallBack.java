package com.cheuks.bin.original.test.mq;

public abstract class MessageQueueCallBack<T> {

	public abstract void onCompletion(T paramRecordMetadata, Exception paramException);

}
