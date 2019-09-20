package yyl.mvc.plugin.hibernate.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.InitializingBean;

import yyl.mvc.common.collection.Mapx;
import yyl.mvc.common.convert.ConvertUtil;
import yyl.mvc.plugin.hibernate.support.MetadataInfoLookup;
import yyl.mvc.plugin.hibernate.util.HibernateUtil;

public class CriterionBuildWalker implements InitializingBean {

	// ==============================Fields===========================================
	private MetadataInfoLookup metadataInfoLookup;

	// ==============================Constructors=====================================
	public CriterionBuildWalker() {
		this(new MetadataInfoLookup());
	}

	public CriterionBuildWalker(MetadataInfoLookup metadataInfoLookup) {
		this.metadataInfoLookup = metadataInfoLookup;
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
			revisePropertyType(propertyFilter, entityClass, false);
			addCriterion(criteria, propertyFilter);
		}
	}

	public static List<PropertyFilter> buildPropertyFilters(Map<String, Object> filters, Class<?> entityClass) {
		List<PropertyFilter> result = new ArrayList<PropertyFilter>();
		for (Map.Entry<String, Object> entry : filters.entrySet()) {

			PropertyFilter propertyFilter = parsePropertyFilter(entry.getKey(), entry.getValue());

			if (propertyFilter != null) {
				result.add(propertyFilter);
			}
		}
		return result;
	}

	public static PropertyFilter parsePropertyFilter(String filterName, Object filterValue) {
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

	public void addCriterion(Criteria criteria, PropertyFilter propertyFilter) {
		String[] paths = propertyFilter.getPropertyPaths();
		String previousAlias = HibernateUtil.getAlias(criteria);
		int associationLength = paths.length - 1;
		for (int i = 0; i < associationLength; i++) {
			String alias = paths[i];
			String associationPath = previousAlias + "." + alias;
			HibernateUtil.addAlias(criteria, associationPath, previousAlias = alias);
		}
		buildCriterion(previousAlias + "." + paths[associationLength], propertyFilter.getMatchType(), propertyFilter.getMatchValue());
	}

	private boolean revisePropertyType(PropertyFilter propertyFilter, Class<?> entityClass, boolean force) {
		Class<?> propertyClass = propertyFilter.getPropertyClass();
		if (force || propertyClass == null || ConvertUtil.isStandardType(propertyClass)) {
			propertyClass = metadataInfoLookup.getPropertyType(entityClass, propertyFilter.getPropertyName());
			propertyFilter.setPropertyType(propertyClass);
		}
		return propertyFilter.revisePropertyValue();
	}

	@SuppressWarnings("rawtypes")
	private Criterion buildCriterion(String propertyName, MatchType matchType, Object matchValue) {
		switch (matchType) {
		case EQ:
			return Restrictions.eq(propertyName, matchValue);
		case NOT:
			return Restrictions.ne(propertyName, matchValue);
		case LIKE:
			return Restrictions.like(propertyName, matchValue.toString(), MatchMode.ANYWHERE);
		case LE:
			return Restrictions.le(propertyName, matchValue);
		case LT:
			return Restrictions.lt(propertyName, matchValue);
		case GE:
			return Restrictions.ge(propertyName, matchValue);
		case GT:
			return Restrictions.gt(propertyName, matchValue);
		case IN:
			return Restrictions.in(propertyName, (Collection) matchValue);
		case INL:
			return Restrictions.isNull(propertyName);
		case NNL:
			return Restrictions.isNotNull(propertyName);
		default:
			return Restrictions.eq(propertyName, matchValue);
		}
	}

	// ==============================Constructors=====================================
	protected MetadataInfoLookup createMetadataInfoLookup(SessionFactoryImplementor sessionFactoryImplementor) {
		return new MetadataInfoLookup(sessionFactoryImplementor);
	}

	// ==============================OverrideMethods==================================
	@Override
	public void afterPropertiesSet() throws Exception {
		if (metadataInfoLookup == null) {
			throw new IllegalArgumentException("'sessionFactoryImplementor' or 'metadataInfoLookup' is required");
		}
	}

	// ==============================IocMethods=======================================
	public void setMetadataInfoLookup(MetadataInfoLookup metadataInfoLookup) {
		this.metadataInfoLookup = metadataInfoLookup;
	}

	public void setSessionFactoryImplementor(SessionFactoryImplementor sessionFactoryImplementor) {
		if (this.metadataInfoLookup == null || sessionFactoryImplementor != this.metadataInfoLookup.getSessionFactoryImplementor()) {
			this.metadataInfoLookup = createMetadataInfoLookup(sessionFactoryImplementor);
		}
	}
}
