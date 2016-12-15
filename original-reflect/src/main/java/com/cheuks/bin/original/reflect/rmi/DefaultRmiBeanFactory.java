package com.cheuks.bin.original.reflect.rmi;

import java.util.Map;

/***
 * 单例模式实现
 * 
 * @author ben
 *
 */
public class DefaultRmiBeanFactory extends SimpleRmiBeanFactory {

	private static final DefaultRmiBeanFactory newInstance = new DefaultRmiBeanFactory();
	private volatile int initCount;
	private String tempSuffixName;

	public static final DefaultRmiBeanFactory newInstance() {
		return newInstance;
	}

	public DefaultRmiBeanFactory() {
		super();
		tempSuffixName = getSuffixName();
	}

	@Override
	public void init(Map<String, Object> args) {
		initCount++;
		setSuffixName(tempSuffixName + "_" + initCount);
		super.init(args);
	}
}
