package yyl.mvc.plug.spring.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderSupport;

import com.google.common.collect.Lists;

public class ApplicationPropertiesFactoryBean extends PropertiesLoaderSupport implements FactoryBean<Properties>,
		InitializingBean {

	// ==============================Fields===========================================
	private static Logger logger = LoggerFactory.getLogger(ApplicationPropertiesFactoryBean.class);
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private String propertiesListLocation = "classpath:/properties.lst";
	private Properties properties;

	// ==============================Constructors=======================================
	// ...

	// ==============================Methods==========================================
	public void afterPropertiesSet() throws IOException {
		// 判断加载哪些properties
		this.readProperties();
		// 默认设置找不到资源也不会报错
		this.setIgnoreResourceNotFound(true);
		// 加载properties
		this.properties = mergeProperties();
	}

	protected void readProperties() throws IOException {
		List<Resource> resources = Lists.newArrayList();
		Resource propertiesListResource = resourceLoader.getResource(propertiesListLocation);
		if (!propertiesListResource.exists()) {
			logger.info("use default properties");
			resources.add(resourceLoader.getResource("classpath:/application.properties"));
		} else {
			InputStream input = null;
			try {
				for (String line : IOUtils.readLines(input = propertiesListResource.getInputStream(), "utf-8")) {
					line = StringUtils.trim(line);
					if (StringUtils.isNoneEmpty(line)) {
						resources.add(resourceLoader.getResource(line));
					}
				}
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		setLocations(resources.toArray(new Resource[0]));
	}

	public Properties getObject() throws IOException {
		return properties;
	}

	public Class<Properties> getObjectType() {
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	// ==============================IOC_Methods=======================================
	public String getPropertiesListLocation() {
		return propertiesListLocation;
	}

	public void setPropertiesListLocation(String propertiesListLocation) {
		this.propertiesListLocation = propertiesListLocation;
	}
}
