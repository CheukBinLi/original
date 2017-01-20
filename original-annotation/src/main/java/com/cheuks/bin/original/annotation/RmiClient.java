package com.cheuks.bin.original.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 远程调用客户端注解(接口/实现)
 * 
 * @author ben
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RmiClient {

	// String host() default "127.0.0.1:10086";

	/***
	 * 远程实例名字
	 * @return
	 */
	String serviceImplementation();

	/***
	 * 远程实例版本
	 * @return
	 */
	String version() default "1.0";

	/**
	 * 本地多例
	 * @return
	 */
	boolean multiInstance() default false;
}
