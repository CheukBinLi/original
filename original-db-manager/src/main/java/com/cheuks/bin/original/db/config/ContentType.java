package com.cheuks.bin.original.db.config;

public interface ContentType {

	interface DbConfigType {
		static final String DB_CONFIG_MODEL = "DB_CONFIG_MODEL";
		static final String DYNAMIC_ROUTING_DATA_SOURCE_MODEL = "DYNAMIC_ROUTING_DATA_SOURCE_MODEL";
		static final String HIBERNATE_SESSION_FACTORY_MODEL = "HIBERNATE_SESSION_FACTORY_MODEL";
		static final String TRANSACTION_MANAGER_MODEL = "TRANSACTION_MANAGER_MODEL";

	}

	interface HibernateSessionFactoryConfigType {
		String HIBERNATE_SESSION_FACTORY = "hibernateSessionFactory";
		String DIALECT = "hibernate.dialect";
		String USE_SECOND_CACHE = "cache.use_second_level_cache";
		String AUTO_RECONNECT = "hibernate.autoReconnect";
		String CACHE_CONFIGURATION_FILE_RESOURCE_PATH = "hibernate.cache.provider_configuration_file_resource_path";
		String CACHE_REGION_FACTORY_CLASS = "hibernate.cache.region.factory_class";
		String SHOW_SQL = "hibernate.show_sql";
		String FORMAT_SQL = "hibernate.format_sql";
		String HDM2DDL = "hibernate.hbm2ddl.auto";
		String VALIDATION_MODE = "javax.persistence.validation.mode";
		String MAPPER = "mapper";
		String MAPPER_TO_SCAN = "mapperToScan";
		String MAPPERS = "mappers";
		String ENTITY = "entity";
		String ENTITYS = "entitys";
		String ENTITY_TO_SCAN = "entityToScan";
		String RESOURCE = "resource";
		String PACKAGES_TO_SCAN = "packagesToScan";
		String HIBERNATE_PROPERTIES="hibernateProperties";
		String DATA_SOURCE="dataSource";
	}

	interface DynamicRoutingDataSourceConfigType {
		String DYANMIC_ROUTING_DATA_SOURCE = "dynamicRoutingDataSource";
		String ID = "id";
		String REF = "ref";
		String CLAZZ = "class";
		String HANDLER = "handler";
		String FILTER = "filter";
		String EXPRESSION = "expression";
		String DATA_SOURCES = "dataSources";
		String DATA_SOURCE = "dataSource";
	}

	interface FilterModelConfigType {
		String ID = "id";
		String EXPRESSION = "expression";
	}

	interface DataSourceConfigType {
		String PATTERN = "pattern";
		String DATA_SOURCE = "dataSource";
	}

	interface TransactionManagerConfigType {
		String ID = "id";
		String REF = "ref";
		String CLAZZ = "class";
		String TRANSACTION_MANAGER = "transactionManager";
		String DATA_SOURCE = "dataSource";
		String SESSION_FACTORY = "sessionFactory";
		String PATTERNS = "patterns";
		String PATTERN = "pattern";
		String ISOLATION = "isolation";
		String NO_ROLLBACK_FOR = "no-rollback-for";
		String ROLLBACK_FOR = "rollback-for";
		String TIMEOUT = "timeout";
		String READ_ONLY = "read-only";
		String PROPAGATION = "propagation";

	}

}
