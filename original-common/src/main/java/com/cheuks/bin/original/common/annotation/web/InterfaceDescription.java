package com.cheuks.bin.original.common.annotation.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 接口描述
 * 
 * @Title: original-common
 * @Description:接口描述
 * @Company:
 * @Email: 20796698@qq.com
 * @author cheuk.bin.li
 * @date 2017年7月24日 下午9:27:04
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface InterfaceDescription {

    static String TYPE_CLASS = "class";

    static String TYPE_METHOD = "method";

    /***
     * 接口类型 (TYPE_METHOD,TYPE_METHOD)
     * 
     * @return
     */
    String type() default "";

    /***
     * 接口信息描述
     * 
     * @return
     */
    String value() default "";

    /***
     * 是否启用(扩展功能)
     * 
     * @return
     */
    boolean enable() default false;

}
