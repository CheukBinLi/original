package com.cheuks.bin.original.anything.test.sql.parser.constant;

public interface Keyword {

	String DML = "DML"; // CREATE,UPDATE....
	String DDL = "DDL";
	String DCL = "DCL"; // GRANT

	/***
	 * SHOW DATABASES;
	 * <p>
	 * SHOW TABLES;
	 * 
	 */
	String SHOW = "SHOW";
	/***
	 * CREATE DATABASE dbname;
	 * <p>
	 * CREATE INDEX index_name ON table_name (column_list)
	 * <p>
	 * CREATE UNIQUE INDEX index_name ON table_name (column_list)
	 */
	String CREATE = "CREATE";

	// DML
	String SELECT = "SELECT";
	String INSERT = "INSERT";
	String UPDATE = "UPDATE";
	String DELETE = "DELETE";
	String WITH = "WITH";

	// DDL
	String SCHEMA = "SCHEMA";
	String EXPLAIN = "EXPLAIN";
	String TABLE = "TABLE";
	/***
	 * DROP DATABASE dbname;
	 * <p>
	 * DROP TABLE dbname;
	 * <p>
	 * DROP INDEX index_name ON talbe_name
	 */
	String DROP = "DROP";
	/***
	 * TRUNCATE TABLE name
	 */
	String TRUNCATE = "TRUNCATE";
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
	String ALTER = "ALTER";
	/***
	 * USE dbname;
	 */
	String USE = "USE";
	/***
	 * DESC tablename;
	 */
	String DESC = "DESC";
	/***
	 * 锁表
	 * <p>
	 * FLUSH TABLES WITH READ LOCK;
	 */
	String FLUSH = "FLUSH";
	String LOCK = "LOCK";
	/***
	 * 解锁
	 * <p>
	 * UNLOCK TABLES;
	 */
	String UNLOCK = "UNLOCK";

	// DCL

	/***
	 * 添加权限
	 * <p>
	 * grant all on 数据库名.* to 用户名@IP;
	 * <p>
	 * grant 权限1,权限2,........,权限n on 数据库名.* to 用户名@IP;
	 * 
	 */
	String GRANT = "GRANT";
	/***
	 * 撤消权限
	 * <p>
	 * revoke 权限1,权限2,........,权限n on 数据库名.* from 用户名@IP;
	 */
	String DENY = "DENY";
	String REVOKE = "REVOKE";

	/*
	 * Common
	 */
	String DATABASE = "DATABASE";
	String COLUMN = "COLUMN";
	String VIEW = "VIEW";
	String INDEX = "INDEX";
	String TRIGGER = "TRIGGER";
	String PROCEDURE = "PROCEDURE";
	String TABLESPACE = "TABLESPACE";
	String FUNCTION = "FUNCTION";
	String SEQUENCE = "SEQUENCE";
	String CURSOR = "CURSOR";
	String TO = "TO";
	String OF = "OF";
	String ON = "ON";
	String FOR = "FOR";
	String WHILE = "WHILE";
	String DO = "DO";
	String NO = "NO";
	String BY = "BY";
	String WITHOUT = "WITHOUT";
	String TRUE = "TRUE";
	String FALSE = "FALSE";
	String TEMPORARY = "TEMPORARY";
	String TEMP = "TEMP";
	String COMMENT = "COMMENT";

	/*
	 * Create
	 */
	String REPLACE = "REPLACE";
	String BEFORE = "BEFORE";
	String AFTER = "AFTER";
	String INSTEAD = "INSTEAD";
	String EACH = "EACH";
	String ROW = "ROW";
	String STATEMENT = "STATEMENT";
	String EXECUTE = "EXECUTE";
	String BITMAP = "BITMAP";
	String NOSORT = "NOSORT";
	String REVERSE = "REVERSE";
	String COMPILE = "COMPILE";

	/*
	 * Alter
	 */
	String MODIFY = "MODIFY";
	String RENAME = "RENAME";
	String ENABLE = "ENABLE";
	String DISABLE = "DISABLE";
	String VALIDATE = "VALIDATE";
	String USER = "USER";
	String IDENTIFIED = "IDENTIFIED";

