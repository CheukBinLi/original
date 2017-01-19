package java.com.cheuks.bin.original.reflect.net;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.pool2.ObjectPool;

import io.netty.channel.Channel;

public class NettyClientPool implements ObjectPool<Channel> {

	private final BlockingDeque<Channel> POOL = new LinkedBlockingDeque<Channel>();
	private final CopyOnWriteArraySet<Channel> BORROW_POOL = new CopyOnWriteArraySet<Channel>();

	public Channel borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
		Channel channel = POOL.take();
		BORROW_POOL.add(channel);
		return channel;
	}

	public void returnObject(Channel obj) throws Exception {
		BORROW_POOL.remove(obj);
		if (obj.isActive()) {
			POOL.addLast(obj);
		} else {
			addObject();
			invalidateObject(obj);
		}

	}

	public void invalidateObject(Channel obj) throws Exception {
		try {
			obj.disconnect();
			obj.close();
		} catch (Exception e) {
		}
	}

	public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {

	}

	public int getNumIdle() {
		return POOL.size();
	}

	public int getNumActive() {
		return BORROW_POOL.size();
	}

	public void clear() throws Exception, UnsupportedOperationException {
		synchronized (this) {
			for (Channel channel : POOL) {
				try {
					channel.disconnect();
					channel.close();
				} catch (Exception e) {
				}
			}
			POOL.clear();
			Iterator<Channel> it = BORROW_POOL.iterator();
			Channel tempChannel;
			while (it.hasNext()) {
				try {
					tempChannel = it.next();
					tempChannel.disconnect();
					tempChannel.close();
				} catch (Exception e) {
				}
			}
			BORROW_POOL.clear();
		}
	}

	private void addObjects(int count) {
		for (int i = 0; i < count; i++) {
			addObject();
		}
	}

}
