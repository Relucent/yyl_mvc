package yyl.mvc.core.plug.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import yyl.mvc.core.util.bean.BeanUtil;
import yyl.mvc.core.util.page.Page;

/**
 * 提供基础功能方法的数据访问层类.<br>
 * 备注：为规范子类实现，该类主要提供了QBC方式查询方式.<br>
 * 建议使用规范的QBC查询方式, 部分特殊需求使用SQL的查询方式, 不建议使用HQL的查询方式.<br>
 * @author YYL
 * @version 0.1 2012-10-13
 */
// @Repository
public class HibernateGenericDao extends HibernateDaoSupport {

    // ===================================基本方法==============================================
    /**
     * 获得一个数据实体
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @param id 实体的主键
     * @return 实体实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getById(Class<T> entityClass, Serializable id) {
        if (id == null) {
            return null;
        } else {
            return (T) getSession().get(entityClass, id);
        }
    }

    /**
     * 获得一个数据实体
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @param id 实体的主键
     * @return 实体实例
     */
    @SuppressWarnings("unchecked")
    public <T> T loadById(Class<T> entityClass, Serializable id) {
        if (id == null) {
            return null;
        } else {
            return (T) getSession().load(entityClass, id);
        }
    }

    /**
     * 获得某个类型的全部实体对象.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @return 实体对象列表
     * @deprecated 可能会引发效率问题，故不建议直接使用
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findAll(Class<T> entityClass) {
        return getSession().createCriteria(entityClass).list();
    }

    /**
     * 保存实体.
     * @param entity 实体
     */
    @SuppressWarnings("unchecked")
    public <I extends Serializable> I save(Object entity) {
        Assert.notNull(entity, "entity不能为空");
        return (I) getSession().save(entity);
    }

    /**
     * 更新实体.
     * @param entity 实体
     */
    public void update(Object entity) {
        getSession().update(entity);
    }

    /**
     * 添加或更新实体.
     * @param entity 实体
     */
    public void saveOrUpdate(Object entity) {
        getSession().saveOrUpdate(entity);
    }

    /**
     * 添加或更新多条数据.
     * @param entities 实体列表
     */
    public void saveOrUpdateAll(Collection<?> entities) {
        Session session = getSession();
        Object entity;
        for (Iterator<?> iterator = entities.iterator(); iterator.hasNext(); session.saveOrUpdate(entity))
            entity = iterator.next();
    }

    /**
     * 删除一条记录.
     * @param entity 实例
     */
    public void remove(Object entity) {
        if (entity != null) {
            getSession().delete(entity);
        }
    }

    /**
     * 删除多条记录.
     * @param entities 实例
     */
    public void removeAll(Collection<?> entities) {
        Session session = getSession();
        Object entity;
        for (Iterator<?> iterator = entities.iterator(); iterator.hasNext(); session.delete(entity)) {
            entity = iterator.next();
        }
    }

    /**
     * 根据主键删除记录.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @param id 主键
     * @return 删除成功返回true,如果实体不存在返回false.
     */
    public <T> boolean removeById(Class<T> entityClass, Serializable id) {
        T entity = getById(entityClass, id);
        if (entity == null) {
            return false;
        } else {
            remove(entity);
            return true;
        }
    }

    /**
     * 删除所有记录.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     */
    public <T> void removeAll(Class<T> entityClass) {
        removeAll(findAll(entityClass));
    }

    /**
     * 合并保存实体对象.
     * @param <T> 实体类型
     * @param entity 实体对象
     * @deprecated 该方法使用不容易理解，为了防止出错不建议使用
     */
    protected <T> void merge(T entity) {
        getSession().merge(entity);
    }

    // ===================================Session相关============================================
    // session
    /**
     * 把session中的数据flush到数据库中.
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * 清空session.
     */
    public void clear() {
        getSession().clear();
    }

    /**
     * 从缓存中删除实体对象.
     * @param entity 实体对象
     * @deprecated
     */
    public void evict(Object entity) {
        getSession().evict(entity);
    }

    /**
     * 从新初始化实体对象，等同于(lazy=false)。
     * @param <T> 实体类型
     */
    public <T> T initialize(T entity) {
        Hibernate.initialize(entity);
        return entity;
    }

    // ===================================Criteria创建=========================================
    /**
     * 根据entityClass生成对应类型的Criteria.
     * @param entityClass 实体类型
     * @return Criteria
     */
    protected Criteria createCriteria(final Class<?> entityClass) {
        return getSession().createCriteria(entityClass);
    }

