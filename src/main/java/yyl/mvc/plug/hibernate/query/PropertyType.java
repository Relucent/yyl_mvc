package yyl.mvc.plug.hibernate.query;

import java.util.Date;

/**
 * 属性类型
 */
public enum PropertyType {

	/** String. */
	S(String.class),
	/** Integer. */
	I(Integer.class),
	/** Long. */
	L(Long.class),
	/** Double. */
	N(Double.class),
	/** Date. */
	D(Date.class),
	/** Boolean. */
	B(Boolean.class),
	/** Object. */
	O(Object.class);

	/** class. */
	private Class<?> clazz;

	private PropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}

	/** @return value. */
	public Class<?> value() {
		return clazz;
	}
}
