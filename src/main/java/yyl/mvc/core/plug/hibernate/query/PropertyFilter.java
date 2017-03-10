package yyl.mvc.core.plug.hibernate.query;

import org.apache.commons.lang3.StringUtils;

import yyl.mvc.core.util.convert.ConvertUtil;

/**
 * property filter.<br>
 * 格式为: "matchType:propertyName#propertyType"
 */
public class PropertyFilter {

	/** property names. */
	private String propertyName;

	/** property class. */
	protected Class<?> propertyClass;

	/** match type. */
	private MatchType matchType;

	/** match value. */
	private Object matchValue;

	/**
	 * constructor.
	 * @param filterName
	 * @param matchValue
	 */
	public PropertyFilter(final String filterName, final Object matchValue) {

		int pos1 = filterName.indexOf(':');
		String first = pos1 != -1 ? filterName.substring(0, pos1) : StringUtils.EMPTY;
		String surplus = pos1 != -1 ? filterName.substring(pos1 + 1) : filterName;

		int pos2 = surplus.indexOf('#');
		String second = pos2 != -1 ? surplus.substring(0, pos2) : surplus;
		String third = pos2 != -1 ? surplus.substring(pos2 + 1) : StringUtils.EMPTY;

		this.matchType = StringUtils.isEmpty(first) ? MatchType.EQ : ConvertUtil.toEnum(first, MatchType.class, MatchType.EQ);
		this.propertyName = second;
		this.propertyClass = (StringUtils.isEmpty(third) ? PropertyType.O : ConvertUtil.toEnum(third, PropertyType.class, PropertyType.O)).value();
		this.matchValue = matchValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Object getMatchValue() {
		return matchValue;
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
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
