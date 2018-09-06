package yyl.mvc.plug.jdbc;

/**
 * JDBC查询方言接口，主要用于提供分页查询<br>
 * @author YYL
 */
public interface Dialect {

    /**
     * 获得分页SQL</br>
     * @param sql 原始SQL
     * @param offset 查詢记录的偏移量
     * @param limit 每页查询的最大数量
     * @return 分页SQL
     */
    String getLimitSql(String sql, int offset, int limit);

    /**
     * 将SQL转换为总记录数SQL
     * @param sql SQL语句
     * @return 总记录数的SQL
     */
    String getCountSql(String sql);


    /**
     * 获得测试用的SQL
     * @return 测试SQL
     */
    String testQuery();
}
