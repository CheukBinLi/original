package com.cheuks.bin.original.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IndexField {

	public static final String ANALYZED_IK = "ik";
	public static final String INDEX_ANALYZED = "analyzed";
	public static final String INDEX_NOT_ANALYZED = "not_analyzed";

	public static final String STORE_YES = "yes";
	public static final String STORE_NO = "no";

	public static final String ANALYZED_FIELD_NAME_ANALYZED = "analyzer";

	/***
	 * 使用的分析器
	 * 
	 * @return
	 */
	String analyzerFieldName() default ANALYZED_FIELD_NAME_ANALYZED;

	String analyzer();// ik_max_word

	/***
	 * 
	 * @return
	 */
	String store() default STORE_YES;

	/***
	 * 是否拆分
	 * 
	 * @return
	 */
	String index() default INDEX_ANALYZED;

}
