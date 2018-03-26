package com.cheuks.bin.original.anything.test.sql.parser.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Deprecated
public enum DefaultKeyword implements Keyword {

		DML, // CREATE,UPDATE....
		DDL,
		DCL, // GRANT

		/***
		 * SHOW DATABASES;
		 * <p>
		 * SHOW TABLES;
		 * 
		 */
		SHOW,
		/***
		 * CREATE DATABASE dbname;
		 * <p>
		 * CREATE INDEX index_name ON table_name (column_list)
		 * <p>
		 * CREATE UNIQUE INDEX index_name ON table_name (column_list)
		 */
		CREATE,

		// DML
		SELECT,
		INSERT,
		UPDATE,
		DELETE,
		WITH,

		// DDL
		SCHEMA,
		EXPLAIN,
		TABLE,
		/***
		 * DROP DATABASE dbname;
		 * <p>
		 * DROP TABLE dbname;
		 * <p>
		 * DROP INDEX index_name ON talbe_name
		 */
		DROP,
		/***
		 * TRUNCATE TABLE name
		 */
		TRUNCATE,
		/***
		 * ALTER TABLE tablename MODIFY name CHAR(128) NOT NULL;
		 * <P>
		 * ALTER TABLE tablename ADD age INT(11) NOT NULL;
		 * <P>
		 * ALTER TABLE tablename DROP age;
		 * <P>
		 * ALTER TABLE tablename CHANGE name uname CHAR(128);
		 * <P>
		 * ALTER TABLE tablename RENAME test; *
		 * <p>
		 * ALTER TABLE table_name DROP INDEX index_name
		 * <p>
		 * ALTER TABLE table_name DROP PRIMARY KEY
		 */
		ALTER,
		/***
		 * USE dbname;
		 */
		USE,
		/***
		 * DESC tablename;
		 */
		DESC,
		/***
		 * 锁表
		 * <p>
		 * FLUSH TABLES WITH READ LOCK;
		 */
		FLUSH,
		LOCK,
		/***
		 * 解锁
		 * <p>
		 * UNLOCK TABLES;
		 */
		UNLOCK,

		// DCL

		/***
		 * 添加权限
		 * <p>
		 * grant all on 数据库名.* to 用户名@IP;
		 * <p>
		 * grant 权限1,权限2,........,权限n on 数据库名.* to 用户名@IP;
		 * 
		 */
		GRANT,
		/***
		 * 撤消权限
		 * <p>
		 * revoke 权限1,权限2,........,权限n on 数据库名.* from 用户名@IP;
		 */
		DENY,
		REVOKE,

		/*
		 * Common
		 */
		DATABASE,
		COLUMN,
		VIEW,
		INDEX,
		TRIGGER,
		PROCEDURE,
		TABLESPACE,
		FUNCTION,
		SEQUENCE,
		CURSOR,
		TO,
		OF,
		ON,
		FOR,
		WHILE,
		DO,
		NO,
		BY,
		WITHOUT,
		TRUE,
		FALSE,
		TEMPORARY,
		TEMP,
		COMMENT,

		/*
		 * Create
		 */
		REPLACE,
		BEFORE,
		AFTER,
		INSTEAD,
		EACH,
		ROW,
		STATEMENT,
		EXECUTE,
		BITMAP,
		NOSORT,
		REVERSE,
		COMPILE,

		/*
		 * Alter
		 */
		MODIFY,
		RENAME,
		ENABLE,
		DISABLE,
		VALIDATE,
		USER,
		IDENTIFIED,

		/* ############ keyword ############ */
		DISTINCT,
		ADD,
		SET,
		WHERE,
		FROM,
		INTO,
		VALUES,
		AS,
		CASE,
		WHEN,
		IF,
		ELSE,
		LEFT,
		RIGHT,
		FULL,
		INNER,
		OUTER,
		CROSS,
		JOIN,
		USING,
		NATURAL,
		ORDER,
		ASC,
		GROUP,
		HAVING,
		UNION,

		DECLARE,
		FETCH,
		CLOSE,

		CAST,
		NEW,
		ESCAPE,
		SOME,
		LEAVE,
		ITERATE,
		REPEAT,
		UNTIL,
		OPEN,
		OUT,
		INOUT,
		OVER,
		ADVISE,
		SIBLINGS,
		LOOP,
		DEFAULT,
		EXCEPT,
		INTERSECT,
		MINUS,
		PASSWORD,
		LOCAL,
		GLOBAL,
		STORAGE,
		DATA,
		COALESCE,

		/*
		 * Types
		 */
		CHAR,
		CHARACTER,
		VARYING,
		VARCHAR,
		VARCHAR2,
		INTEGER,
		INT,
		SMALLINT,
		DECIMAL,
		DEC,
		NUMERIC,
		FLOAT,
		REAL,
		DOUBLE,
		PRECISION,
		DATE,
		TIME,
		INTERVAL,
		BOOLEAN,
		BLOB,

		/*
		 * Conditionals
		 */
		AND,
		OR,
		XOR,
		IS,
		NOT,
		NULL,
		IN,
		BETWEEN,
		LIKE,
		ANY,
		ALL,
		EXISTS,

		/*
		 * Functions
		 */
		AVG,
		MAX,
		MIN,
		SUM,
		COUNT,
		GREATEST,
		LEAST,
		ROUND,
		TRUNC,
		POSITION,
		EXTRACT,
		LENGTH,
		CHAR_LENGTH,
		SUBSTRING,
		SUBSTR,
		INSTR,
		INITCAP,
		UPPER,
		LOWER,
		TRIM,
		LTRIM,
		RTRIM,
		BOTH,
		LEADING,
		TRAILING,
		TRANSLATE,
		CONVERT,
		LPAD,
		RPAD,
		DECODE,
		NVL,

		/*
		 * Constraints
		 */
		CONSTRAINT,
		UNIQUE,
		PRIMARY,
		FOREIGN,
		KEY,
		CHECK,
		REFERENCES;

	static final Map<String, DefaultKeyword> values = new ConcurrentSkipListMap<String, DefaultKeyword>();
	static {
		for (DefaultKeyword word : DefaultKeyword.values())
			values.put(word.toString(), word);
	}
	public static DefaultKeyword converyType(final String keyword) {
		return values.get(keyword);
	}
}
