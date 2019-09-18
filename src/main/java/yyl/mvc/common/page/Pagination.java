package yyl.mvc.common.page;

import java.io.Serializable;

/**
 * 分页查询条件参数<br>
 * @author YYL
 * @version 2010-10-10
 */
public interface Pagination extends Serializable {

    /** 默认每页记录条数 */
    int DEFAULT_LIMIT = 20;

    /**
     * 获取从第几条数据开始查询
     * @return 查询的偏移量
     */
    int getOffset();

    /**
     * 获取每页显示记录数
     * @return 每页显示记录数
     */
    int getLimit();
}
