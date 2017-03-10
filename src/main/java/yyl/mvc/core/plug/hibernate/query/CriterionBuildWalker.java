package yyl.mvc.core.plug.hibernate.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import yyl.mvc.core.plug.hibernate.support.PropertiesTypeLookupStore;
import yyl.mvc.core.util.collect.Listx;
import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.convert.ConvertUtil;

@Deprecated
public class CriterionBuildWalker {
	
	// ==============================Fields===========================================
	private PropertiesTypeLookupStore lookupStore;

	// ==============================Constructors=====================================
	public CriterionBuildWalker() {
		this(new PropertiesTypeLookupStore());
	}

	public CriterionBuildWalker(PropertiesTypeLookupStore lookupStore) {
		this.lookupStore = lookupStore;
	}

	// ==============================Methods==========================================
	/**
	 * 构建 Criterion 查询条件
	 * @param criteria Criteria
	 * @param filters 过滤条件
	 * @param entityClass 实体类型
	 */
	public void addCriterions(Criteria criteria, Mapx filters, Class<?> entityClass) {
		for (PropertyFilter propertyFilter : buildPropertyFilters(filters, entityClass)) {
			addCriterion(criteria, propertyFilter);
		}
	}

	public static List<PropertyFilter> buildPropertyFilters(Map<String, Object> filters, Class<?> entityClass) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		for (Map.Entry<String, Object> entry : filters.entrySet()) {
			filterList.add(buildPropertyFilter(entry));
		}
		return filterList;
	}

	public static PropertyFilter buildPropertyFilter(Map.Entry<String, Object> entry) {
		return new PropertyFilter(entry.getKey(), entry.getValue());
	}

	public void addCriterion(Criteria criteria, PropertyFilter propertyFilter) {

		Criterion criterion = buildCriterion(propertyFilter);
		if (criterion != null) {
			addAssociationPath(criteria, propertyFilter.getPropertyName());
			criteria.add(criterion);
		}
	}

	private void addAssociationPath(Criteria criteria, String path) {

		//HibernateUtil.addAlias(criteria, associationPath, alias);

	}

	@SuppressWarnings("rawtypes")
	private Criterion buildCriterion(PropertyFilter propertyFilter) {
		Assert.notNull(propertyFilter, "propertyFilter cannot be null");
		String propertyName = propertyFilter.getPropertyName();
		Class<?> propertyClass = propertyFilter.getPropertyClass();
		MatchType matchType = propertyFilter.getMatchType();
		Object matchValue = propertyFilter.getMatchValue();

		if (!ConvertUtil.isStandardType(propertyClass)) {
			return null;
		}

		if (MatchType.IN.equals(matchType)) {
			matchValue = convertToCollection(matchValue, propertyClass);
		} else if (MatchType.LIKE.equals(matchType)) {
			if (!(matchValue instanceof String)) {
				matchType = MatchType.UNKNOWN;
			}
		} else {
			matchValue = ConvertUtil.convert(matchValue, propertyClass);
		}

		Criterion criterion = null;
		switch (matchType) {
		case EQ:
			criterion = Restrictions.eq(propertyName, matchValue);
			break;
		case NOT:
			criterion = Restrictions.ne(propertyName, matchValue);
			break;
		case LIKE:
			criterion = Restrictions.like(propertyName, matchValue.toString(), MatchMode.ANYWHERE);
			break;
		case LE:
			criterion = Restrictions.le(propertyName, matchValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, matchValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, matchValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, matchValue);
			break;
		case IN:
			criterion = Restrictions.in(propertyName, (Collection) matchValue);
			break;
		case INL:
			criterion = Restrictions.isNull(propertyName);
			break;
		case NNL:
			criterion = Restrictions.isNotNull(propertyName);
			break;
		default:
			criterion = Restrictions.eq(propertyName, matchValue);
			break;
		}
		return criterion;
	}

	private <T> Collection<T> convertToCollection(Object value, Class<T> type) {
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
}
