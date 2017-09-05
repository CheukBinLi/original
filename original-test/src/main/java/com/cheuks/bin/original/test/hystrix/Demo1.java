package com.cheuks.bin.original.test.hystrix;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

public class Demo1 extends HystrixCommand<String> {

	private String name;

	public Demo1(String name) {
		//		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(Demo1.class.getSimpleName())));//普通
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(Demo1.class.getSimpleName())).andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));//超时降级机制
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		String threadName = Thread.currentThread().getName();
		if (Integer.parseInt(threadName.substring(threadName.lastIndexOf("-") + 1)) == 4) {
			Thread.sleep(1000);
		}
		return Demo1.class.getSimpleName() + ":" + name + " : " + Thread.currentThread().getName();
	}

	@Override
	protected String getFallback() {
		return Demo1.class.getSimpleName() + " 降级:" + name + " : " + Thread.currentThread().getName();
	}

	//调用实例  
	public static void main(String[] args) throws Exception {
		int a = 2;
		if (a != 1 || a != 2) {
			System.out.println("#######################");
		}

		//每个Command对象只能调用一次,不可以重复调用,  
		//重复调用对应异常信息:This instance can only be executed once. Please instantiate a new instance.  
		Demo1 helloWorldCommand = new Demo1("Synchronous-hystrix");
		//使用execute()同步调用代码,效果等同于:helloWorldCommand.queue().get();   
		String result = helloWorldCommand.execute();
		System.out.println("result=" + result);

		helloWorldCommand = new Demo1("Asynchronous-hystrix");
		//异步调用,可自由控制获取结果时机,  
		Future<String> future = helloWorldCommand.queue();
		//get操作不能超过command定义的超时时间,默认:1秒  
		result = future.get(100, TimeUnit.MILLISECONDS);
		System.out.println("result=" + result);
		System.out.println("mainThread=" + Thread.currentThread().getName());

		Demo1 x1 = new Demo1("侦听");
		Observable<String> obServe = x1.observe();
		obServe.subscribe(new Observer<String>() {

			public void onCompleted() {
				System.out.println("onCompleted");
			}

			public void onError(Throwable arg0) {
				System.out.println("onError");
			}

			public void onNext(String arg0) {
				System.out.println("onNext" + arg0);
			}
		});
		//
		obServe.subscribe(new Action1<String>() {

			public void call(String arg0) {
				System.out.println("callback0:" + arg0);
			}
		});
		obServe.subscribe(new Action1<String>() {

			public void call(String arg0) {
				System.out.println("callback1:" + arg0);
			}
		});
		//		System.out.println(x1.queue().get());

		Demo1 x2 = new Demo1("侦听");
		System.err.println(x2.execute());
	}

}
