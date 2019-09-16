package yyl.mvc.util.jdbc.impl;

import yyl.mvc.util.jdbc.Dialect;

/**
 * JDBC查询方言Oracle实现，主要用于提供分页查询<br>
 */
public class OracleDialect implements Dialect {

    public static final OracleDialect INSTANCE = new OracleDialect();

    @Override
    public String getCountSql(String sql) {
        return "select count(*) as COUNT___y from (" + sql + ") T___T";
    }

    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        return " SELECT X___X.* FROM  ( " //
                + " SELECT ROWNUM as ROWNO___y, T__T.* FROM (" + sql + ") T__T  WHERE ROWNUM <=" + (offset + limit)
                + " ) X___X " //
                + " WHERE X___X.ROWNO___y > " + offset + " ";
    }

    @Override
    public String testQuery() {
        return "select 1 from dual";
    }
}
