package com.cheuks.bin.message.queue.kafka;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.message.queue.MessageQueueCallBack;
import com.cheuks.bin.original.common.message.queue.MessageQueueException;
import com.cheuks.bin.original.common.message.queue.MessageQueueProducerFactory;

public class KafkaMessageQueueProducerFactory implements MessageQueueProducerFactory<RecordMetadata, RecordMetadata> {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageQueueProducerFactory.class);

	/***
	 * 服务列表: 127.0.0.1:9092,127.0.0.2:9092,127.0.0.3:9092
	 */
	private String serverList;

	/***
	 * producer需要server接收到数据之后发出的确认接收的信号，此项配置就是指procuder需要多少个这样的确认信号。此配置实际上代表了数据备份的可用性。以下设置为常用选项：
	 * 
	 * @（1）acks=0： 设置为0表示producer不需要等待任何确认收到的信息。<br>
	 *             副本将立即加到socket buffer并认为已经发送。 没有任何保障可以保证此种情况下server已经成功接收数据，
	 *             同时重试配置不会发生作用（因为客户端不知道是否失败） 回馈的offset会总是设置为-1；
	 * 
	 * @（2）acks=1：这意味着至少要等待leader已经成功将数据写入本地log， 但是并没有等待所有follower是否成功写入。这种情况下，
	 * 如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
	 * 
	 * @（3）acks=all： 这意味着leader需要等待所有备份都成功写入日志， 这种策略会保证只要有一个备份存活就不会丢失数据。
	 * 这是最强的保证。
	 * 
	 * @（4）其他的设置，例如acks=2也是可以的， 这将需要给定的acks数量，但是这种策略一般很少用。
	 */
	private String acks;

	/***
	 * 当向server发出请求时，这个字符串会发送给server。 <br>
	 * 目的是能够追踪请求源头，以此来允许ip/port许可列表之外的一些应用可以发送信息。
	 * 这项应用可以设置任意字符串，因为没有任何功能性的目的，除了记录和跟踪
	 */
	private String clientId;

	/***
	 * 设置大于0的值将使客户端重新发送任何数据，一旦这些数据发送失败。<br>
	 * 注意，这些重试与客户端接收到发送错误时的重试没有什么不同。 <br>
	 * 允许重试将潜在的改变数据的顺序，如果这两个消息记录都是发送到同一个partition，
	 * 则第一个消息失败第二个发送成功，则第二条消息会比第一条消息出现要早。
	 */
	private Integer retries;

	/***
	 * producer将试图批处理消息记录，以减少请求次数。 <br>
	 * 这将改善client与server之间的性能。这项配置控制默认的批量处理消息字节数。<br>
	 * 不会试图处理大于这个字节数的消息字节数。<br>
	 * 发送到brokers的请求将包含多个批量处理， 其中会包含对每个partition的一个请求。
	 * 较小的批量处理数值比较少用，并且可能降低吞吐量（0则会仅用批量处理）。
	 * 较大的批量处理数值将会浪费更多内存空间，这样就需要分配特定批量处理数值的内存大小。
	 */
	private Integer batchSize = 1000;// 批量大小

	/***
	 * producer组将会汇总任何在请求与发送之间到达的消息记录一个单独批量的请求。<br>
	 * 通常来说，这只有在记录产生速度大于发送速度的时候才能发生。<br>
	 * 然而，在某些条件下，客户端将希望降低请求的数量，甚至降低到中等负载一下。<br>
	 * 这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，<br>
	 * producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理。<br>
	 * 这可以认为是TCP种Nagle的算法类似。<br>
	 * 这项设置设定了批量处理的更高的延迟边界：一旦我们获得某个partition的batch.size，<br>
	 * 他将会立即发送而不顾这项设置，然而如果我们获得消息字节数比这项设置要小的多，<br>
	 * 我们需要“linger”特定的时间以获取更多的消息。<br>
	 * 这个设置默认为0，即没有延迟。设定linger.ms=5，<br>
	 * 例如，将会减少请求数目，但是同时会增加5ms的延迟。
	 */
	private Integer lingerMs;

	/***
	 * producer可以用来缓存数据的内存大小。<br>
	 * 如果数据产生速度大于向broker发送的速度，<br>
	 * producer会阻塞或者抛出异常，以“block.on.buffer.full”来表明。
	 * 
	 * 这项设置将和producer能够使用的总内存相关，<br>
	 * 但并不是一个硬性的限制，因为不是producer使用的所有内存都是用于缓存。<br>
	 * 一些额外的内存会用于压缩（如果引入压缩机制），<br>
	 * 同样还有一些用于维护请求。
	 */
	private Long bufferMemory = 33554432L;// 32M

	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

	private Properties propertie = new Properties();
	private volatile Producer<String, String> producer;
	private volatile boolean isInit = false;
	private long timeoutMillis=30000;

	public synchronized RecordMetadata makeMessage(String queueName, String message, Object additional) throws MessageQueueException {
		final Future<RecordMetadata> response = producer.send(new ProducerRecord<String, String>(queueName, message, message));
		try {
			return response.get(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch (Throwable e) {
			throw new MessageQueueException(e);
		}finally {}
		// return producer.send(new ProducerRecord<String, String>(queueName,
		// null != additional ? additional.toString() : null, message));
	}

	public synchronized void makeAsyncMessage(String queueName, String message, Object additional, final MessageQueueCallBack<RecordMetadata> callBack) throws MessageQueueException {
		final Future<RecordMetadata> response = producer.send(new ProducerRecord<String, String>(queueName, null != additional ? additional.toString() : null, message), new Callback() {
			// 回调
			public void onCompletion(RecordMetadata paramRecordMetadata, Exception paramException) {
				callBack.onCompletion(paramRecordMetadata, paramException);
			}
		});
	}

	public void destory() {
		producer.close();
	}

	public MessageQueueProducerFactory<RecordMetadata, RecordMetadata> init() {
		return init(null);
	}

	public KafkaMessageQueueProducerFactory init(Map<String, Object> args) {
		if (isInit)
			return this;
		if (null == serverList) {
			LOG.error("serverList is null.");
		}
		//		else if (clientId == null) {
		//			try {
		//				clientId = InetAddress.getLocalHost().getHostName() + "_" + Thread.currentThread().getName();
		//			} catch (UnknownHostException e) {
		//				LOG.error("获取服务器名称失败", e);
		//			}
		//		}
		propertie.put("bootstrap.servers", serverList);
		if (null != clientId)
			propertie.put("client.id", clientId);
		if (null != acks)
			propertie.put("acks", acks);
		if (null != retries)
			propertie.put("retries", retries);
		if (null != batchSize)
			propertie.put("batch.size", batchSize);
		if (null != lingerMs)
			propertie.put("linger.ms", lingerMs);
		if (null != bufferMemory)
			propertie.put("buffer.memory", bufferMemory);
		if (null != keySerializer)
			propertie.put("key.serializer", keySerializer);
		if (null != valueSerializer)
			propertie.put("value.serializer", valueSerializer);
		if (null != args)
			propertie.putAll(args);
		producer = new KafkaProducer<String, String>(propertie);
		return this;
	}

	public String getServerList() {
		return serverList;
	}

	public KafkaMessageQueueProducerFactory setServerList(String serverList) {
		this.serverList = serverList;
		return this;
	}

	public String getAcks() {
		return acks;
	}

	public KafkaMessageQueueProducerFactory setAcks(String acks) {
		this.acks = acks;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public KafkaMessageQueueProducerFactory setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public int getRetries() {
		return retries;
	}

	public KafkaMessageQueueProducerFactory setRetries(int retries) {
		this.retries = retries;
		return this;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public KafkaMessageQueueProducerFactory setBatchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	public int getLingerMs() {
		return lingerMs;
	}

	public KafkaMessageQueueProducerFactory setLingerMs(int lingerMs) {
		this.lingerMs = lingerMs;
		return this;
	}

	public Long getBufferMemory() {
		return bufferMemory;
	}

	public KafkaMessageQueueProducerFactory setBufferMemory(Long bufferMemory) {
		this.bufferMemory = bufferMemory;
		return this;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public KafkaMessageQueueProducerFactory setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
		return this;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public KafkaMessageQueueProducerFactory setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
		return this;
	}

	public Properties getPropertie() {
		return propertie;
	}

	public KafkaMessageQueueProducerFactory setPropertie(Properties propertie) {
		this.propertie.putAll(propertie);
		return this;
	}

	public Producer<String, String> getProducer() {
		return producer;
	}

	public KafkaMessageQueueProducerFactory setProducer(Producer<String, String> producer) {
		this.producer = producer;
		return this;
	}

	public KafkaMessageQueueProducerFactory() {
		super();
	}

	public KafkaMessageQueueProducerFactory(String serverList) {
		super();
		this.serverList = serverList;
	}

}
