package yyl.mvc.plug.mybatis;

import java.util.List;
import java.util.function.Supplier;

import yyl.mvc.util.page.SimplePage;
import yyl.mvc.util.jdbc.Dialect;
import yyl.mvc.util.page.Pagination;

/**
 * _Mybatis 分页工具类<br/>
 * @author YYL
 */
public class MybatisHelper {

    private static ThreadLocal<Pagination> PAGINATION_HOLDER = new ThreadLocal<>();
    private static ThreadLocal<Long> TOTAL_HOLDER = new ThreadLocal<>();

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param select 查询方法
     * @return 分页查询结果
     */
    public static <T> SimplePage<T> selectPage(Pagination pagination, Select<T> select) {
        try {
            PAGINATION_HOLDER.set(pagination);
            TOTAL_HOLDER.set(-1L);
            List<T> records = select.get();
            int offset = pagination.getOffset();
            int limit = pagination.getLimit();
            long total = TOTAL_HOLDER.get();
            return new SimplePage<T>(offset, limit, records, total);
        } finally {
            release();
        }
    }

    /**
     * 获得数据库方言
     * @return 数据库方言
     */
    protected Dialect getCurrentDialect() {
        return null;
    }

    /**
     * 设置当前分页查询记录总数
     * @param total 记录总数
     */
    protected static void setTotalCount(long total) {
        TOTAL_HOLDER.set(total);
    }

    /**
     * 获得当前分页条件
     * @return 分页条件
     */
    protected static Pagination getCurrentPagination() {
        return PAGINATION_HOLDER.get();
    }

    /**
     * 释放资源
     */
    private static void release() {
        TOTAL_HOLDER.remove();
        PAGINATION_HOLDER.remove();
    }

    /** 查询方法 */
    @FunctionalInterface
    public static interface Select<T> extends Supplier<List<T>> {}
}
