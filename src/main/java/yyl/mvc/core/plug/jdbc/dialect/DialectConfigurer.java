package yyl.mvc.core.plug.jdbc.dialect;

import java.util.concurrent.atomic.AtomicReference;

import yyl.mvc.core.plug.jdbc.dialect.impl.Db2Dialect;
import yyl.mvc.core.plug.jdbc.dialect.impl.MySqlDialect;
import yyl.mvc.core.plug.jdbc.dialect.impl.OracleDialect;

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
				dialect = new MySqlDialect();
			} else if ("db2".equalsIgnoreCase(className)) {
				dialect = new Db2Dialect();
			} else if ("oracle".equalsIgnoreCase(className)) {
				dialect = new OracleDialect();
			} else {
				dialect = (Dialect) Class.forName(className).newInstance();
			}
			setDialect(dialect);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
