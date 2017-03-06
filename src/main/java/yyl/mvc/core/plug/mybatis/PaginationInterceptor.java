package yyl.mvc.core.plug.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yyl.mvc.core.plug.jdbc.dialect.Dialect;
import yyl.mvc.core.plug.jdbc.dialect.DialectConfigurer;

/**
 * 用于MyBatis的分页查询插件.<br>
 * 用于提供数据库的物理分页查询功能.<br>
 * @see org.apache.ibatis.plugin.Interceptor
 * @author _yyl
 */
@Intercepts({ //
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }), //
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) //
})
public class PaginationInterceptor implements Interceptor {

	// ==============================Fields===========================================
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	// ==============================Methods==========================================
	/**
	 * 插件拦截方法
	 * @param ivk MyBatis调用
	 * @return 查询结果
	 */
	@Override
	public Object intercept(Invocation ivk) throws Throwable {

		Object target = ivk.getTarget();

		if (target instanceof RoutingStatementHandler) {
			return interceptStatementHandler(ivk);
		}

		if (target instanceof ResultSetHandler) {
			return interceptResultSetHandler(ivk);
		}

		return ivk.proceed();
	}

	/**
	 * 用于生成代理类
	 * @param target 要拦截的对象
	 * @return 包装类
	 */
	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
		return (target instanceof StatementHandler || target instanceof ResultSetHandler) ? Plugin.wrap(target, this) : target;
	}

	/**
	 * 获得配置的参数
	 * 
	 * @param properties 配置的参数
	 */
	@Override
	public void setProperties(Properties properties) {
	}

	// ==============================ProcessMethods===================================
	protected Object interceptStatementHandler(Invocation ivk) throws Throwable {
		RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
		MetaObject meta = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
		RowBounds rowBounds = (RowBounds) meta.getValue("delegate.rowBounds");

		//BaseStatementHandler delegate =  (BaseStatementHandler)meta.getValue("delegate");;

		meta = getTargetMetaObject(meta);

		MappedStatement ms = (MappedStatement) meta.getValue("delegate.mappedStatement");

		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = StringUtils.trimToEmpty(boundSql.getSql());
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		Dialect dialect = DialectConfigurer.getDialect();
		if (rowBounds instanceof PageBounds) {
			PageBounds pageBounds = (PageBounds) rowBounds;
			pageBounds.totalCount = obtainTotalCount((Connection) ivk.getArgs()[0], ms, boundSql, dialect);
			meta.setValue("delegate.resultSetHandler.rowBounds", new OriginalRowBoundsWrapper(pageBounds));
			meta.setValue("delegate.boundSql.sql", sql = dialect.getLimitSql(sql, offset, limit));
		} else if (rowBounds != RowBounds.DEFAULT && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
			meta.setValue("delegate.resultSetHandler.rowBounds", new OriginalRowBoundsWrapper(rowBounds));
			meta.setValue("delegate.boundSql.sql", sql = dialect.getLimitSql(sql, offset, limit));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("execute:" + ms.getId() + "\n" + sql);
		}
		return ivk.proceed();
	}

	protected Object interceptResultSetHandler(Invocation ivk) throws Throwable {

		ResultSetHandler handler = (ResultSetHandler) ivk.getTarget();//#DefaultResultSetHandler

		MetaObject meta = MetaObject.forObject(handler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

		RowBounds rowBounds = (RowBounds) meta.getValue("rowBounds");

		List<?> records = (List<?>) ivk.proceed();

		if (rowBounds instanceof OriginalRowBoundsWrapper) {
			OriginalRowBoundsWrapper wrapper = (OriginalRowBoundsWrapper) rowBounds;
			if (wrapper.original instanceof PaginationFilters) {
				records = ((PaginationFilters) rowBounds).wrap(records);
			}
		}

		return records;
	}

	// ==============================InnerClass=======================================
	class OriginalRowBoundsWrapper extends RowBounds {

		final RowBounds original;

		OriginalRowBoundsWrapper(RowBounds rowBounds) {
			super();
			this.original = rowBounds;
		}
	}

	// ==============================ToolMethods======================================
	/**
	 * 分离代理对象链(目标类可能被多个拦截器拦截，从而形成多次代理，用于分离出最原始的的目标类)
	 * @param meta 元数据对象
	 * @return 原始目标的元数据对象
	 */
	protected MetaObject getTargetMetaObject(MetaObject meta) {
		while (meta.hasGetter("h")) {//Proxy.h -> InvocationHandler
			Object ih = meta.getValue("h");
			MetaObject ihm = MetaObject.forObject(ih, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
			if (ihm.hasGetter("target")) {//Plugin.target -> object
				Object object = meta.getValue("target");
				meta = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
			}
		}
		return meta;
	}

	/**
	 * 获得查询的记录总数(并将记录总数存储到线程本地变量中)
	 * @param conn 数据库连接
	 * @param ms 映射表达式
	 * @param boundSql 范围SQL
	 * @param dialect 数据库方言
	 * @throws SQLException 如果查询中出现错误则抛出该异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int obtainTotalCount(Connection conn, MappedStatement ms, BoundSql boundSql, Dialect dialect) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String countSql = dialect.getCountSql(boundSql.getSql());
			ps = conn.prepareStatement(countSql);
			BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			ErrorContext.instance().activity("setting parameters").object(ms.getParameterMap().getId());
			List<ParameterMapping> parameterMappings = countBoundSql.getParameterMappings();
			Object parameter = countBoundSql.getParameterObject();
			if (parameterMappings != null) {
				Configuration configuration = ms.getConfiguration();
				TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
				MetaObject metaObject = parameter == null ? null : configuration.newMetaObject(parameter);
				for (int i = 0; i < parameterMappings.size(); i++) {
					ParameterMapping parameterMapping = parameterMappings.get(i);
					if (parameterMapping.getMode() != ParameterMode.OUT) {
						Object value;
						String propertyName = parameterMapping.getProperty();
						PropertyTokenizer prop = new PropertyTokenizer(propertyName);
						if (parameter == null) {
							value = null;
						} else if (typeHandlerRegistry.hasTypeHandler(parameter.getClass())) {
							value = parameter;
						} else if (countBoundSql.hasAdditionalParameter(propertyName)) {
							value = countBoundSql.getAdditionalParameter(propertyName);
						} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && countBoundSql.hasAdditionalParameter(prop.getName())) {
							value = countBoundSql.getAdditionalParameter(prop.getName());
							if (value != null) {
								value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
							}
						} else {
							value = metaObject == null ? null : metaObject.getValue(propertyName);
						}
						TypeHandler typeHandler = parameterMapping.getTypeHandler();
						if (typeHandler == null) {
							throw new ExecutorException(
									"There was no TypeHandler found for parameter " + propertyName + " of statement " + ms.getId());
						}
						typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
					}
				}
			}
			int count = 0;
			if ((rs = ps.executeQuery()).next()) {
				count = rs.getInt(1);
			}
			return count;
		} catch (Exception e) {
			logger.error("!", e);
			return -1;
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
		}
	}
}
