package yyl.mvc.core.plug.jdbc.dialect.impl;

import yyl.mvc.core.plug.jdbc.dialect.Dialect;

/**
 * JDBC查询方言HSQLDB实现，主要用于提供分页查询<br>
 */
public class HsqldbDialect implements Dialect {

	public static final HsqldbDialect INSTANCE = new HsqldbDialect();

	@Override
	public String getCountSql(String sql) {
		return "select count(*) as COUNT___y from (" + sql + ") TEMP___TABLE";
	}

	@Override
	public String getLimitSql(String sql, int start, int limit) {
		return sql + " LIMIT " + start + " OFFSET " + limit;
	}

	@Override
	public String testQuery() {
		return "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS";
	}
}
