package yyl.mvc.core.plug.hibernate.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import yyl.mvc.core.util.convert.ConvertUtil;

/**
 * 属性类型查找器工具类
 */
public class PropertiesTypeLookupStore {

	// ==============================Fields===========================================
	public static final PropertiesTypeLookupStore INSTANCE = new PropertiesTypeLookupStore();

	private final ConcurrentMap<Class<?>, PropertiesTypeLookup<?>> lookups = new ConcurrentHashMap<>();

	// ==============================Constructors=====================================
	public PropertiesTypeLookupStore() {
	}

	// ==============================Methods==========================================
	/**
	 * 获得实体的属性类型检索器
	 * @param entityClass 实体类型
	 * @return 属性类型检索器
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> PropertiesTypeLookup<T> getPropertiesTypeLookup(Class<T> entityClass) {

		if (entityClass == null) {
			return null;
		}

		if (ConvertUtil.isStandardType(entityClass)) {
			return null;
		}

		PropertiesTypeLookup<?> lookup = lookups.get(entityClass);
		if (lookup == null) {
			lookups.putIfAbsent(entityClass, createLookup(entityClass));
			lookup = lookups.get(entityClass);
		}
		return (PropertiesTypeLookup<T>) lookup;
	}

	/**
	 * 获得实体的属性的类型
	 * @param entityClass 实体类型
	 * @param propertyName 属性名称
	 * @return 属性的类型 (如果属性不存在则返回null)
	 */
	public <T> Class<?> getPropertyType(Class<T> entityClass, String propertyName) {
		PropertiesTypeLookup<T> lookup = getPropertiesTypeLookup(entityClass);
		return lookup == null ? null : lookup.getPropertyType(propertyName);
	}

	/**
	 * 创建属性类型查找器
	 * @param entityClass 实体类型
	 * @return 属性类型查找器
	 */
	private <T> PropertiesTypeLookup<T> createLookup(Class<T> entityClass) {
		return new PropertiesTypeLookup<T>(entityClass, this);
	}

}
