package yyl.mvc.core.plug.hibernate.util;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.sql.JoinType;

/**
 * _Hibernate 工具类
 */
public class HibernateUtil {

	// ==============================Fields===========================================
	private static final JoinType DEFAULT_JOIN_TYPE = JoinType.LEFT_OUTER_JOIN;

	private static final Field CRITERIAIMPL_IMPL_FIELD;
	static {
		Field field = null;
		try {
			field = CriteriaImpl.class.getDeclaredField("impl");
			field.setAccessible(true);
		} catch (Exception e) {
		}
		CRITERIAIMPL_IMPL_FIELD = field;
	}

	// ==============================Methods==========================================
	/**
	 * 增加关联(建立LEFT_OUTER_JOIN关联)
	 * @param criteria Criteria类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @return criteria 传入的Criteria对象
	 */
	public static Criteria addAlias(Criteria criteria, String associationPath, String alias) {
		return addAlias(criteria, associationPath, alias, DEFAULT_JOIN_TYPE);
	}

	/**
	 * 增加关联(建立JOIN关联)
	 * @param criteria Criteria类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @param joinType 关联类型
	 * @return criteria 传入的Criteria对象
	 */
	public static Criteria addAlias(Criteria criteria, String associationPath, String alias, JoinType joinType) {
		return criteria instanceof CriteriaImpl //
				? addAlias(criteria, associationPath, alias, joinType) //
				: criteria.createAlias(associationPath, alias, joinType);
	}

	/**
	 * 增加关联(建立LEFT_OUTER_JOIN关联)
	 * @param dc DetachedCriteria类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @return criteria 传入的DetachedCriteria对象
	 */
	public static DetachedCriteria addAlias(DetachedCriteria dc, String associationPath, String alias) {
		return addAlias(dc, associationPath, alias, DEFAULT_JOIN_TYPE);
	}

	/**
	 * 增加关联(建立JION关联)
	 * @param dc DetachedCriteria类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @param joinType 关联类型
	 * @return criteria 传入的DetachedCriteria对象
	 */
	public static DetachedCriteria addAlias(DetachedCriteria dc, String associationPath, String alias, JoinType joinType) {
		CriteriaImpl impl = getImpl(dc);
		if (impl == null) {
			return dc.createAlias(associationPath, alias, joinType);
		}
		addAlias(impl, associationPath, alias, joinType);
		return dc;
	}

	/**
	 * 增加关联(建立LEFT_OUTER_JOIN关联)
	 * @param criteria Criteria实现类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @return criteria 传入的Criteria对象
	 */
	public static Criteria addAlias(CriteriaImpl criteria, String associationPath, String alias) {
		return addAlias(criteria, associationPath, alias, DEFAULT_JOIN_TYPE);
	}

	/**
	 * 增加关联(建立JION关联)
	 * @param criteria Criteria实现类
	 * @param associationPath 关联路径
	 * @param alias 别名
	 * @param joinType 关联类型
	 * @return criteria 传入的Criteria对象
	 */
	public static CriteriaImpl addAlias(CriteriaImpl criteria, String associationPath, String alias, JoinType joinType) {
		if (StringUtils.isEmpty(associationPath) || StringUtils.isEmpty(alias)) {
			return criteria;
		}
		if (!hasSubcriteriaAlias(criteria, alias)) {
			criteria.createAlias(associationPath, alias, joinType);
		}
		return criteria;
	}

	/**
	 * 获得DetachedCriteria类的_impl字段
	 * @param dc DetachedCriteria
	 * @return CriteriaImpl 对象
	 */
	private static CriteriaImpl getImpl(DetachedCriteria dc) {
		try {
			return (CriteriaImpl) CRITERIAIMPL_IMPL_FIELD.get(dc);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断 CriteriaImpl 是否存在该关联查询别名
	 * @param criteria CriteriaImpl
	 * @param alias 关联别名
	 * @return 存在返回true,否则返回false
	 */
	private static boolean hasSubcriteriaAlias(CriteriaImpl criteria, String alias) {
		for (Iterator<?> iter = criteria.iterateSubcriteria(); iter.hasNext();) {
			Subcriteria subCriteria = (Subcriteria) iter.next();
			if (alias.equals(subCriteria.getAlias())) {
				return true;
			}
		}
		return false;
	}
}
