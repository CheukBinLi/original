package com.cheuks.bin.original.common.annotation.rmi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 
 * @Title: original-common
 * @Description: 提供者：实现注解(注解只能放在实现类上)
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年9月19日 下午10:06:43
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RmiProviderAnnotation {
	/***
	 * 注入名
	 * 
	 * @return
	 */
	String id() default "";

	/***
	 * 接口类(父类)
	 * 
	 * @return
	 */
	Class<?> interfaceClass();
	/***
	 * 服务名
	 * 
	 * @return
	 */
	String serviceName() default "";

	/***
	 * 版本
	 * 
	 * @return
	 */
	String version() default "";

	/***
	 * 单例/多例
	 * 
	 * @return
	 */
	boolean multiInstance() default false;
}
