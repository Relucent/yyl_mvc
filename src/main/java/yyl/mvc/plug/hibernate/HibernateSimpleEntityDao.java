package yyl.mvc.plug.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 提供基础功能方法的数据访问层类.<br>
 * 使用泛型定义具体的实体对象.提供对具体类型实体对象的基本数据操作.<br>
 * 备注：为规范子类实现，该类主要提供了QBC方式查询方式.<br>
 * 建议使用规范的QBC查询方式, 部分特殊需求使用SQL的查询方式, 不建议使用HQL的查询方式.<br>
 * @see com.bisoft.core.orm.hibernate.icitic.framework.core.orm.hibernate.HibernateDaoSupport
 * @author YYL
 * @version 0.1 2012-10-17
 */
public abstract class HibernateSimpleEntityDao<T> extends HibernateGenericDao {

	// ===================================Fields==============================================
	/** 持久类的类型. */
	protected Class<T> entityClass;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	// ===================================Constructors========================================
	/** 构造方法. */
	protected HibernateSimpleEntityDao() {
		defineGenericType();
	}

	// ===================================Initialize==========================================
	@SuppressWarnings("unchecked")
	protected void defineGenericType() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class<T>) params[0];
	}

	// ===================================Methods=============================================
	/**
	 * 子类可以获得泛型对应的实体类型.
	 * @return entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	// ===================================基本方法==============================================
	/**
	 * 根据主键删除记录.
	 * @param id 主键
	 * @return 删除成功返回true,如果实体不存在返回false.
	 */
	public <I extends Serializable> boolean removeById(I id) {
		return super.removeById(entityClass, id);
	}

	/**
	 * 获得一个实体类型的所有记录.
	 * @return 所有实例列表
	 * @deprecated 可能会引发效率问题，故不建议直接使用。
	 */
	public List<T> findAll() {
		return super.findAll(entityClass);
	}

	/**
	 * 刪除该实体类型的所有记录.
	 * @deprecated 该操作会删除该类型全部数据，谨慎使用.
	 */
	public void removeAll() {
		super.removeAll(entityClass);
	}

	// ===================================Criteria创建=========================================
	/**
	 * 获得泛型对应实体的Criteria.
	 */
	public Criteria createCriteria() {
		return super.createCriteria(entityClass);
	}

	// ===================================查询多条记录===========================================
	// ..
	// ===================================查询单条记录===========================================
	/**
	 * 根据主键获得实体对象.
	 * @param id 主键
	 * @return 实体对象
	 */
	public <I extends Serializable> T getById(I id) {
		return super.getById(entityClass, id);
	}

	// ===================================辅助函数==============================================
	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * @param entity 对象
	 * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 * @return 如果唯一返回true，否则返回false
	 */
	public boolean isUnique(T entity, String... uniquePropertyNames) {
		return isUnique(entityClass, entity, uniquePropertyNames);
	}

	/**
	 * 获得总记录数.
	 * @return 总数
	 */
	protected Integer getCount() {
		return getCount(entityClass);
	}

	/**
	 * 取得对象的主键值，辅助函数.
	 * @param entity 实例
	 * @return 主键
	 * @throws NoSuchMethodException 找不到方法
	 * @throws IllegalAccessException 没有访问权限
	 * @throws InvocationTargetException 反射异常
	 */
	@SuppressWarnings("unchecked")
	public <I extends Serializable> I getId(T entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return (I) getId(entityClass, entity);
	}
}
