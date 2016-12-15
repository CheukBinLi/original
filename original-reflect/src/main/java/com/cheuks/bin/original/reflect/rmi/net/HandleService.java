package com.cheuks.bin.original.reflect.rmi.net;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.esotericsoftware.minlog.Log;

/***
 * 抽像消息处理服务
 * 
 * @author ben
 *
 * @param <Input>
 * @param <Value>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HandleService<Input, Value> implements MessageHandle<Input, Value, Integer> {

	private final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	private final BlockingDeque<InvokeModel> QUEUE = new LinkedBlockingDeque<InvokeModel>();

	private final Map<String, ServiceHandle<Input, Value>> SERVICE_HANDLE = new ConcurrentHashMap<String, ServiceHandle<Input, Value>>();

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

	public void addHandle(int serviceType, ServiceHandle serviceHandle) {
		SERVICE_HANDLE.put(Integer.toString(serviceType), serviceHandle);
	}

	public boolean handleContains(int serviceType) {
		synchronized (SERVICE_HANDLE) {
			return SERVICE_HANDLE.containsKey(serviceType);
		}
	}

	public void messageHandle(final Input in, final Value v, final int serviceType) throws Exception {
		ServiceHandle serviceHandle = SERVICE_HANDLE.get(Integer.toString(serviceType));
		if (null == serviceHandle)
			throw new NullPointerException("handle is null");
		QUEUE.addLast(new InvokeModel(serviceHandle, in, v));
	}

	class HandleWorker implements Runnable {
		public void run() {
			InvokeModel invokeModel;
			try {
				while (!interrupt) {
					// if (null == (invokeModel = QUEUE.poll())) {
					if (null == (invokeModel = QUEUE.poll(pollInterval, TimeUnit.MILLISECONDS))) {
						continue;
					}
					invokeModel.handle.invoke(invokeModel.in, invokeModel.v);
				}
			} catch (InterruptedException e) {
				Log.warn("HandleWorker is error", e);
			}
		}
	}

	class InvokeModel {
		private ServiceHandle handle;
		private Input in;
		private Value v;

		public InvokeModel(ServiceHandle handle, Input in, Value v) {
			super();
			this.handle = handle;
			this.in = in;
			this.v = v;
		}
	}
}
