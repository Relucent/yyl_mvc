package yyl.mvc.core.plug.jdbc.dialect.impl;

import yyl.mvc.core.plug.jdbc.dialect.Dialect;

/**
 * JDBC查询方言Oracle实现，主要用于提供分页查询<br>
 */
public class OracleDialect implements Dialect {
	@Override
	public String getCountSql(String sql) {
		return "select count(*) as COUNT___y from (" + sql + ") T___T";
	}

	@Override
	public String getLimitSql(String sql, int start, int limit) {
		return " SELECT X___X.* FROM  ( SELECT ROWNUM as ROWNO___y, T__T.* FROM (" + sql + ") T__T WHERE ROWNUM<=" + (start + limit)
				+ " ) X___X WHERE X___X.ROWNO___y > " + start + " ";
	}

	// @Override
	// public String getTableExistsSql(String table) {
	// return "SELECT COUNT(*) AS count_ FROM USER_TABLES WHERE TABLE_NAME = '" + table + "'";
	// }
}