    /**
     * 根据entityClass生成对应类型的Criteria.
     * @param entityClass 实体类型
     * @param alias 实体别名
     * @return Criteria
     */
    protected Criteria createCriteria(final Class<?> entityClass, String alias) {
        return getSession().createCriteria(entityClass, alias);
    }

    /**
     * 根据entityClass生成对应类型的Criteria.
     * @param entityClass 实体类型
     * @param criterions 条件
     * @return Criteria
     */
    private Criteria createCriteria(final Class<?> entityClass, final Criterion... criterions) {
        Criteria criteria = createCriteria(entityClass);
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        return criteria;
    }

    /**
     * 根据detachedCriteria获得与Session关联的Criteria
     * @param detachedCriteria 条件
     * @return 与Session关联的Criteria
     */
    public Criteria getExecutableCriteria(final DetachedCriteria detachedCriteria) {
        return detachedCriteria.getExecutableCriteria(getSession());
    }

    // ===================================查询单条记录===========================================
    /**
     * 查询唯一记录.
     * @param <T> 实体类型
     * @param query 查询
     * @return 查询唯一记录
     */
    @SuppressWarnings("unchecked")
    public <T> T getByQuery(final Query query) {
        return (T) query.setMaxResults(1).uniqueResult();
    }

    /**
     * 查询唯一记录.
     * @param criteria 条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> T getByCriteria(final Criteria criteria) {
        return (T) criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * 查询唯一记录.
     * @param detachedCriteria 条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> T getByCriteria(final DetachedCriteria detachedCriteria) {
        return (T) detachedCriteria.getExecutableCriteria(getSession()).setMaxResults(1).uniqueResult();
    }

    /**
     * 查询唯一记录.
     * @param entityClass 实体类型
     * @param criterions 查询条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> T getByCriterion(final Class<T> entityClass, Criterion... criterions) {
        return (T) createCriteria(entityClass, criterions).setMaxResults(1).uniqueResult();
    }

    /**
     * 查询唯一记录.
     * @param entityClass 实体类型
     * @param name 字段名
     * @param value 参数值
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> T getByField(final Class<T> entityClass, final String name, final Object value) {
        return (T) createCriteria(entityClass, Restrictions.eq(name, value)).setMaxResults(1).uniqueResult();
    }

    // ===================================查询多条记录===========================================
    /**
     * 查询记录.
     * @param criteria 条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findByCriteria(final Criteria criteria) {
        return (List<T>) criteria.list();
    }

    /**
     * 查询记录.
     * @param detachedCriteria 条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findByCriteria(final DetachedCriteria detachedCriteria) {
        return (List<T>) detachedCriteria.getExecutableCriteria(getSession()).list();
    }

    /**
     * 查询记录.
     * @param entityClass 实体类型
     * @param criterions 查询条件
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findByCriterion(final Class<T> entityClass, final Criterion... criterions) {
        return createCriteria(entityClass, criterions).list();
    }

    /**
     * 查询记录.
     * @param entityClass 实体类型
     * @param name 字段名
     * @param value 参数值
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(final Class<T> entityClass, final String name, final Object value) {
        return createCriteria(entityClass, Restrictions.eq(name, value)).list();
    }

    // ===================================分页查询==============================================
    /**
     * 分页查询函数，使用已设好查询条件与排序的<code>DetachedCriteria</code>.<br>
     * 不支持复杂complex查询,如分组查询之类.<BR>
     * @param detachedCriteria 查询条件
     * @param start 查询开始的记录索引
     * @param limit 每页显示最大记录数
     * @return 包含记录数和当前页数据的Page对象.
     * @see org.hibernate.criterion.DetachedCriteria
     */
    public <T> Page<T> pagedQuery(DetachedCriteria criteria, int start, int limit) {
        return pagedQuery(criteria.getExecutableCriteria(getSession()), start, limit);
    }

