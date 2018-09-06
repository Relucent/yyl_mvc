package yyl.mvc.plug.spring.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.google.common.collect.Lists;

import yyl.mvc.plug.jdbc.Dialect;
import yyl.mvc.util.page.Page;
import yyl.mvc.util.page.Pagination;


/**
 * 基于JDBC的数据访问层支持工具类.<br>
 * @author _yyl
 */
public class JdbcDaoHelper {

    /**
     * 判断数据是否存在
     * @param sql 查询语句
     * @param args 查询参数
     * @param jdbcTemplate JDBC模板
     * @return 有一条数据则返回true,否则返回false.
     */
    // select '1' from dual where exists( select '1' from {table} )
    public static boolean exists(String sql, Object[] args, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("select count(1) from dual where exists( " + sql + " )", args, Long.class)
                .longValue() > 0L;
    }

    /**
     * 插入数据并返回主键
     * @param sql SQL语句
     * @param args 参数
     * @param jdbcTemplate JDBC模板
     * @return 主键
     */
    public static Long insertAndGeneratedKey(String sql, Object[] args, JdbcTemplate jdbcTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                int i = 0;
                for (Object arg : args) {
                    ps.setObject(++i, arg);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * 查询单个记录
     * @param sql 查询单个数据
     * @param args 查询参数
     * @param rowMapper 行映射
     * @param jdbcTemplate JDBC模板
     * @return 分页查询結果
     */
    public static <T> T queryOne(String sql, Object[] args, RowMapper<T> rowMapper, JdbcTemplate jdbcTemplate) {
        return getSingleResult(jdbcTemplate.query(sql, args, rowMapper));
    }


    /**
     * 分页查询
     * @param sql 查询语句
     * @param args 查询参数
     * @param offset 第一条记录索引
     * @param limit 每页显示记录数
     * @param rowMapper 行映射
     * @param jdbcTemplate JDBC模板
     * @param dialect 数据方言
     * @return 分页查询結果
     */
    public static <T> Page<T> pagedQuery(String sql, Object[] args, int offset, int limit, RowMapper<T> rowMapper,
            JdbcTemplate jdbcTemplate, Dialect dialect) {
        int count = jdbcTemplate.queryForObject(dialect.getCountSql(sql), args, Long.class).intValue();
        if (count == 0) {
            return new Page<T>(offset, limit, Lists.<T>newArrayList(), 0);
        }
        List<T> records = jdbcTemplate.query(dialect.getLimitSql(sql, offset, limit), args, rowMapper);
        return new Page<T>(offset, limit, records, count);
    }

    /**
     * 分页查询
     * @param sql 查询语句
     * @param args 查询参数
     * @param start 第一条记录索引
     * @param limit 每页显示记录数
     * @param rowMapper 行映射
     * @param jdbcTemplate JDBC模板
     * @param dialect 方言
     * @return 分页查询結果
     */
    public static <T> Page<T> pagedQuery(CharSequence sql, List<Object> args, int start, int limit,
            RowMapper<T> rowMapper, JdbcTemplate jdbcTemplate, Dialect dialect) {
        return pagedQuery(sql.toString(), args.toArray(), start, limit, rowMapper, jdbcTemplate, dialect);
    }

    /**
     * 分页查询
     * @param sql 查询语句
     * @param args 查询参数
     * @param pagination 分页限制
     * @param limit 每页显示记录数
     * @param rowMapper 行映射
     * @param jdbcTemplate JDBC模板
     * @param dialect 方言
     * @return 分页查询結果
     */
    public static <T> Page<T> pagedQuery(CharSequence sql, List<Object> args, Pagination pagination,
            RowMapper<T> rowMapper, JdbcTemplate jdbcTemplate, Dialect dialect) {
        return pagedQuery(sql.toString(), args.toArray(), pagination.getOffset(), pagination.getLimit(), rowMapper,
                jdbcTemplate, dialect);
    }

    // ==============================Tools_Methods====================================
    public static <T> T getSingleResult(List<T> multipleResult) {
        return (multipleResult == null || multipleResult.isEmpty()) ? null : multipleResult.get(0);
    }

    public static Object[] toArray(List<?> list) {
        return (list == null || list.isEmpty()) ? new Object[0] : list.toArray(new Object[list.size()]);
    }
}
