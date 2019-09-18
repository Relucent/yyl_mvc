package yyl.mvc.plugin.hibernate.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import yyl.mvc.common.collect.Listx;
import yyl.mvc.common.convert.ConvertUtil;

/**
 * property filter.<br>
 * 格式为: "matchType:propertyName#propertyType"
 */
public class PropertyFilter {

	/** property names. */
	private String propertyName;

	/** property class. */
	private Class<?> propertyClass;

	/** match type. */
	private MatchType matchType;

	/** match value. */
	private Object matchValue;

	/** original match value. */
	private Object originalMatchValue;

	/** property path. */
	private String[] propertyPaths;

	/** property class and value correct */
	private transient boolean typeRevised = false;

	/**
	 * constructor.
	 * @param propertyName 属性名
	 * @param matchType 匹配类型
	 * @param matchValue 匹配值
	 */
	public PropertyFilter(String propertyName, MatchType matchType, Object matchValue) {
		this(propertyName, matchValue != null ? matchValue.getClass() : null, matchType, matchValue);
	}

	/**
	 * constructor.
	 * @param propertyName 属性名
	 * @param propertyClass 属性类型
	 * @param matchType 匹配类型
	 * @param matchValue 匹配值
	 */
	public PropertyFilter(String propertyName, Class<?> propertyClass, MatchType matchType, Object matchValue) {
		this.propertyName = propertyName;
		this.propertyClass = propertyClass;
		this.matchType = matchType;
		this.originalMatchValue = matchValue;
		this.matchValue = this.originalMatchValue;
		this.propertyPaths = StringUtils.split(this.propertyName, '.');
	}

	/**
	 * constructor.
	 */
	protected static PropertyFilter parse(final String filterName, final String filterValue) {
		int pos1 = filterName.indexOf(':');
		String first = pos1 != -1 ? filterName.substring(0, pos1) : StringUtils.EMPTY;
		String surplus = pos1 != -1 ? filterName.substring(pos1 + 1) : filterName;

		int pos2 = surplus.indexOf('#');
		String second = pos2 != -1 ? surplus.substring(0, pos2) : surplus;
		String third = pos2 != -1 ? surplus.substring(pos2 + 1) : StringUtils.EMPTY;
		MatchType matchType = StringUtils.isEmpty(first) ? MatchType.EQ : ConvertUtil.toEnum(first, MatchType.class, MatchType.EQ);
		String propertyName = second;
		Class<?> propertyClass = (StringUtils.isEmpty(third) ? //
				PropertyType.O : ConvertUtil.toEnum(third, PropertyType.class, PropertyType.O)).value();
		Object matchValue = filterValue;
		return new PropertyFilter(propertyName, propertyClass, matchType, matchValue);
	}

	protected boolean revisePropertyValue() {

		Assert.notNull(propertyClass, "propertyClass cannot be null");

		if (!ConvertUtil.isStandardType(propertyClass)) {
			return false;
		}

		if (MatchType.IN.equals(matchType)) {
			matchValue = convertToCollection(originalMatchValue, propertyClass);
		} else if (MatchType.LIKE.equals(matchType)) {
			if (!(matchValue instanceof String)) {
				matchType = MatchType.UNKNOWN;
			}
		} else {
			matchValue = ConvertUtil.convert(originalMatchValue, propertyClass);
		}

		typeRevised = true;

		return true;
	}

	private static <T> Collection<T> convertToCollection(Object value, Class<T> type) {
		List<T> list = new ArrayList<T>();
		if (value instanceof String) {
			for (String item : StringUtils.split(value.toString(), ',')) {
				list.add((T) ConvertUtil.convert(item, type));
			}
		} else {
			Listx listx = ConvertUtil.toList(value);
			for (int i = 0, I = listx.size(); i < I; i++) {
				list.add(ConvertUtil.convert(listx.get(i), type));
			}
		}
		return list;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Object getMatchValue() {
		if (!typeRevised) {
			revisePropertyValue();
		}
		return matchValue;
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
	}

	protected void setPropertyType(Class<?> propertyClass) {
		this.propertyClass = propertyClass;
	}

	public String[] getPropertyPaths() {
		return propertyPaths;
	}

	public boolean isComplexProperty() {
		return propertyPaths.length > 1;
	}

	protected boolean isTypeRevised() {
		return typeRevised;
	}

	@Override
	public String toString() {
		return "PropertyFilter ["//
				+ "propertyName=" + propertyName//
				+ ", matchType=" + matchType //
				+ ", propertyClass=" + propertyClass//
				+ ", matchValue=" + matchValue //
				+ "]";
	}
}
