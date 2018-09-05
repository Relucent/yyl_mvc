/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package yyl.mvc.plug.mybatis;

import java.util.List;
import java.util.function.Supplier;

import yyl.mvc.plug.jdbc.Dialect;
import yyl.mvc.util.page.Page;
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
    public static <T> Page<T> selectPage(Pagination pagination, Select<T> select) {
        try {
            PAGINATION_HOLDER.set(pagination);
            TOTAL_HOLDER.set(-1L);
            List<T> records = select.get();
            int start = pagination.getStart();
            int limit = pagination.getLimit();
            long total = TOTAL_HOLDER.get();
            return new Page<T>(start, limit, records, total);
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
