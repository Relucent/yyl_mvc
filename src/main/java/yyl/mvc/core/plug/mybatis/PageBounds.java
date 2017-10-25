package yyl.mvc.core.plug.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import yyl.mvc.core.util.page.Page;
import yyl.mvc.core.util.page.Pagination;
import yyl.mvc.core.util.page.impl.SimplePage;

/**
 * 分页查询范围限定类
 * @author _yyl
 */
public class PageBounds extends RowBounds {

	public static final PageBounds DEFAULT = new PageBounds();

	protected int totalCount = 0;

	public PageBounds() {
		super();
	}

	public PageBounds(int offset, int limit) {
		super(offset, limit);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public static PageBounds of(Pagination pagination) {
		return new PageBounds(pagination.getStart(), pagination.getLimit());
	}

	/**
	 * 封装分页结果
	 * @param 查询到的结果集
	 * @return 封装分页结果
	 */
	public <T> Page<T> wrap(List<T> records) {
		return new SimplePage<T>(getOffset(), getLimit(), records, getTotalCount());
	}
}
