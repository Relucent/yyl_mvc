package yyl.mvc.core.util.page.impl;

import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.collect.Mapxs;
import yyl.mvc.core.util.page.Pagination;

/**
 * 分页查询条件参数 包含分页查询的页数，每页最大记录，查询条件，排序条件等信息
 * @author YaoYiLang
 * @version 2010-10-10
 */

@SuppressWarnings("serial")
public class SimplePagination implements Pagination {

	// =================================Fields=================================================
	/** 开始查询 的数据索引号 (从0开始) */
	private int start = 0;

	/** 每页条数 */
	private int limit = 25;

	/** 查询参数 */
	private Mapx filters = Mapxs.newMapx();

	// =================================Constructors===========================================
	/**
	 * 构造函数
	 */
	public SimplePagination() {
	}

	// =================================Methods================================================

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Mapx getFilters() {
		return filters;
	}

	public void setFilters(Mapx filters) {
		this.filters = filters;
	}

	// =================================HashCode_Equals========================================
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
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
		SimplePagination other = (SimplePagination) o;
		return (filters == other.filters || (filters != null && filters.equals(other.filters))) //
				&& limit == other.limit //
				&& start == other.start;
	}
}
