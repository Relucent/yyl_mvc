package yyl.mvc.core.plug.hibernate.support;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.mapping.Collection;

import yyl.mvc.core.util.bean.BeanUtil;
import yyl.mvc.core.util.convert.ConvertUtil;

public class PropertiesTypeLookup<T> {

	// ==============================Fields===========================================
	private final Class<T> beanClass;
	private final PropertiesTypeLookupStore lookupStore;
	private final Map<String, Class<?>> propertyTypes;

	// ==============================Methods==========================================
	protected PropertiesTypeLookup(Class<T> beanClass, PropertiesTypeLookupStore lookupStore) {
		this.beanClass = beanClass;
		this.lookupStore = lookupStore;
		this.propertyTypes = new ConcurrentHashMap<>();
	}

	public Class<?> getPropertyType(String propertyName) {
		Class<?> propertyType = propertyTypes.get(propertyName);
		if (propertyType != null) {
			try {
				lookupPropertyType(this.beanClass, propertyName);
			} catch (Exception e) {
				throw new NoSuchPropertyException(e);
			}
		}
		return propertyType;
	}

	protected Class<?> lookupPropertyType(Class<T> beanClass, String propertyName) throws NoSuchFieldException {
		PathTokenizer tokenizer = new PathTokenizer(propertyName);
		if (tokenizer.hasNext()) {
			Field field = BeanUtil.forceGetDeclaredField(beanClass, tokenizer.name);
			PropertiesTypeLookup<?> lookup = getLookupForProperty(field);
			return lookup.getPropertyType(tokenizer.children);
		} else {
			Field field = BeanUtil.forceGetDeclaredField(beanClass, tokenizer.name);
			return field.getType();
		}
	}

	private PropertiesTypeLookup<?> getLookupForProperty(Field field) {
		Class<?> type = getActualTypeArguments(field);
		return lookupStore.getPropertiesTypeLookup(type);
	}

	private Class<?> getActualTypeArguments(Field field) {
		Class<?> fieldType = field.getType();

		if (ConvertUtil.isStandardType(fieldType)) {
			return fieldType;
		}

		if (Map.class.isAssignableFrom(fieldType)) {
			return null;
		}

		if (Collection.class.isAssignableFrom(fieldType)) {
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
				if (actualTypeArguments.length > 0) {
					Type type = actualTypeArguments[0];
					if (type instanceof Class) {
						return (Class<?>) type;
					}
				}
			}
		}

		return fieldType;
	}

	private static class PathTokenizer {
		private String name;
		private String children;

		public PathTokenizer(String fullname) {
			int delim = fullname.indexOf('.');
			if (delim > -1) {
				this.name = fullname.substring(0, delim);
				this.children = fullname.substring(delim + 1);
			} else {
				this.name = fullname;
				this.children = null;
			}
		}

		public boolean hasNext() {
			return this.children != null;
		}
	}

	@SuppressWarnings("serial")
	public static class NoSuchPropertyException extends RuntimeException {
		protected NoSuchPropertyException(Exception error) {
			super(error);
		}
	}

}
