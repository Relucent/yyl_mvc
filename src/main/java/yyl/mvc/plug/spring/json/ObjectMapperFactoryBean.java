package yyl.mvc.plug.spring.json;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import yyl.mvc.plug.json.MyObjectMapper;

/**
 * Jackson_ObjectMapper的工厂类
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

	@Override
	public ObjectMapper getObject() throws Exception {
		return MyObjectMapper.INSTANCE;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
