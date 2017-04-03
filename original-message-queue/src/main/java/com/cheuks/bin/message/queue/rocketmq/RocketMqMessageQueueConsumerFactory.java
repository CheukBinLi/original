package com.cheuks.bin.message.queue.rocketmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerFactory;
import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerHandler;

public class RocketMqMessageQueueConsumerFactory implements MessageQueueConsumerFactory<String, RocketMqConsumerRecord> {

	private final static Logger LOG = LoggerFactory.getLogger(RocketMqMessageQueueConsumerFactory.class);

	private final Map<String, List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>>> HANDLER_POOL = new ConcurrentSkipListMap<String, List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>>>();

	public static final String ALL_TAGS = "*";

	private String serverList;
	private String topicList;
	private String consumerGroup;
	private String instanceName;
	/***
	 * 0：CONSUME_FROM_LAST_OFFSET:第一次启动从队列最后位置消费，后续再启动接着上次消费的进度开始消费 <br>
	 * CONSUME_FROM_LAST_OFFSET_AND_FROM_MIN_WHEN_BOOT_FIRST,<br>
	 * CONSUME_FROM_MIN_OFFSET,<br>
	 * CONSUME_FROM_MAX_OFFSET, <br>
	 * 4: CONSUME_FROM_FIRST_OFFSET:第一次启动从队列初始位置消费，后续再启动接着上次消费的进度开始消费 <br>
	 * 5：CONSUME_FROM_TIMESTAMP:第一次启动从指定时间点位置消费，后续再启动接着上次消费的进度开始消费
	 */
	private int consumeFromWhere = 4;

	private DefaultMQPushConsumer consumer;

	public void RegisterHandler(MessageQueueConsumerHandler<String, RocketMqConsumerRecord> handler) {
		String[] tags = handler.getQueueInfo().split(",");
		for (String tag : tags) {
			List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>> handlers = HANDLER_POOL.get(tag);
			if (null == handlers) {
				handlers = new ArrayList<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>>();
				HANDLER_POOL.put(tag, handlers);
			}
			handlers.add(handler);
		}
	}

	public void RegisterHandler(List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>> handlers) {
		for (MessageQueueConsumerHandler<String, RocketMqConsumerRecord> consumerHandler : handlers) {
			RegisterHandler(consumerHandler);
		}
	}

	public RocketMqMessageQueueConsumerFactory init(Map<String, Object> args) {
		consumer = new DefaultMQPushConsumer(consumerGroup);
		consumer.setNamesrvAddr(serverList);
		if (null != instanceName) {
			consumer.setInstanceName(instanceName);
		}
		consumer.setConsumeFromWhere(ConsumeFromWhere.values()[consumeFromWhere]);
		try {
			consumer.subscribe(topicList, "*");
			consumer.registerMessageListener(new MessageListenerConcurrently() {

				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>> handlers;
					boolean nothing = true;
					for (MessageExt message : msgs) {
						/**
						 * 过滤消息主题:tag </br>
						 */
						handlers = HANDLER_POOL.get(message.getTags());
						if (null != handlers) {
							iterador(handlers, message, context);
							nothing = false;
						}
						if (null != (handlers = HANDLER_POOL.get(ALL_TAGS))) {
							iterador(handlers, message, context);
							nothing = false;
						} else if (nothing) {
							LOG.warn("can't found tag:{} msg:{}", message.getTags(), new String(message.getBody()));
						}
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
			System.err.println("监听");
		} catch (MQClientException e) {
			LOG.error(null, e);
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {
				if (null != consumer)
					consumer.shutdown();
			}

		}));
		return this;
	}

	private void iterador(final List<MessageQueueConsumerHandler<String, RocketMqConsumerRecord>> handlers, final MessageExt message, final ConsumeConcurrentlyContext context) {
		if (LOG.isDebugEnabled())
			LOG.debug("topic:{} tag:{} msg:{}", message.getTopic(), message.getTags(), new String(message.getBody()));
		for (MessageQueueConsumerHandler<String, RocketMqConsumerRecord> messageQueueConsumerHandler : handlers) {
			messageQueueConsumerHandler.doProcess(new String(message.getBody()), new RocketMqConsumerRecord(message, context));
		}
	}

	public void destory() {
		if (null != consumer)
			consumer.shutdown();
	}

	public String getServerList() {
		return serverList;
	}

	public RocketMqMessageQueueConsumerFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public String getTopicList() {
		return topicList;
	}

	public RocketMqMessageQueueConsumerFactory setTopicList(String topicList) {
		this.topicList = topicList;
		return this;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public RocketMqMessageQueueConsumerFactory setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
		return this;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public RocketMqMessageQueueConsumerFactory setInstanceName(String instanceName) {
		this.instanceName = instanceName;
		return this;
	}

	public int getConsumeFromWhere() {
		return consumeFromWhere;
	}

	public RocketMqMessageQueueConsumerFactory setConsumeFromWhere(int consumeFromWhere) {
		this.consumeFromWhere = consumeFromWhere;
		return this;
	}

	public DefaultMQPushConsumer getConsumer() {
		return consumer;
	}

	public RocketMqMessageQueueConsumerFactory setConsumer(DefaultMQPushConsumer consumer) {
		this.consumer = consumer;
		return this;
	}

}
