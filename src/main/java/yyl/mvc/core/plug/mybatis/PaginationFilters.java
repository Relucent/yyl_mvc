package yyl.mvc.core.plug.mybatis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.collect.Mapxs;
import yyl.mvc.core.util.page.Pagination;

/**
 * 分页查询条件参数 包含分页查询的页数，每页最大记录，查询条件，排序条件等信息
 * @author YaoYiLang
 * @version 2015-03-07
 */
@Deprecated
@SuppressWarnings("serial")
public class PaginationFilters extends PageBounds implements Pagination, Map<String, Object> {

	// =================================Constants=============================================
	public static final int DEFAULT_LIMIT = 15;

	// =================================Fields=================================================
	/** 开始查询 的数据索引号 (从0开始) */
	private int start = 0;

	/** 每页条数 */
	private int limit = DEFAULT_LIMIT;

	/** 查询参数 */
	private Mapx filters = Mapxs.newMapx();

	// =================================Constructors===========================================
	/** 构造函数 */
	public PaginationFilters() {
	}

	/**
	 * 构造函数
	 * @param start 查询数据的偏移量
	 * @param limit 查询记录数
	 */
	public PaginationFilters(int start, int limit) {
		super(start, limit);
		this.start = start;
		this.limit = limit;
	}

	/**
	 * 构造函数
	 * @param start 查询数据的偏移量
	 * @param limit 查询记录数
	 * @param filters 查询参数
	 */
	public PaginationFilters(int start, int limit, Mapx filters) {
		super(start, limit);
		this.start = start;
		this.limit = limit;
		this.filters = filters;
	}

	// =================================ToolMethods============================================
	/**
	 * 封装分页结果
	 * @param 查询到的结果集
	 * @return 封装分页结果
	 */
	@Override
	public <T> PageList<T> wrap(List<T> records) {
		return new PageList<T>(getStart(), getLimit(), records, getTotalCount());
	}

	// =================================Methods================================================
	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setFilters(Mapx filters) {
		this.filters = filters;
	}

	// =================================OverrideMethods========================================
	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public Mapx getFilters() {
		return filters;
	}

	@Deprecated
	@Override
	public int getOffset() {
		return start;
	}

	// =================================Overrides==============================================
	@Override
	public int size() {
		return filters.size();
	}

	@Override
	public boolean isEmpty() {
		return filters.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return filters.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return filters.containsValue(value);
	}

	@Override
	public Object put(String key, Object value) {
		return filters.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return filters.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		filters.putAll(m);
	}

	@Override
	public void clear() {
		filters.clear();
	}

	@Override
	public Set<String> keySet() {
		return filters.keySet();
	}

	@Override
	public Collection<Object> values() {
		return filters.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return filters.entrySet();
	}

	@Override
	public Object get(Object key) {
		return filters.get(key);
	}

	// =================================HashCode_Equals========================================
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + start;
		result = prime * result + limit;
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
		PaginationFilters other = (PaginationFilters) o;
		return (filters == other.filters || (filters != null && filters.equals(other.filters))) //
				&& start == other.start//
				&& limit == other.limit;
	}
}