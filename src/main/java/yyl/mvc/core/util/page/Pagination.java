package yyl.mvc.core.util.page;

import java.io.Serializable;

import yyl.mvc.core.util.collect.Mapx;

/**
 * 分页查询条件参数(接口) 包含分页查询的页数，每页最大记录，查询条件，排序条件等信息
 * @author YaoYiLang
 * @version 2010-10-10
 */
public interface Pagination extends Serializable {

	/** 获取从第几条数据开始查询 */
	public int getStart();

	/** 获取每页显示条数 */
	public int getLimit();

	/** 查询过滤条件 */
	public Mapx getFilters();

}
