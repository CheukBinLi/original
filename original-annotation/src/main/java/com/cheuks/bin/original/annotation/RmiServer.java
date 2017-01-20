package com.cheuks.bin.original.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 远程调用服务端注解(实现)
 * 
 * @author ben
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RmiServer {
	/***
	 * 注解实例名字
	 * @return
	 */
	String serviceName() default "";

	/***
	 * 版本
	 * @return
	 */
	String version() default "1.0";

	/**
	 * 多例
	 * @return
	 */
	boolean multiInstance() default false;

}
