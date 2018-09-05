package yyl.mvc.test.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import yyl.mvc.plug.hibernate.HibernateGenericDao;
import yyl.mvc.plug.hibernate.session.DefaultHibernateSessionProviderImpl;
import yyl.mvc.plug.hibernate.session.HibernateSessionProvider;

/**
 * _Hibernate配置项
 */
@Configuration
public class HibernateConfiguration {

	@Autowired
	private Environment environment;

	/** HibernateSession工厂 */
	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		hibernateProperties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.put("hibernate.jdbc.fetch_size", environment.getProperty("hibernate.jdbc.fetch_size"));
		hibernateProperties.put("hibernate.jdbc.batch_size", environment.getProperty("hibernate.jdbc.batch_size"));
		hibernateProperties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		hibernateProperties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
		hibernateProperties.put("hibernate.connection.SetBigStringTryClob", environment.getProperty("hibernate.connection.SetBigStringTryClob"));
		hibernateProperties.put("current_session_context_class", "thread");
		factoryBean.setHibernateProperties(hibernateProperties);
		return factoryBean;
	}

	/** 配置事务管理器 | 注:这里的dataSource和SqlSessionFactoryBean的dataSource要一致，不然事务不起作用 */
	@Bean
	public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
		HibernateTransactionManager manager = new HibernateTransactionManager();
		manager.setSessionFactory(sessionFactory);
		manager.setGlobalRollbackOnParticipationFailure(false);
		return manager;
	}

	/** 为DAO的 HibernateSession提供者 */
	@Bean
	public HibernateSessionProvider hibernateSessionProvider(SessionFactory sessionFactory) {
		DefaultHibernateSessionProviderImpl provider = new DefaultHibernateSessionProviderImpl();
		provider.setSessionFactory(sessionFactory);
		return provider;
	}

	/** 通用HibernateDao */
	@Bean
	public HibernateGenericDao hibernateGenericDao() {
		return new HibernateGenericDao();
	}
}
