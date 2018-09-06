package yyl.mvc.util.collect;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 增强的Map接口<br>
 * 这个接口继承自Map，但是获得值的时候提供了一些类型转换方法<br>
 * 该类作为数据传输对象使用十分方便<br>
 * @see java.util.Map
 * @author YaoYiLang
 * @version 2012-11-13
 */
public interface Mapx extends Map<String, Object> {
    //===================================Methods=============================================
    public int size();

    public boolean isEmpty();

    public boolean containsKey(Object key);

    public boolean containsValue(Object value);

    public Object get(String key);

    public Object put(String key, Object value);

    public Object remove(Object key);

    public void putAll(Map<? extends String, ? extends Object> m);

    public void clear();

    public Set<String> keySet();

    public Collection<Object> values();

    public Set<Entry<String, Object>> entrySet();

    //===================================Primitive===========================================
    public boolean getPrimitiveBoolean(String key);

    public boolean getPrimitiveBoolean(String key, boolean defBoolean);

    public int getPrimitiveInt(String key);

    public int getPrimitiveInt(String key, int defInt);

    public long getPrimitiveLong(String key);

    public long getPrimitiveLong(String key, long defLong);

    public float getPrimitiveFloat(String key);

    public float getPrimitiveFloat(String key, float defFloat);

    public double getPrimitiveDouble(String key);

    public double getPrimitiveDouble(String key, double defDouble);

    //===================================Wrapped=============================================
    public Boolean getBoolean(String key);

    public Boolean getBoolean(String key, Boolean defBoolean);

    public Integer getInteger(String key);

    public Integer getInteger(String key, Integer defInteger);

    public Long getLong(String key);

    public Long getLong(String key, Long defLong);

    public Float getFloat(String key);

    public Float getFloat(String key, Float defFloat);

    public Double getDouble(String key);

    public Double getDouble(String key, Double defDouble);

    //===================================General=============================================
    public String getString(String key);

    public String getString(String key, String defString);

    public Date getDate(String key);

    public Date getDate(String key, Date defDate);

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType);

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defEnum);

    //===================================Collection==========================================
    public Listx getList(String key);

    public Listx getList(String key, Listx va);

    public Mapx getMap(String key);

    public Mapx getMap(String key, Mapx vo);
    
    public Map<String, Object> toMap();
}
