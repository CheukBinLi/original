package com.cheuks.bin.original.common.annotation.reflect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 
 * @author Bin
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Replace {

	/***
	 * 唯一ID
	 * 
	 * @return
	 */
	String id() default "-1";

	/***
	 * 1:你好吗
	 * 2:一点都不好
	 * @return
	 */
	String[] replacementRule() default "";

}
