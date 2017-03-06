package yyl.mvc.core.plug.spring.jdbc;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Lists;

import yyl.mvc.core.plug.jdbc.dialect.Dialect;
import yyl.mvc.core.plug.jdbc.dialect.DialectConfigurer;
import yyl.mvc.core.util.page.Page;
import yyl.mvc.core.util.page.impl.SimplePage;

/**
 * 基于JDBC的数据访问层支持工具类.
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
		return jdbcTemplate.queryForObject("select count(1) from dual where exists( " + sql + " )", args, Long.class).longValue() > 0L;
	}

	/**
	 * 分页查询
	 * @param sql 查询语句
	 * @param args 查询参数
	 * @param start 第一条记录索引
	 * @param limit 每页显示记录数
	 * @param rowMapper 行映射
	 * @param jdbcTemplate JDBC模板
	 * @return 分页查询結果
	 */
	public static <T> Page<T> pagedQuery(String sql, Object[] args, int start, int limit, RowMapper<T> rowMapper, JdbcTemplate jdbcTemplate) {
		int count = jdbcTemplate.queryForObject(getDialect().getCountSql(sql), args, Long.class).intValue();
		if (count == 0) {
			return new SimplePage<T>(start, limit, Lists.<T> newArrayList(), 0);
		}
		List<T> records = jdbcTemplate.query(getDialect().getLimitSql(sql, start, limit), args, rowMapper);
		return new SimplePage<T>(start, limit, records, count);
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

	// ==============================Tools_Methods====================================
	public static <T> T getSingleResult(List<T> multipleResult) {
		return (multipleResult == null || multipleResult.isEmpty()) ? null : multipleResult.get(0);
	}

	public static Object[] toArray(List<?> list) {
		return (list == null || list.isEmpty()) ? new Object[0] : list.toArray(new Object[list.size()]);
	}

	private static Dialect getDialect() {
		return DialectConfigurer.getDialect();
	}
}