    /**
     * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.<br>
     * 不支持复杂complex查询,如分组查询之类.<BR>
     * @param start 查询开始的记录索引
     * @param limit 每页显示最大记录数
     * @return 包含记录数和当前页数据的Page对象.
     * @see org.hibernate.Criteria
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <T> Page<T> pagedQuery(Criteria criteria, int start, int limit) {
        Assert.notNull(criteria, "criteria==null");
        Assert.isTrue(start >= 0, "start should be eg 0");
        Integer total = getCountByCriteria(criteria);
        if (total < 1) {
            return new Page(0, limit, new ArrayList(), 0);
        }
        List<T> records = criteria.setFirstResult(start).setMaxResults(limit).list();
        return new Page<T>(start, limit, records, total);
    }

    // ===================================统计函数==============================================
    /**
     * 获得总记录数.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @return 总数
     */
    public <T> Integer getCount(Class<T> entityClass) {
        return ((Number) createCriteria(entityClass).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    /**
     * 获得总记录数.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @param criterions 条件
     * @return 总数
     */
    public <T> Integer getCountByCriterion(Class<T> entityClass, Criterion... criterions) {
        return ((Number) createCriteria(entityClass, criterions).setProjection(Projections.rowCount()).uniqueResult())
                .intValue();
    }

    /**
     * 获得总记录数.
     * @param <T> 实体类型
     * @param entityClass 实体类型
     * @return 总数
     */
    public Integer getCountByCriteria(DetachedCriteria detachedCriteria) {
        return getCountByCriteria(detachedCriteria.getExecutableCriteria(getSession()));
    }

    /**
     * 获得总记录数(该方法无法正确处理分组查询).
     * @param criteria 条件
     * @return 总数
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Integer getCountByCriteria(Criteria criteria) {
        CriteriaImpl impl = null;
        if (criteria instanceof CriteriaImpl) {
            impl = CriteriaImpl.class.cast(criteria);
        } else {
            Assert.isTrue(criteria instanceof CriteriaImpl, "!(criteria instanceof CriteriaImpl)");
        }

        // 先把Projection、Resultk和OrderBy条件取出来,处理后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<CriteriaImpl.OrderEntry> orderEntries;
        Integer firstResult = impl.getFirstResult();
        Integer maxResults = impl.getMaxResults();;
        try {
            orderEntries = (List) BeanUtil.forceGetProperty(impl, "orderEntries");
            BeanUtil.forceSetProperty(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }
        try {

            // 判断是否存在复杂表达式，如果存在则需要使用复杂SQL计算COUNT.
            if (projection instanceof ProjectionList) {

                // 获取SESSION实现
                SessionImplementor sessionImplementor = impl.getSession();

                // 获取SESSION工厂实现
                SessionFactoryImplementor factory = sessionImplementor.getFactory();

                // 查询翻译者
                CriteriaQueryTranslator translator = new CriteriaQueryTranslator(//
                        factory, //
                        impl, //
                        impl.getEntityOrClassName(), //
                        CriteriaQueryTranslator.ROOT_SQL_ALIAS);

                // 判断是否是分组(GroupBy)查询
                if (StringUtils.isNotBlank(translator.getGroupBy())) {

                    // 查询参数
                    QueryParameters queryParameters = translator.getQueryParameters();

                    // 获得参数数组
                    final Object[] values = queryParameters.getPositionalParameterValues();//

                    // 如果参数数组存在(如果不存在则无法使用这个方法进行查询)
                    if (values != null) {

                        // 实施者(查询的主体对象()类名)
                        String implementor = factory.getImplementors(impl.getEntityOrClassName())[0];

                        // 实体持久
                        EntityPersister entityPersister = factory.getEntityPersister(implementor);

                        // 判断是否是OuterJoinLoadable类型(如果不是这个不能用以下方法进行查询)
                        if (entityPersister instanceof OuterJoinLoadable) {

                            // 外连接加载器
                            OuterJoinLoadable outerJoinLoadable = (OuterJoinLoadable) entityPersister;

                            // CriteriaJoinWalker对象可以获得
                            CriteriaJoinWalker walker = new CriteriaJoinWalker(//
                                    outerJoinLoadable, //
                                    translator, //
                                    factory, //
                                    impl, //
                                    impl.getEntityOrClassName(), //
                                    sessionImplementor.getLoadQueryInfluencers()//
                            );

                            // 获得HibernateCriteria的原生SQL
                            final String sql = walker.getSQLString();

                            // 用来储存Integer类型的原子包装对象
                            final AtomicInteger totalCount = new AtomicInteger(0);

                            // 进行COUNT查询
                            // Assert.isTrue(sessionImplementor==getSession());
                            ((Session) sessionImplementor).doWork(new Work() {
                                public void execute(Connection conn) throws SQLException {
                                    PreparedStatement ps = null;
                                    ResultSet rs = null;
                                    try {

                                        ps = conn.prepareStatement(""//
                                                + " select "//
                                                + " count(*) as __virtual_count__y "//
                                                + " from ( " //
                                                + sql //
                                                + " ) __virtual_table__y "//
                                        );

                                        ParameterMetaData meta = ps.getParameterMetaData();

                                        // 参数个数不匹配(说明这个方式的查询不适用)
                                        if (meta.getParameterCount() != values.length) {
                                            totalCount.set(-1);
                                            return;
                                        }

                                        for (int i = 0; i < values.length; i++) {
                                            ps.setObject(i + 1, values[i]);
                                        }

                                        rs = ps.executeQuery();
                                        //
                                        if (rs.next()) {
                                            totalCount.set(rs.getInt("__virtual_count__y"));
                                        }
                                    } finally {
                                        try {
                                            rs.close();
                                        } catch (Exception e) {}
                                        try {
                                            ps.close();
                                        } catch (Exception e) {}
                                    }
                                }
                            });

                            // 获得count返回结果
                            int count = totalCount.get();

                            // 如果等于-1则说明上面的参数不匹配
                            if (count > -1) {
                                return count;
                            }
                        }
                    }
                }
            } ;

            // 执行Count查询
            Object result = impl.setProjection(Projections.rowCount()).uniqueResult();
            return ((Number) result).intValue();

        } finally {

            // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
            criteria.setProjection(projection);
            if (transformer != null) {
                criteria.setResultTransformer(transformer);
            }
            if (projection == null) {
                criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            }
            try {
                BeanUtil.forceSetProperty(impl, "orderEntries", orderEntries);
            } catch (Exception e) {
                throw new InternalError(" Runtime Exception impossibility throw ");
            }
            if (firstResult != null) {
                impl.setFirstResult(firstResult);
            }
            if (maxResults != null) {
                impl.setMaxResults(maxResults);
            }
        }
    }

    // ===================================辅助函数==============================================
    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     * @param entityClass 实体类型
     * @param entity 对象
     * @param uniquePropertyNames 在POJO里不能重复的属性列表
     * @param <T> 实体类泛型
     * @return 如果唯一返回true，否则返回false
     */
    public <T> boolean isUnique(Class<T> entityClass, Object entity, String... uniquePropertyNames) {
        Criteria criteria = createCriteria(entityClass).setProjection(Projections.rowCount());
        try {
            // 循环加入唯一列
            for (String name : uniquePropertyNames) {
                Object property = PropertyUtils.getProperty(entity, name);
                if (property == null) {
                    criteria.add(Restrictions.isNull(name));
                } else {
                    criteria.add(Restrictions.eq(name, property));
                }
            }
            // 以下代码为了如果是update的情况,排除entity自身.
            String idName = getIdName(entityClass);
            // 取得entity的主键值
            Serializable id = getId(entityClass, entity);
            // 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
            if (id != null) {
                criteria.add(Restrictions.ne(idName, id));
            }
            // System.out.println(idName + ":" + id);
        } catch (IllegalAccessException e) {
            ReflectionUtils.handleReflectionException(e);
        } catch (NoSuchMethodException e) {
            ReflectionUtils.handleReflectionException(e);
        } catch (InvocationTargetException e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return ((Number) criteria.uniqueResult()).intValue() == 0;
    }

    /**
     * 取得对象的主键值，辅助函数.
     * @param entityClass 实体类型
     * @param entity 实例
     * @return 主键
     * @throws NoSuchMethodException 找不到方法
     * @throws IllegalAccessException 没有访问权限
     * @throws InvocationTargetException 反射异常
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getId(Class<?> entityClass, Object entity)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Assert.notNull(entity, "entity==null");
        Assert.notNull(entityClass, "entityClass==null");
        return (T) PropertyUtils.getProperty(entity, getIdName(entityClass));
    }

    /**
     * 取得对象的主键名,辅助函数.
     * @param entityClass 实体类型
     * @return 主键名称
     */
    protected String getIdName(Class<?> entityClass) {
        Assert.notNull(entityClass, "entityClass==null");
        ClassMetadata meta = getClassMetadata(entityClass);
        Assert.notNull(meta, "Class " + entityClass + " not define in hibernate session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, entityClass.getSimpleName() + " has no identifier property define.");
        return idName;
    }

    /**
     * 取得实体对象的元数据.
     * @param entityClass 实体类型
     * @return 实体对象的元数据
     */
    protected ClassMetadata getClassMetadata(Class<?> entityClass) {
        Assert.notNull(entityClass, "entityClass==null");
        return getSessionFactory().getClassMetadata(entityClass);
    }

    // ~#
    // HibernateCallback
    // DetachedCriteria
}
