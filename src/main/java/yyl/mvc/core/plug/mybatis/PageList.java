package yyl.mvc.core.plug.mybatis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import yyl.mvc.core.util.page.Page;

/**
 * 分页查询的结果数据.<br>
 */
@SuppressWarnings("serial")
public class PageList<T> implements Page<T>, List<T> {

	// =================================Fields================================================
	/** 开始查询 的数据索引号 (从0开始) */
	private int start = 0;
	/** 每页条数 */
	private int limit = 15;
	/** 总记录数 */
	private int total = 0;
	/** 当前页数据 */
	private List<T> records;

	// =================================Constructors===========================================
	/**
	 * 构造函数
	 */
	public PageList() {
		this.records = new ArrayList<T>();
	}

	/**
	 * 构造函数
	 * @param start 记录开始索引号
	 * @param limit 页面最大记录数
	 * @param records 当前页数据
	 * @param total 总记录数
	 */
	public PageList(int start, int limit, List<T> records, int total) {
		this.start = start;
		this.limit = limit;
		this.records = records;
		this.total = total;
	}

	// =================================Methods================================================
	/** 获取从第几条数据开始查询 */
	public int getStart() {
		return start;
	}

	/** 设置从第几条数据开始查询 */
	public void setStart(int start) {
		this.start = start;
	}

	/** 获取每页显示条数 */
	public int getLimit() {
		return limit;
	}

	/** 设置每页显示条数 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/** 设置总条数 */
	public void setTotal(int total) {
		this.total = total;
	}

	/** 获取总条数 */
	public int getTotal() {
		return total;
	}

	/** 获取当前页数据 */
	public List<T> getRecords() {
		return records;
	}

	/** 设置当前页数据 */
	public void setRecords(List<T> records) {
		this.records = records;
	}

	// =================================Overrides==============================================
	@Override
	public int size() {
		return records.size();
	}

	@Override
	public boolean isEmpty() {
		return records.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return records.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return records.iterator();
	}

	@Override
	public Object[] toArray() {
		return records.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return records.<E> toArray(a);
	}

	@Override
	public boolean add(T e) {
		return records.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return records.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return records.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return records.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return records.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return records.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return records.retainAll(c);
	}

	@Override
	public void clear() {
		records.clear();
	}

	@Override
	public boolean equals(Object o) {
		return records.equals(o);
	}

	@Override
	public int hashCode() {
		return records.hashCode();
	}

	@Override
	public T get(int index) {
		return records.get(index);
	}

	@Override
	public T set(int index, T element) {
		return records.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		records.add(index, element);
	}

	@Override
	public T remove(int index) {
		return records.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return records.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return records.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return records.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return records.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return records.subList(fromIndex, toIndex);
	}
}
