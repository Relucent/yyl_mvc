package yyl.mvc.core.util.page;

import java.util.List;

import yyl.mvc.core.util.page.impl.SimplePage;

/**
 * 分页查询帮助类，提供一些分页查询需要的计算方法.
 * @author YYL
 * @version 0.1 2012-10-08
 */
public class PageUtil {

	// =================================Static#Fields==========================================

	// ================================Static#Methods=========================================
	/** 根据当前页第一条记录数和每页最大记录数计算出当前页数* */
	public static int getPageNo(int start, int pageSize) {
		return (start / pageSize) + 1;
	}

	/** 计算本页第一条记录的索引* */
	public static int getStart(int pageNo, int pageSize) {
		if ((pageNo < 1) || (pageSize < 1)) {
			return -1;
		} else {
			return (pageNo - 1) * pageSize;
		}
	}

	/** 计算最大页数* */
	public static int getPageCount(int count, int pageSize) {
		if ((count < 0) || (pageSize < 1)) {
			return -1;
		} else {
			return (int) ((count - 1) / pageSize) + 1;
		}
	}

	/**
	 * 创建一个Page对象
	 * @param start 记录开始索引号
	 * @param limit 页面最大记录数
	 * @param records 当前页数据
	 * @param total 总记录数
	 */
	public static <T> Page<T> newSimplePage(int start, int limit, List<T> records, int total) {
		return new SimplePage<T>(start, limit, records, total);
	}

	//	public static Page<T> cast(Page<T> page, RecordConverter converter) {
	//		return null;
	//	}
}
