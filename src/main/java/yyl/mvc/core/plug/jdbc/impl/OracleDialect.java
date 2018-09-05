package yyl.mvc.core.plug.jdbc.impl;

import yyl.mvc.core.plug.jdbc.Dialect;

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
    public String getLimitSql(String sql, int start, int limit) {
        return " SELECT X___X.* FROM  ( SELECT ROWNUM as ROWNO___y, T__T.* FROM (" + sql + ") T__T WHERE ROWNUM<="
                + (start + limit) + " ) X___X WHERE X___X.ROWNO___y > " + start + " ";
    }

    @Override
    public String testQuery() {
        return "select 1 from dual";
    }
}
