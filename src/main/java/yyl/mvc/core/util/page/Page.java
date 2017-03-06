package yyl.mvc.core.util.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询的结果数据(接口)<br>
 */
public interface Page<T> extends Serializable {

	/** 获取从第几条数据开始查询 */
	int getStart();

	/** 获取每页显示条数 */
	int getLimit();

	/** 设置每页显示条数 */
	void setLimit(int limit);

	/** 获取总条数 */
	int getTotal();

	/** 获取当前页数据 */
	List<T> getRecords();
}
