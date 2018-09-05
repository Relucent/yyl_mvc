package yyl.mvc.test.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfiguration {

    // @Autowired
    // private Environment environment;

    // @Bean
    // public DataSource dataSource() {
    // DruidDataSource dataSource = new DruidDataSource();
    // dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
    // dataSource.setUrl(environment.getProperty("spring.datasource.url"));
    // dataSource.setUsername(environment.getProperty("spring.datasource.username"));
    // dataSource.setPassword(environment.getProperty("spring.datasource.password"));
    // dataSource.setInitialSize(2);
    // dataSource.setMaxActive(20);
    // dataSource.setMinIdle(0);
    // dataSource.setMaxWait(60000);
    // dataSource.setValidationQuery("SELECT 1");
    // dataSource.setTestOnBorrow(false);
    // dataSource.setTestWhileIdle(true);
    // dataSource.setPoolPreparedStatements(false);
    // return dataSource;
    // }

    // @Bean
    // public JdbcTemplate jdbcTemplate() {
    // JdbcTemplate jdbcTemplate = new JdbcTemplate();
    // jdbcTemplate.setDataSource(dataSource());
    // return jdbcTemplate;
    // }
}
