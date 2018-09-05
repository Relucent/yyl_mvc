package yyl.mvc.plug.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import yyl.mvc.plug.jdbc.impl.Db2Dialect;
import yyl.mvc.plug.jdbc.impl.HsqldbDialect;
import yyl.mvc.plug.jdbc.impl.InformixDialect;
import yyl.mvc.plug.jdbc.impl.MySqlDialect;
import yyl.mvc.plug.jdbc.impl.OracleDialect;

/**
 * 数据库方言路由工具类
 * @author YYL
 */
public class DialectRouteUtil {

    private static final Map<String, Class<? extends Dialect>> DIALECT_ALIAS_MAP = new ConcurrentHashMap<>();
    private static final Map<String, Dialect> URL_DIALECT_MAP = new ConcurrentHashMap<>();

    static {
        // HSQLDB
        DIALECT_ALIAS_MAP.put("hsqldb", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("h2", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("postgresql", HsqldbDialect.class);
        DIALECT_ALIAS_MAP.put("phoenix", HsqldbDialect.class);
        // MYSQL
        DIALECT_ALIAS_MAP.put("mysql", MySqlDialect.class);
        DIALECT_ALIAS_MAP.put("mariadb", MySqlDialect.class);
        DIALECT_ALIAS_MAP.put("sqlite", MySqlDialect.class);
        // ORACLE
        DIALECT_ALIAS_MAP.put("oracle", OracleDialect.class);
        DIALECT_ALIAS_MAP.put("dm", OracleDialect.class);
        // DB2
        DIALECT_ALIAS_MAP.put("db2", Db2Dialect.class);
        // INFORMIX
        DIALECT_ALIAS_MAP.put("informix", InformixDialect.class);
        DIALECT_ALIAS_MAP.put("informix-sqli", InformixDialect.class);
    }

    /**
     * 根据数据库连接获取数据库方言
     * @param conn 数据库连接
     * @return 数据库方言
     * @throws SQLException
     */
    public static Dialect getDialect(Connection conn) throws SQLException {
        String url = conn.getMetaData().getURL();
        Dialect dialect = URL_DIALECT_MAP.get(url);
        if (dialect != null) {
            return dialect;
        }
        dialect = getDialectByJdbcUrl(url);
        URL_DIALECT_MAP.put(url, dialect);
        return dialect;
    }

    /**
     * 根据数据库连接获得数据库方言
     * @param jdbcUrl 数据库连接
     * @return 数据库方言
     */
    private static Dialect getDialectByJdbcUrl(String jdbcUrl) throws SQLException {
        Class<? extends Dialect> dialectClass = null;
        for (Map.Entry<String, Class<? extends Dialect>> entry : DIALECT_ALIAS_MAP.entrySet()) {
            String alias = entry.getKey();
            if (jdbcUrl.indexOf(":" + alias + ":") != -1) {
                dialectClass = entry.getValue();
                break;
            }
        }
        if (dialectClass == null) {
            throw new SQLException("Can't find jdbc-dialect of " + jdbcUrl);
        }
        try {
            return dialectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SQLException(e);
        }
    }
}
