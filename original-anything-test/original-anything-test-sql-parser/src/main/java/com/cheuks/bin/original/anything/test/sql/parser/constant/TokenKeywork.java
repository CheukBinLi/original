package com.cheuks.bin.original.anything.test.sql.parser.constant;

@Deprecated
public enum TokenKeywork {

		// first token
		SELECT("SELECT"),
		INSERT("INSERT"),
		UPDATE("UPDATE"),
		DELETE("DELETE"),
		EXPLAIN("EXPLAIN"),
		UNLOCK("UNLOCK"),
		LOCK("LOCK"),
		WITH("WITH"),
		SHOW("SHOW"),
		DROP("DROP"),

		// type

		SCHEMA("SCHEMA"),
		TRIGGER("TRIGGER"),
		TABLE("TABLE"),
		FUNCTION("FUNCTION"),
		INDEX("INDEX"),
		PROC("PROC"),
		PROCEDURE("PROCEDURE"),
		DATABASE("DATABASE"),

		USING("USING"),
		INTO("INTO"),
		IN("IN"),
		IF("IF"),
		STRAIGHT_JOIN("STRAIGHT_JOIN"),
		OUT("OUT"),
		OUTER("OUTER"),
		JOIN("JOIN"),
		INNER("INNER"),
		INOUT("INOUT"),
		WHERE("WHERE"),
		VALUES("VALUES"),
		USE("USE"),
		TRUE("TRUE"),
		LEFT("LEFT"),
		RIGHT("RIGHT"),

		DISTINCT("DISTINCT"),
		DISTINCTROW("DISTINCTROW"),
		LOW_PRIORITY("LOW_PRIORITY"),
		HIGH_PRIORITY("HIGH_PRIORITY"),
		SQL_BUFFER_RESULT("SQL_BUFFER_RESULT"),
		SQL_SMALL_RESULT("SQL_SMALL_RESULT"),
		SQL_BIG_RESULT("SQL_BIG_RESULT"),
		SQL_CALC_FOUND_ROWS("SQL_CALC_FOUND_ROWS"),

		SET("SET"),
		AS("AS"),
		SEPARATOR("SEPARATOR"), // GROUP_CONCAT(USERID order by USERID separator ';') userId,
		RETURN("RETURN"),
		RESTRICT("RESTRICT"),
		REPEAT("REPEAT"),
		REPLACE("REPLACE"),
		REFERENCES("REFERENCES"),
		PURGE("PURGE"),
		PRIMARY("PRIMARY"),
		PRECISION("PRECISION"),
		ORDER("ORDER"),
		OR("OR"),
		OPTIMIZE("OPTIMIZE"),
		ON("ON"),
		NULL("NULL"),
		NOT("NOT"),
		MOD("MOD"),
		MATCH("MATCH"),
		LOOP("LOOP"),
		FALSE("FALSE"),
		ELSEIF("ELSEIF"),
		ELSE("ELSE"),
		DUAL("DUAL"),
		DEFAULT("DEFAULT"),
		DECLARE("DECLARE"),
		CURSOR("CURSOR"),
		CROSS("CROSS"),
		CONTINUE("CONTINUE"),
		CONSTRAINT("CONSTRAINT"),
		CONDITION("CONDITION"),
		COLUMN("COLUMN"),
		COLLATE("COLLATE"),
		CHECK("CHECK"),
		CASE("CASE"),
		CASCADE("CASCADE"),
		BY("BY"),

		EXISTS("EXISTS"),
		EXIT("EXIT"),
		FETCH("FETCH"),
		FOR("FOR"),
		FORCE("FORCE"),
		FOREIGN("FOREIGN"),
		FROM("FROM"),
		HAVING("HAVING"),
		GOTO("GOTO"),
		GROUP("GROUP"),
		DESC("DESC"),
		ASC("ASC"),
		IS("IS"),
		LIKE("LIKE"),
		BETWEEN("BETWEEN"),
		LIMIT("LIMIT"),
		RLIKE("RLIKE"),
		AND("AND"),
		ANALYZE("ANALYZE"),
		ALTER("ALTER"),
		ALL("ALL"),
		ADD("ADD"),;

	public final String name;

	TokenKeywork() {
		this(null);
	}

	TokenKeywork(String name) {
		this.name = name;
	}
}
