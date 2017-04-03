package com.cheuks.bin.message.queue.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerFactory;
import com.cheuks.bin.original.common.message.queue.MessageQueueConsumerHandler;

public class KafkaMessageQueueConsumerFactory implements MessageQueueConsumerFactory<String, ConsumerRecord<String, String>> {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageQueueProducerFactory.class);

	private final Map<String, MessageQueueConsumerHandler<String, ConsumerRecord<String, String>>> HANDLER_POOL = new ConcurrentSkipListMap<String, MessageQueueConsumerHandler<String, ConsumerRecord<String, String>>>();

	/***
	 * 服务列表: 127.0.0.1:9092,127.0.0.2:9092,127.0.0.3:9092
	 */
	private String serverList;
	/***
	 * 主题: phone,water,tv,foo,pc
	 */
	private String topicList;
	/***
	 * 用来唯一标识consumer进程所在组的字符串，如果设置同样的group id，表示这些processes都是属于同一个consumer
	 * group
	 */
	private String groupId;
	/***
	 * 自动后台提交。
	 */
	private Boolean autoCommit;
	/***
	 * 自动提交间隔,每隔1s，自动提交offsets
	 */
	private Integer autoCommitInterval = 1000;
	/***
	 * Consumer向集群发送自己的心跳(30秒)，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
	 */
	private Integer sessionTimeout;

	private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
	private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	private Properties propertie = new Properties();
	private volatile KafkaConsumer<String, String> consumer;
	private Thread listening;
	private volatile boolean interrupted = false;
	private volatile boolean isRunning = false;

	public void RegisterHandler(MessageQueueConsumerHandler<String, ConsumerRecord<String, String>> handler) {
		HANDLER_POOL.put(handler.getQueueInfo(), handler);
	}

	public void RegisterHandler(List<MessageQueueConsumerHandler<String, ConsumerRecord<String, String>>> handlers) {
		for (MessageQueueConsumerHandler<String, ConsumerRecord<String, String>> consumerHandler : handlers) {
			RegisterHandler(consumerHandler);
		}
	}

	public void destory() {
		interrupted = true;
	}

	public KafkaMessageQueueConsumerFactory init(Map<String, Object> args) {
		if (isRunning)
			return this;

		if (null == serverList) {
			LOG.error("serverList is null");
			return this;
		} else if (null == topicList) {
			LOG.error("topicList is null");
			return this;
		} else if (null == groupId) {
			LOG.error("groupId is null");
			return this;
		}
		propertie.put("bootstrap.servers", serverList);
		propertie.put("group.id", groupId);
		if (null != autoCommit)
			propertie.put("enable.auto.commit", autoCommit.toString());
		if (null != autoCommitInterval)
			propertie.put("auto.commit.interval.ms", autoCommitInterval);
		if (null != sessionTimeout)
			propertie.put("session.timeout.ms", sessionTimeout);
		if (null != keyDeserializer)
			propertie.put("key.deserializer", keyDeserializer);
		if (null != valueDeserializer)
			propertie.put("value.deserializer", valueDeserializer);
		if (null != args)
			propertie.putAll(args);

		isRunning = true;
		consumer = new KafkaConsumer<String, String>(propertie);
		consumer.subscribe(Arrays.asList(topicList.split(",")));// 订阅TOPIC
		listening = new Thread(new Runnable() {
			public void run() {
				System.out.println("开始");
				try {
					MessageQueueConsumerHandler<String, ConsumerRecord<String, String>> messageQueueConsumerHandler;
					while (!interrupted) {// 轮询
						ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
						for (TopicPartition partition : records.partitions()) {
							List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
							for (ConsumerRecord<String, String> record : partitionRecords) {
								if (LOG.isInfoEnabled()) {
									LOG.info("topic:{}\nkey{}\nvalue:{}", record.topic(), record.key(), record.value());
								}
								messageQueueConsumerHandler = HANDLER_POOL.get(record.topic());
								messageQueueConsumerHandler.doProcess(record.value(), record);
							}
							consumer.commitSync();// 同步
						}
					}
				} finally {
					consumer.close();
					interrupted = false;
				}

			}
		});
		listening.start();
		return this;
	}

	public String getServerList() {
		return serverList;
	}

	public KafkaMessageQueueConsumerFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public String getTopicList() {
		return topicList;
	}

	public KafkaMessageQueueConsumerFactory setTopicList(String topicList) {
		this.topicList = topicList;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public KafkaMessageQueueConsumerFactory setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public Boolean getAutoCommit() {
		return autoCommit;
	}

	public KafkaMessageQueueConsumerFactory setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
		return this;
	}

	public int getAutoCommitInterval() {
		return autoCommitInterval;
	}

	public KafkaMessageQueueConsumerFactory setAutoCommitInterval(int autoCommitInterval) {
		this.autoCommitInterval = autoCommitInterval;
		return this;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public KafkaMessageQueueConsumerFactory setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
		return this;
	}

	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public KafkaMessageQueueConsumerFactory setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
		return this;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public KafkaMessageQueueConsumerFactory setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
		return this;
	}

	public KafkaMessageQueueConsumerFactory() {
		super();
	}

	public KafkaMessageQueueConsumerFactory(String serverList, String topicList, String groupId) {
		super();
		this.serverList = serverList;
		this.topicList = topicList;
		this.groupId = groupId;
	}

}
