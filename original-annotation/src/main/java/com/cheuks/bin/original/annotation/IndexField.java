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
	String analyzer() default "ik";// ik_max_word

	store store() default store.YES;

	index index() default index.NOT_ANALYZED;

	static enum index {
		NOT_ANALYZED {
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		},
		NALYZED {
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		}
	}

	static enum store {
		YES {
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		},
		NO {
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		}
	}
}
