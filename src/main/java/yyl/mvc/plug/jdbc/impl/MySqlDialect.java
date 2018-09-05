package yyl.mvc.plug.jdbc.impl;

import yyl.mvc.plug.jdbc.Dialect;

/**
 * JDBC查询方言mysql实现，主要用于提供分页查询<br>
 */
public class MySqlDialect implements Dialect {

    public static final MySqlDialect INSTANCE = new MySqlDialect();

    @Override
    public String getCountSql(String sql) {
        return "select count(*) as COUNT___y from (" + sql + ") T___T";
    }

    @Override
    public String getLimitSql(String sql, int start, int limit) {
        return sql + " limit " + start + "," + limit;
    }

    @Override
    public String testQuery() {
        return "select 1";
    }
}