	/* ############ keyword ############ */
	String DISTINCT = "DISTINCT";
	String ADD = "ADD";
	String SET = "SET";
	String WHERE = "WHERE";
	String FROM = "FROM";
	String INTO = "INTO";
	String VALUES = "VALUES";
	String AS = "AS";
	String CASE = "CASE";
	String WHEN = "WHEN";
	String IF = "IF";
	String ELSE = "ELSE";
	String LEFT = "LEFT";
	String RIGHT = "RIGHT";
	String FULL = "FULL";
	String INNER = "INNER";
	String OUTER = "OUTER";
	String CROSS = "CROSS";
	String JOIN = "JOIN";
	String USING = "USING";
	String NATURAL = "NATURAL";
	String ORDER = "ORDER";
	String ASC = "ASC";
	String GROUP = "GROUP";
	String HAVING = "HAVING";
	String UNION = "UNION";

	String DECLARE = "DECLARE";
	String FETCH = "FETCH";
	String CLOSE = "CLOSE";

	String CAST = "CAST";
	String NEW = "NEW";
	String ESCAPE = "ESCAPE";
	String SOME = "SOME";
	String LEAVE = "LEAVE";
	String ITERATE = "ITERATE";
	String REPEAT = "REPEAT";
	String UNTIL = "UNTIL";
	String OPEN = "OPEN";
	String OUT = "OUT";
	String INOUT = "INOUT";
	String OVER = "OVER";
	String ADVISE = "ADVISE";
	String SIBLINGS = "SIBLINGS";
	String LOOP = "LOOP";
	String DEFAULT = "DEFAULT";
	String EXCEPT = "EXCEPT";
	String INTERSECT = "INTERSECT";
	String MINUS = "MINUS";
	String PASSWORD = "PASSWORD";
	String LOCAL = "LOCAL";
	String GLOBAL = "GLOBAL";
	String STORAGE = "STORAGE";
	String DATA = "DATA";
	String COALESCE = "COALESCE";

	/*
	 * Types
	 */
	String CHAR = "CHAR";
	String CHARACTER = "CHARACTER";
	String VARYING = "VARYING";
	String VARCHAR = "VARCHAR";
	String VARCHAR2 = "VARCHAR2";
	String INTEGER = "INTEGER";
	String INT = "INT";
	String SMALLINT = "SMALLINT";
	String DECIMAL = "DECIMAL";
	String DEC = "DEC";
	String NUMERIC = "NUMERIC";
	String FLOAT = "FLOAT";
	String REAL = "REAL";
	String DOUBLE = "DOUBLE";
	String PRECISION = "PRECISION";
	String DATE = "DATE";
	String TIME = "TIME";
	String INTERVAL = "INTERVAL";
	String BOOLEAN = "BOOLEAN";
	String BLOB = "BLOB";

	/*
	 * Conditionals
	 */
	String AND = "AND";
	String OR = "OR";
	String XOR = "XOR";
	String IS = "IS";
	String NOT = "NOT";
	String NULL = "NULL";
	String IN = "IN";
	String BETWEEN = "BETWEEN";
	String LIKE = "LIKE";
	String ANY = "ANY";
	String ALL = "ALL";
	String EXISTS = "EXISTS";

	/*
	 * Functions
	 */
	String AVG = "AVG";
	String MAX = "MAX";
	String MIN = "MIN";
	String SUM = "SUM";
	String COUNT = "COUNT";
	String GREATEST = "GREATEST";
	String LEAST = "LEAST";
	String ROUND = "ROUND";
	String TRUNC = "TRUNC";
	String POSITION = "POSITION";
	String EXTRACT = "EXTRACT";
	String LENGTH = "LENGTH";
	String CHAR_LENGTH = "CHAR_LENGTH";
	String SUBSTRING = "SUBSTRING";
	String SUBSTR = "SUBSTR";
	String INSTR = "INSTR";
	String INITCAP = "INITCAP";
	String UPPER = "UPPER";
	String LOWER = "LOWER";
	String TRIM = "TRIM";
	String LTRIM = "LTRIM";
	String RTRIM = "RTRIM";
	String BOTH = "BOTH";
	String LEADING = "LEADING";
	String TRAILING = "TRAILING";
	String TRANSLATE = "TRANSLATE";
	String CONVERT = "CONVERT";
	String LPAD = "LPAD";
	String RPAD = "RPAD";
	String DECODE = "DECODE";
	String NVL = "NVL";

	/*
	 * Constraints
	 */
	String CONSTRAINT = "CONSTRAINT";
	String UNIQUE = "UNIQUE";
	String PRIMARY = "PRIMARY";
	String FOREIGN = "FOREIGN";
	String KEY = "KEY";
	String CHECK = "CHECK";
	String REFERENCES = "REFERENCES";

	String LEFT_BRACKETS = "(";
	String RIGHT_BRACKETS = ")";

}
