package yyl.mvc.plugin.hibernate.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.InitializingBean;

import yyl.mvc.common.convert.ConvertUtil;

/**
 * 实体元数据信息对象工具类
 */
public class MetadataInfoLookup implements InitializingBean {

	// ==============================Fields===========================================
	private final ConcurrentMap<Class<?>, MetadataInfo<?>> lookups = new ConcurrentHashMap<>();
	private SessionFactoryImplementor sessionFactoryImplementor;

	// ==============================Constructors=====================================
	public MetadataInfoLookup() {
	}

	public MetadataInfoLookup(SessionFactoryImplementor sessionFactoryImplementor) {
		this.sessionFactoryImplementor = sessionFactoryImplementor;
	}

	// ==============================Methods==========================================
	/**
	 * 获得实体元数据信息对象
	 * @param entityClass 实体类型
	 * @return 实体元数据信息对象
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> MetadataInfo<T> getEntityMetadata(Class<T> entityClass) {

		if (entityClass == null) {
			return null;
		}

		if (ConvertUtil.isStandardType(entityClass)) {
			return null;
		}

		MetadataInfo<?> lookup = lookups.get(entityClass);
		if (lookup == null) {
			lookups.putIfAbsent(entityClass, createLookup(entityClass));
			lookup = lookups.get(entityClass);
		}
		return (MetadataInfo<T>) lookup;
	}

	/**
	 * 获得实体元数据信息对象
	 * @param entityClass 实体类型
	 * @param propertyName 属性名称
	 * @return 属性的类型 (如果属性不存在则返回null)
	 */
	public <T> Class<?> getPropertyType(Class<T> entityClass, String propertyName) {
		MetadataInfo<T> lookup = getEntityMetadata(entityClass);
		return lookup == null ? null : lookup.getPropertyType(propertyName);
	}

	/**
	 * 创建实体元数据信息对象
	 * @param entityClass 实体类型
	 * @return 实体元数据信息对象
	 */
	private <T> MetadataInfo<T> createLookup(Class<T> entityClass) {
		ClassMetadata classMetadata = sessionFactoryImplementor.getClassMetadata(entityClass);
		return new MetadataInfo<T>(classMetadata, sessionFactoryImplementor);
	}

	//!
	public SessionFactoryImplementor getSessionFactoryImplementor() {
		return this.sessionFactoryImplementor;
	}

	// ==============================OverrideMethods==================================
	@Override
	public void afterPropertiesSet() throws Exception {
		if (sessionFactoryImplementor == null) {
			throw new IllegalArgumentException("Property 'sessionFactoryImplementor' is required");
		}
	}

	// ==============================IocMethods=======================================
	public void setSessionFactoryImplementor(SessionFactoryImplementor sessionFactoryImplementor) {
		this.sessionFactoryImplementor = sessionFactoryImplementor;
	}

}
