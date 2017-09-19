package yyl.mvc.test.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("exampleJobTrigger") Trigger cronJobTrigger) throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		// this allows to update triggers in DB when updating settings in config file:  
		//用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了  
		factory.setOverwriteExistingJobs(true);

		//用于quartz集群,加载quartz数据源  
		//factory.setDataSource(dataSource);    

		//QuartzScheduler 延时启动，应用启动完10秒后 QuartzScheduler 再启动  
		factory.setStartupDelay(10);

		//配置quart参数
		factory.setQuartzProperties(quartzProperties());
		//直接使用配置文件的方式
		//factory.setConfigLocation(new FileSystemResource(this.getClass().getResource("/quartz.properties").getPath()));

		factory.setAutoStartup(true);
		factory.setApplicationContextSchedulerContextKey("applicationContext");

		//注册触发器  
		factory.setTriggers(cronJobTrigger);
		return factory;

	}

	private Properties quartzProperties() throws IOException {

		String instanceName = environment.getProperty("quartz.scheduler.instanceName", "MyScheduler");
		String myDSDriver = environment.getProperty("org.quartz.dataSource.myDS.driver", "org.postgresql.Driver");
		String myDSURL = environment.getProperty("org.quartz.dataSource.myDS.URL", "jdbc:postgresql://localhost/dev");
		String myDSUser = environment.getProperty("org.quartz.dataSource.myDS.user", "my");
		String myDSPassword = environment.getProperty("org.quartz.dataSource.myDS.password", "password");
		String myDSMaxConnections = environment.getProperty("org.quartz.dataSource.myDS.maxConnections", "5");

		Properties prop = new Properties();
		prop.put("quartz.scheduler.instanceName", instanceName);
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
		prop.put("org.quartz.scheduler.jmx.export", "true");
		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
		prop.put("org.quartz.jobStore.isClustered", "true");
		prop.put("org.quartz.jobStore.clusterCheckinInterval", "20000");
		prop.put("org.quartz.jobStore.dataSource", "myDS");
		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
		prop.put("org.quartz.jobStore.misfireThreshold", "120000");
		prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");
		prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE");
		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		prop.put("org.quartz.threadPool.threadCount", "10");
		prop.put("org.quartz.threadPool.threadPriority", "5");
		prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
		prop.put("org.quartz.dataSource.myDS.driver", myDSDriver);
		prop.put("org.quartz.dataSource.myDS.URL", myDSURL);
		prop.put("org.quartz.dataSource.myDS.user", myDSUser);
		prop.put("org.quartz.dataSource.myDS.password", myDSPassword);
		prop.put("org.quartz.dataSource.myDS.maxConnections", myDSMaxConnections);
		prop.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
		prop.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
		prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");
		return prop;
	}

	@Bean(name = "exampleJobTrigger")
	public CronTriggerFactoryBean exampleJobTrigger(@Qualifier("exampleJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
		factoryBean.setJobDetail(jobDetail);
		factoryBean.setCronExpression("0/5 * * * * ?");//每5秒一次调度 
		return factoryBean;
	}

	@Bean

	public JobDetailFactoryBean exampleJobDetail() {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(ExampleQuartzJobBean.class);
		factoryBean.setDurability(true);
		factoryBean.setRequestsRecovery(true);
		factoryBean.setGroup("exampleGroup");
		Map<String, String> map = new HashMap<>();
		map.put("value", "hello job!");
		factoryBean.setJobDataAsMap(map);
		return factoryBean;
	}

	/** @see JobDetailFactoryBean */
	public static class ExampleQuartzJobBean extends QuartzJobBean {

		private ApplicationContext applicationContext;
		private String value;

		@Override
		protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
			System.out.println(applicationContext);
			System.out.println(value);
			System.out.println(context.getMergedJobDataMap());
		}

		/**
		 * 注入Spring ApplicationContext (由QuartzJobBean中的BeanWrapper实现)<br>
		 * 方法名称和 SchedulerFactoryBean.setApplicationContextSchedulerContextKey传入参数对应<br>
		 */
		public void setApplicationContext(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		/**
		 * 注入 value
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}

}
