package yyl.mvc.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询的结果数据 <br>
 * @author YYL
 * @version 2010-10-11
 */
@SuppressWarnings("serial")
public class SimplePage<T> implements Page<T> {

    // =================================Fields================================================
    /** 开始查询 的数据索引号 (从0开始) */
    private int offset = 0;
    /** 每页条数 */
    private int limit = Pagination.DEFAULT_LIMIT;
    /** 总记录数 */
    private long total = 0;
    /** 当前页数据 */
    private List<T> records;

    // =================================Constructors===========================================
    /**
     * 构造函数
     */
    public SimplePage() {
        this.records = new ArrayList<T>();
    }

    /**
     * 构造函数
     * @param records 当前页数据
     */
    public SimplePage(List<T> records) {
        this(0, records.size(), records, records.size());
    }

    /**
     * 构造函数
     * @param offset 记录开始索引号
     * @param limit 页面最大记录数
     * @param records 当前页数据
     * @param total 总记录数
     */
    public SimplePage(int offset, int limit, List<T> records, long total) {
        this.offset = offset;
        this.limit = limit;
        this.records = records;
        this.total = total;
    }

    // =================================Methods================================================
    /**
     * 获取从第几条数据开始查询
     * @return 开始查询索引
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 设置从第几条数据开始查询
     * @param offset 开始查询索引
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * 获取每页查询记录数
     * @return 每页查询记录数
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 设置每页显示条数
     * @param limit 每页查询记录数
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 获取总记录数
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     * @param total 总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }



    /**
     * 获取当前页数据
     * @return 当前页数据
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * 设置当前页数据
     * @param records 设置页数据
     */
    public void setRecords(List<T> records) {
        this.records = records;
    }
}
