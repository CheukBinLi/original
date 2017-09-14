package com.cheuks.bin.original.rmi.net.netty.server;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.rmi.net.MessageHandle;
import com.cheuks.bin.original.common.rmi.net.MessageHandleFactory;

/***
 * 抽像消息处理服务
 * 
 * @author ben
 *
 * @param <Input>
 * @param <Value>
 */
public class HandleService<Input extends Object, Value extends Object> implements MessageHandleFactory<Input, Value, Integer> {

	private final static Logger LOG = LoggerFactory.getLogger(HandleService.class);

	private final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	private final BlockingDeque<InvokeModel> QUEUE = new LinkedBlockingDeque<InvokeModel>();

	private final Map<String, MessageHandle<Input, Value>> SERVICE_HANDLE = new ConcurrentHashMap<String, MessageHandle<Input, Value>>();

	private volatile boolean interrupt;
	private volatile int pollInterval = 50;

	public Input getObject() {
		return null;
	}

	public void start(Integer threadSize) {
		for (int i = 0; i < threadSize; i++) {
			EXECUTOR_SERVICE.execute(new HandleWorker());
		}
	}

	public boolean serviceTypeContains(int serviceType) {
		synchronized (SERVICE_HANDLE) {
			return SERVICE_HANDLE.containsKey(serviceType);
		}
	}

	public void registrationMessageHandle(int serviceType, MessageHandle<Input, Value> messageHandle) {
		SERVICE_HANDLE.put(Integer.toString(serviceType), messageHandle);
	}

	public void messageHandle(final Input in, final Value v, final int serviceType) {
		MessageHandle<Input, Value> messageHandle = SERVICE_HANDLE.get(Integer.toString(serviceType));
		if (null == messageHandle)
			throw new NullPointerException("handle is null");
		QUEUE.addLast(new InvokeModel(messageHandle, in, v));
	}

	class HandleWorker implements Runnable {
		public void run() {
			InvokeModel invokeModel;
			try {
				while (!interrupt) {
					if (null == (invokeModel = QUEUE.poll(pollInterval, TimeUnit.MILLISECONDS))) {
						continue;
					}
					invokeModel.handle.doHandle(invokeModel.in, invokeModel.v);
				}
			} catch (InterruptedException e) {
				LOG.warn(null, e);
			}
		}
	}

	class InvokeModel {
		private MessageHandle<Input, Value> handle;
		private Input in;
		private Value v;

		public InvokeModel(MessageHandle<Input, Value> handle, Input in, Value v) {
			super();
			this.handle = handle;
			this.in = in;
			this.v = v;
		}
	}

}
