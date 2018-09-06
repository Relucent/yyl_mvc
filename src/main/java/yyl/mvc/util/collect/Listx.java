package yyl.mvc.util.collect;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 增强的List接口<br>
 * 这个接口继承自List，但是获得值的时候提供了一些类型转换方法<br>
 * 该类作为数据传输对象使用十分方便<br>
 * @see java.util.List
 * @author YaoYiLang
 * @version 2012-11-13
 */
public interface Listx extends List<Object> {
    //===================================Methods=============================================
    public int size();

    public boolean isEmpty();

    public boolean contains(Object o);

    public Iterator<Object> iterator();

    public Object[] toArray();

    public <T> T[] toArray(T[] a);

    public boolean add(Object e);

    public boolean remove(Object o);

    public boolean containsAll(Collection<?> c);

    public boolean addAll(Collection<? extends Object> c);

    public boolean addAll(int index, Collection<? extends Object> c);

    public boolean removeAll(Collection<?> c);

    public boolean retainAll(Collection<?> c);

    public void clear();

    public Object get(int index);

    public Object set(int index, Object element);

    public void add(int index, Object element);

    public Object remove(int index);

    public int indexOf(Object o);

    public int lastIndexOf(Object o);

    public ListIterator<Object> listIterator();

    public ListIterator<Object> listIterator(int index);

    public List<Object> subList(int fromIndex, int toIndex);

    //===================================Primitive===========================================
    public boolean getPrimitiveBoolean(int index);

    public boolean getPrimitiveBoolean(int index, boolean defaultBoolean);

    public int getPrimitiveInt(int index);

    public int getPrimitiveInt(int index, int defaultInt);

    public long getPrimitiveLong(int index);

    public long getPrimitiveLong(int index, long defaultLong);

    public float getPrimitiveFloat(int index);

    public float getPrimitiveFloat(int index, float defaultFloat);

    public double getPrimitiveDouble(int index);

    public double getPrimitiveDouble(int index, double defaultDouble);

    //===================================Wrapped=============================================
    public Boolean getBoolean(int index, Boolean defBoolean);

    public Boolean getBoolean(int index);

    public Integer getInteger(int index);

    public Integer getInteger(int index, Integer defInteger);

    public Long getLong(int index);

    public Long getLong(int index, Long defLong);

    public Float getFloat(int index);

    public Float getFloat(int index, Float defFloat);

    public Double getDouble(int index);

    public Double getDouble(int index, Double defDouble);

    //===================================General=============================================
    public String getString(int index);

    public String getString(int index, String defString);

    public Date getDate(int index);

    public Date getDate(int index, Date defDate);

    public <T extends Enum<T>> T getEnum(int index, Class<T> enumType);

    public <T extends Enum<T>> T getEnum(int index, Class<T> enumType, T defEnum);

    //===================================Collection==========================================
    public Listx getList(int index);

    public Listx getList(int index, Listx va);

    public Mapx getMap(int index);

    public Mapx getMap(int index, Mapx vo);
}
