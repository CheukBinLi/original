package com.cheuks.bin.message.queue.rocketmq;

import java.util.Map;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.message.queue.MessageQueueCallBack;
import com.cheuks.bin.original.common.message.queue.MessageQueueException;
import com.cheuks.bin.original.common.message.queue.MessageQueueProducerFactory;

public class RocketMqMessageQueueProducerFactory implements MessageQueueProducerFactory<SendResult, SendResult> {
	private final static Logger LOG = LoggerFactory.getLogger(RocketMqMessageQueueProducerFactory.class);

	private String serverList;
	private String producerGroupName;
	private String instanceName;
	private String charset = "UTF-8";
	private volatile boolean isRunning;

	private DefaultMQProducer producer;

	/***
	 * @param queueName topic
	 * @param message msg
	 * @param additional tags
	 * @throws e
	 * @return
	 */
	public SendResult makeMessage(String queueName, String message, Object additional) throws MessageQueueException {
		try {
			return producer.send(new Message(queueName, additional.toString(), message.getBytes(charset)));
		} catch (Exception e) {
			throw new MessageQueueException(e);
		}
	}

	/***
	 * @param queueName topic
	 * @param message msg
	 * @param additional tags
	 * @throws e
	 * @return 抛出或者返回空对象new SendResult();
	 */
	public SendResult makeMessage(String queueName, String message, Object additional, final MessageQueueCallBack<SendResult> callBack) throws MessageQueueException {
		try {
			producer.send(new Message(queueName, additional.toString(), message.getBytes(charset)), new SendCallback() {
				public void onSuccess(SendResult sendResult) {
					callBack.onCompletion(sendResult, null);
				}

				public void onException(Throwable e) {
					callBack.onCompletion(null, new Exception(e));
				}
			});
			return new SendResult();
		} catch (Exception e) {
			throw new MessageQueueException(e);
		}
	}

	public RocketMqMessageQueueProducerFactory init(Map<String, Object> args) {
		if (isRunning)
			return this;
		isRunning = true;
		try {
			producer = new DefaultMQProducer(producerGroupName);
			producer.setNamesrvAddr(serverList);
			if (null != instanceName)
				producer.setInstanceName(instanceName);
			producer.start();
		} catch (MQClientException e) {
			isRunning = false;
			LOG.error(null, e);
		}

		return this;
	}

	public void destory() {
		if (null != producer) {
			producer.shutdown();
			isRunning = false;
		}
	}

	public String getServerList() {
		return serverList;
	}

	public RocketMqMessageQueueProducerFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public String getProducerGroupName() {
		return producerGroupName;
	}

	public RocketMqMessageQueueProducerFactory setProducerGroupName(String producerGroupName) {
		this.producerGroupName = producerGroupName;
		return this;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public RocketMqMessageQueueProducerFactory setInstanceName(String instanceName) {
		this.instanceName = instanceName;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public RocketMqMessageQueueProducerFactory setCharset(String charset) {
		this.charset = charset;
		return this;
	}

}
