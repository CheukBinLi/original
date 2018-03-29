package com.cheuks.bin.original.db.config.model;

import java.io.Serializable;
import java.util.Set;

import com.cheuks.bin.original.common.util.conver.ObjectFill;
import com.cheuks.bin.original.db.config.ContentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HibernateSessionFactoryModel extends ObjectFill implements ContentType.HibernateSessionFactoryConfigType, Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String ref;
	private String clazz;
	private String dialect;
	private Boolean autoReconnect;
	private Boolean useSecondCache;
	private String cacheConfigurationFileResourcePath;
	private String cacheRegionFactoryClass;
	private Boolean showSql;
	private Boolean formatSql;
	private Boolean hbm2ddl;
	private String validationMode;
	/***
	 * mapper
	 */
	private String mapperToScan;
	/***
	 * mapper resources
	 */
	private Set<String> mappers;

	private Set<String> entitys;
	/***
	 * entity
	 */
	private String EntityToScan;
}
