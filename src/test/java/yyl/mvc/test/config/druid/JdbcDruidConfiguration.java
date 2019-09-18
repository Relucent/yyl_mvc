package yyl.mvc.test.config.druid;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

import yyl.mvc.common.jdbc.Dialect;
import yyl.mvc.common.jdbc.impl.Db2Dialect;
import yyl.mvc.common.jdbc.impl.MySqlDialect;
import yyl.mvc.common.jdbc.impl.OracleDialect;
import yyl.mvc.common.jdbc.impl.PostgreSqlDialect;

// @Configuration
public class JdbcDruidConfiguration {

    // ==============================Fields===========================================
    @Value("${jdbc.dialect}")
    private String jdbcDialect;
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;
    @Value("${jdbc.test-query}") // "SELECT 1"
    private String jdbcTestQuery;

    // ==============================Methods==========================================
    /**
     * 数据源配置, 使用C3P0数据库连接池
     */
    @Primary
    @Bean(destroyMethod = "close")
    public DataSource dataSource() throws PropertyVetoException {
        DruidDataSource dataSource = new DruidDataSource();

        // 数据库驱动
        dataSource.setDriverClassName(jdbcDriver);
        // 数据库连接
        dataSource.setUrl(jdbcUrl);
        // 数据库用户
        dataSource.setUsername(jdbcUsername);
        // 数据库密码
        dataSource.setPassword(jdbcPassword);

        // 初始化连接数量
        dataSource.setInitialSize(1);
        // 最小空闲连接数
        dataSource.setMinIdle(1);
        // 最大并发连接数
        dataSource.setMaxActive(20);
        // 获取连接等待超时的时间(毫秒)
        dataSource.setMaxWait(30 * 1000);
        // 检测间隔(检测需要关闭的空闲连接，单位是毫秒)
        dataSource.setTimeBetweenEvictionRunsMillis(60 * 1000);
        // 一个连接在池中最小生存的时间(单位是毫秒)
        dataSource.setMinEvictableIdleTimeMillis(300 * 1000);

        // 检测SQL
        dataSource.setValidationQuery(jdbcTestQuery);
        // 申请连接的时候检测连接是否有效
        dataSource.setTestWhileIdle(true);
        // 申请连接时执行validationQuery检测连接是否有效(该配置配置会降低性能)
        dataSource.setTestOnReturn(false);
        // 归还连接时执行validationQuery检测连接是否有效(该配置配置会降低性能)
        dataSource.setTestOnBorrow(false);

        // 超过时间限制是否回收
        dataSource.setRemoveAbandoned(true);
        // 超时时间(单位是秒)
        dataSource.setRemoveAbandonedTimeout(5 * 60);
        // 关闭Abandoned连接时是否输出错误日志
        dataSource.setLogAbandoned(true);
        return dataSource;
    }

    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        return new JdbcTemplate(dataSource());
    }

    /**
     * 数据源方言
     */
    @Primary
    @Bean
    public Dialect dialect() {
        try {
            if ("DB2".equalsIgnoreCase(jdbcDialect)) {
                return Db2Dialect.INSTANCE;
            } else if ("MYSQL".equalsIgnoreCase(jdbcDialect)) {
                return MySqlDialect.INSTANCE;
            } else if ("ORACLE".equalsIgnoreCase(jdbcDialect)) {
                return OracleDialect.INSTANCE;
            } else if ("POSTGRESQL".equalsIgnoreCase(jdbcDialect)) {
                return PostgreSqlDialect.INSTANCE;
            } else {
                return (Dialect) Class.forName(jdbcDialect).newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
