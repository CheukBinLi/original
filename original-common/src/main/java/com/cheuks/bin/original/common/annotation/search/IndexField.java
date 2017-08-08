package com.cheuks.bin.original.common.annotation.search;

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

	public static final String ANALYZED_IK_MAX_WORD = "ik_max_word";
	public static final String ANALYZED_IK_SMART = "ik_smart";
	public static final String INDEX_ANALYZED = "analyzed";
	public static final String INDEX_NOT_ANALYZED = "not_analyzed";

	public static final String STORE_YES = "yes";
	public static final String STORE_NO = "no";

	public static final String ANALYZED_FIELD_NAME_ANALYZED = "analyzer";

	public static final String TERM_VECTOR_YES = "yes";
	public static final String TERM_VECTOR_NO = "no";

	public static final String INCLUDE_IN_ALL_TRUE = "true";
	public static final String INCLUDE_IN_ALL_false = "false";

	/***
	 * 使用的分析器
	 * 
	 * @return
	 */
	String analyzerFieldName() default ANALYZED_FIELD_NAME_ANALYZED;

	String analyzer() default ANALYZED_IK_MAX_WORD;// ik_max_word

	/***
	 * 
	 * @return
	 */
	String store() default STORE_NO;

	/***
	 * 是否拆分
	 * 
	 * @return
	 */
	String index() default INDEX_ANALYZED;

	String searchAnalyzer() default ANALYZED_IK_MAX_WORD;

	String termVector() default TERM_VECTOR_NO;

	String includeInAll() default INCLUDE_IN_ALL_TRUE;

}
