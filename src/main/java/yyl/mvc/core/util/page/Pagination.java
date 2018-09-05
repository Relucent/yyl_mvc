package yyl.mvc.core.util.page;

import java.io.Serializable;

/**
 * 分页查询条件参数： 包含分页查询的页数，每页最大记录，查询条件，排序条件等信息。<br>
 * @author YYL
 * @version 2010-10-10
 */
@SuppressWarnings("serial")
public class Pagination implements Serializable {

    // =================================Constants=============================================
    public static final int DEFAULT_LIMIT = 20;

    // =================================Fields=================================================
    /** 开始查询 的数据索引号 (从0开始) */
    private int start = 0;

    /** 每页条数 */
    private int limit = DEFAULT_LIMIT;

    // =================================Constructors===========================================
    /**
     * 构造函数
     */
    public Pagination() {
        this(0, DEFAULT_LIMIT);
    }

    /**
     * 构造函数
     * @param start 查询数据开始索引
     * @param limit 查询记录数
     */
    public Pagination(int start, int limit) {
        this.start = start;
        this.limit = limit;
    }

    // =================================Methods================================================
    /**
     * 获取从第几条数据开始查询
     * @return 查询的偏移量
     */
    public int getStart() {
        return start;
    }

    /**
     * 设置从第几条数据开始查询
     * @param start 查询的偏移量
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 获取每页显示记录数
     * @return 每页显示记录数
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 设置每页显示记录数
     * @param limit 每页显示记录数
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    // =================================HashCode_Equals========================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + limit;
        result = prime * result + start;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pagination other = (Pagination) o;
        return limit == other.limit && start == other.start;
    }
}
