package java.com.cheuks.bin.original.reflect.net;

import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.cheuks.bin.original.reflect.rmi.net.MessageHandle;
import com.cheuks.bin.original.reflect.rmi.net.ServiceHandle;
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
public class HandleService2<Input, Value> implements MessageHandle<Input, Value, Integer> {

	private final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	private final BlockingDeque<HandleWorker> QUEUE = new LinkedBlockingDeque<HandleWorker>();

	private final Map<String, ServiceHandle<Input, Value>> SERVICE_HANDLE = new ConcurrentHashMap<String, ServiceHandle<Input, Value>>();

	private volatile boolean interrupt;
	private volatile int pollInterval = 50;

	public Input getObject() {
		return null;
	}

	public void start(Integer threadSize) {
		HandleWorker handleWorker;
		for (int i = 0; i < threadSize; i++) {
			handleWorker = new HandleWorker();
			QUEUE.addLast(handleWorker);
			EXECUTOR_SERVICE.execute(handleWorker);
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
		// QUEUE.addLast(new InvokeModel(serviceHandle, in, v));
		HandleWorker worker;
		do {
			if (null != (worker = QUEUE.poll(pollInterval, TimeUnit.MILLISECONDS)))
				QUEUE.addLast(worker.addJOB(new InvokeModel(serviceHandle, in, v)));
		} while (null == worker);
	}

	class HandleWorker implements Runnable {
		private final BlockingDeque<InvokeModel> JOB = new LinkedBlockingDeque<InvokeModel>();

		public void run() {
			InvokeModel invokeModel;
			try {
				while (!interrupt) {
					// if (null == (invokeModel = QUEUE.poll())) {
					if (null == (invokeModel = JOB.poll(pollInterval, TimeUnit.MILLISECONDS))) {
						continue;
					}
					invokeModel.handle.invoke(invokeModel.in, invokeModel.v);
				}
			} catch (InterruptedException e) {
				Log.warn("HandleWorker is error", e);
			}
		}

		public HandleWorker addJOB(InvokeModel job) {
			JOB.addLast(job);
			return this;
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
