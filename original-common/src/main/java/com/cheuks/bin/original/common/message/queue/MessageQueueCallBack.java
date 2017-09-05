package com.cheuks.bin.original.common.message.queue;

public abstract class MessageQueueCallBack<T> {

	public abstract void onCompletion(T paramRecordMetadata, Exception paramException);

}
