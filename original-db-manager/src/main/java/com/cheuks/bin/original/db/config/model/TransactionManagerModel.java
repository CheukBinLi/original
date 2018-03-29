package com.cheuks.bin.original.db.config.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionManagerModel {

	private String id;
	private String ref;
	private String clazz;

	private SessionFactoryModel sessionFactory;

	private List<PatternModel> patterns;

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SessionFactoryModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private String name;
		private String ref;
		private String clazz;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatternModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private String pattern;
		private String isolation;
		private String noRollbackFor;
		private Long timeout;
		private String rollbackFor;
		private String readOnly;
		// propagation="REQUIRED"
		private String propagation;
	}

}
