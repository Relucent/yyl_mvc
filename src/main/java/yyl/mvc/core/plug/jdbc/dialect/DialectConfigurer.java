package yyl.mvc.core.plug.jdbc.dialect;

import java.util.concurrent.atomic.AtomicReference;

import yyl.mvc.core.plug.jdbc.dialect.impl.Db2Dialect;
import yyl.mvc.core.plug.jdbc.dialect.impl.MySqlDialect;
import yyl.mvc.core.plug.jdbc.dialect.impl.OracleDialect;
import yyl.mvc.core.plug.jdbc.dialect.impl.PostgreSqlDialect;

public class DialectConfigurer {

	// ==============================Fields===========================================
	private static final AtomicReference<Dialect> DIALECT = new AtomicReference<Dialect>();

	// ==============================Methods==========================================
	/**
	 * 获得方言
	 * @return JDBC方言
	 */
	public static Dialect getDialect() {
		return DIALECT.get();
	}

	// ==============================Spring_Ioc=======================================
	/**
	 * 设置方言
	 * @param dialect 方言
	 */
	public void setDialect(Dialect dialect) {
		DIALECT.set(dialect);
	}

	/**
	 * 设置方言Class
	 * @param className 方言CLASS
	 */
	public void setDialectClass(String className) {
		try {
			Dialect dialect = null;
			if ("mysql".equalsIgnoreCase(className)) {
				dialect = MySqlDialect.INSTANCE;
			} else if ("db2".equalsIgnoreCase(className)) {
				dialect = Db2Dialect.INSTANCE;
			} else if ("oracle".equalsIgnoreCase(className)) {
				dialect = OracleDialect.INSTANCE;
			} else if ("postgresql".equalsIgnoreCase(className)) {
				dialect = PostgreSqlDialect.INSTANCE;
			} else {
				dialect = (Dialect) Class.forName(className).newInstance();
			}
			setDialect(dialect);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
