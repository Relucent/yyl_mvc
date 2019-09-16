package yyl.mvc.util.jdbc.impl;

import yyl.mvc.util.jdbc.Dialect;

/**
 * JDBC查询方言GreenPlum实现，主要用于提供分页查询<br>
 */
public class GreenPlumDialect implements Dialect {

    public static final GreenPlumDialect INSTANCE = new GreenPlumDialect();

    @Override
    public String getCountSql(String sql) {
        return "select count(*) as COUNT___y from (" + sql + ") T___T";
    }

    @Override
    public String getLimitSql(String sql, int offset, int limit) {
        return sql + " limit " + limit + " offset " + offset;
    }

    @Override
    public String testQuery() {
        return "select version()";
    }
}
