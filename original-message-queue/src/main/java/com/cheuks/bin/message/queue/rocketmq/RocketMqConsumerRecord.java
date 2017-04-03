package com.cheuks.bin.message.queue.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public class RocketMqConsumerRecord {

	private MessageExt msg;
	private ConsumeConcurrentlyContext context;

	public MessageExt getMsg() {
		return msg;
	}

	public RocketMqConsumerRecord setMsg(MessageExt msg) {
		this.msg = msg;
		return this;
	}

	public ConsumeConcurrentlyContext getContext() {
		return context;
	}

	public RocketMqConsumerRecord setContext(ConsumeConcurrentlyContext context) {
		this.context = context;
		return this;
	}

	public RocketMqConsumerRecord(MessageExt msg, ConsumeConcurrentlyContext context) {
		super();
		this.msg = msg;
		this.context = context;
	}

	public RocketMqConsumerRecord() {
		super();
	}

}
