package com.cheuks.bin.original.common.util.design.factory;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cheuks.bin.original.common.util.reflection.ClassInfo;
import com.cheuks.bin.original.common.util.reflection.ReflectionUtil;

public abstract class AbstractHandlerManager<T extends Handler<?>> implements HandlerManager<T, String> {

	static final Logger log = LoggerFactory.getLogger(AbstractHandlerManager.class);

	protected ConcurrentSkipListMap<String, T> POOL = new ConcurrentSkipListMap<String, T>();
	protected ConcurrentSkipListMap<String, T> POOL_FOR_NAME = new ConcurrentSkipListMap<String, T>();

	private volatile boolean isInit = false;
	private volatile String scanPackage = null;

	@Override
	public T getHandler(String typeOrName) {
		T result = POOL.get(typeOrName);
		return null == result ? POOL_FOR_NAME.get(typeOrName) : result;
	}

	@Override
	public void addHandler(T t) {
		POOL.put(t.getType(), t);
		POOL_FOR_NAME.put(t.getClass().getName(), t);
	}

	@Override
	public boolean concat(String type) {
		return POOL.containsKey(type);
	}

	@Override
	public boolean concat(Class<T> c) {
		return POOL_FOR_NAME.containsKey(c.getName());
	}

	@Override
	public void remove(String type) {
		POOL.remove(type);
	}

	@Override
	public T instance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public String getScanPackage() {
		return scanPackage;
	}

	public AbstractHandlerManager<T> setScanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			synchronized (this) {
				if (isInit)
					return;
				isInit = true;
			}
			Set<ClassInfo> handlers = ReflectionUtil.instance().findImplementationForSpringBoot(null == getScanPackage() ? this.getClass().getPackage().getName() : getScanPackage(), this.getClass().getClassLoader(), getSuperHandler());
			handlers.forEach(item -> {
				if (!item.isAbstract()) {
					try {
						addHandler(instance((Class<T>) item.getClazz()));
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				}
			});
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

}
