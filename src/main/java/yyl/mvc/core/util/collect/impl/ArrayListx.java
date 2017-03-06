package yyl.mvc.core.util.collect.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import yyl.mvc.core.util.collect.Listx;
import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.convert.ConvertUtil;

/**
 * 增强版List接口的实现类。<br>
 * @see java.util.List
 * @author YYL
 */
public class ArrayListx implements Listx {

	// ===================================Fields==============================================
	protected List<Object> data = new ArrayList<Object>();

	// ===================================Methods=============================================

	@Override
	public boolean add(Object e) {
		return data.add(convertValidity(e));
	}

	@Override
	public void add(int index, Object element) {
		if (index < size()) {
			this.data.add(index, element);
		} else {
			while (index != size()) {
				add((Object) null);
			}
			add(element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		return data.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		return data.addAll(index, c);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public Object get(int index) {
		return data.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {
		return data.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return data.lastIndexOf(o);
	}

	@Override
	public ListIterator<Object> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return data.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return data.remove(o);
	}

	@Override
	public Object remove(int index) {
		return data.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return data.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return data.retainAll(c);
	}

	@Override
	public Object set(int index, Object element) {
		if (index < size()) {
			return this.data.set(index, element);
		} else {
			while (index != size()) {
				add((Object) null);
			}
			add(element);
			return null;
		}
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public List<Object> subList(int fromIndex, int toIndex) {
		ArrayListx va = new ArrayListx();
		va.addAll(data.subList(fromIndex, toIndex));
		return va;
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	// ===================================Primitive===========================================
	public boolean getPrimitiveBoolean(int index) {
		return getPrimitiveBoolean(index, false);
	}

	public int getPrimitiveInt(int index) {
		return getPrimitiveInt(index, 0x0);
	}

	public long getPrimitiveLong(int index) {
		return getPrimitiveLong(index, 0L);
	}

	public float getPrimitiveFloat(int index) {
		return getPrimitiveFloat(index, 0.0F);
	}

	public double getPrimitiveDouble(int index) {
		return getPrimitiveDouble(index, 0.0D);
	}

	public boolean getPrimitiveBoolean(int index, boolean defaultBoolean) {
		return get(index, Boolean.TYPE, defaultBoolean);
	}

	public int getPrimitiveInt(int index, int defaultInt) {
		return get(index, Integer.TYPE, defaultInt);
	}

	public long getPrimitiveLong(int index, long defaultLong) {
		return get(index, Long.TYPE, defaultLong);
	}

	public float getPrimitiveFloat(int index, float defaultFloat) {
		return get(index, Float.TYPE, defaultFloat);
	}

	public double getPrimitiveDouble(int index, double defaultDouble) {
		return get(index, Double.TYPE, defaultDouble);
	}

	// ===================================Wrapped=============================================
	public Boolean getBoolean(int index) {
		return getBoolean(index, null);
	}

	public Integer getInteger(int index) {
		return getInteger(index, null);
	}

	public Long getLong(int index) {
		return getLong(index, null);
	}

	public Float getFloat(int index) {
		return getFloat(index, null);
	}

	public Double getDouble(int index) {
		return getDouble(index, null);
	}

	// ~
	public Boolean getBoolean(int index, Boolean vDefault) {
		return get(index, Boolean.class, vDefault);
	}

	public Integer getInteger(int index, Integer vDefault) {
		return get(index, Integer.class, vDefault);
	}

	public Long getLong(int index, Long vDefault) {
		return get(index, Long.class, vDefault);
	}

	public Float getFloat(int index, Float vDefault) {
		return get(index, Float.class, vDefault);
	}

	public Double getDouble(int index, Double vDefault) {
		return get(index, Double.class, vDefault);
	}

	// ===================================General=============================================
	public String getString(int index) {
		return getString(index, null);
	}

	public String getString(int index, String vDefault) {
		return get(index, String.class, vDefault);
	}

	public Date getDate(int index) {
		return getDate(index, null);
	}

	public Date getDate(int index, Date defaultDate) {
		return get(index, Date.class, defaultDate);
	}

	public <T extends Enum<T>> T getEnum(int index, Class<T> enumType) {
		return getEnum(index, enumType, null);
	}

	public <T extends Enum<T>> T getEnum(int index, Class<T> enumType, T defaultEnum) {
		return get(index, enumType, defaultEnum);
	}

	// ===================================Collection==========================================
	public Listx getList(int index) {
		return getList(index, null);
	}

	public Listx getList(int index, Listx defaultList) {
		return get(index, Listx.class, defaultList);
	}

	public Mapx getMap(int index) {
		return getMap(index, null);
	}

	public Mapx getMap(int index, Mapx defaultMap) {
		return get(index, Mapx.class, defaultMap);
	}

	// ===================================Standard============================================
	private <T> T get(int index, Class<T> toType, T _default) {
		return ConvertUtil.convert(get(index), toType, _default);
	}

	// ==================================SerializableString====================================
	@Override
	public String toString() {
		return data.toString();
	}

	// ===================================Private_Methods=====================================
	private final static Object convertValidity(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Double) {
			if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
				return null;
			}
		}
		if (o instanceof Float) {
			if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
				return null;
			}
		}
		return o;
	}

}
