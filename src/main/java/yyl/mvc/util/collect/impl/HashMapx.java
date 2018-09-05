package yyl.mvc.util.collect.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;
import yyl.mvc.util.convert.ConvertUtil;

/**
 * 增强版Map接口的实现类。<br>
 * @see java.util.List
 * @author YYL
 */
@SuppressWarnings("serial")
public class HashMapx implements Mapx, Serializable {
	// ===================================Fields==============================================
	protected Map<String, Object> data = new LinkedHashMap<String, Object>();

	// ===================================Methods=============================================

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return data.entrySet();
	}

	@Override
	public Object get(String key) {
		return data.get(key);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		return data.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return data.remove(key);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public Collection<Object> values() {
		return data.values();
	}

	@Override
	public Object get(Object key) {
		return data.get(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		data.putAll(m);
	}

	// ===================================Primitive===========================================
	public boolean getPrimitiveBoolean(String key) {
		return getPrimitiveBoolean(key, false);
	}

	public int getPrimitiveInt(String key) {
		return getPrimitiveInt(key, 0x0);
	}

	public long getPrimitiveLong(String key) {
		return getPrimitiveLong(key, 0L);
	}

	public float getPrimitiveFloat(String key) {
		return getPrimitiveFloat(key, 0.0F);
	}

	public double getPrimitiveDouble(String key) {
		return getPrimitiveDouble(key, 0.0D);
	}

	public boolean getPrimitiveBoolean(String key, boolean defaultBoolean) {
		return get(key, Boolean.TYPE, defaultBoolean);
	}

	public int getPrimitiveInt(String key, int defaultInt) {
		return get(key, Integer.TYPE, defaultInt);
	}

	public long getPrimitiveLong(String key, long defaultLong) {
		return get(key, Long.TYPE, defaultLong);
	}

	public float getPrimitiveFloat(String key, float defaultFloat) {
		return get(key, Float.TYPE, defaultFloat);
	}

	public double getPrimitiveDouble(String key, double defaultDouble) {
		return get(key, Double.TYPE, defaultDouble);
	}

	// ===================================Wrapped=============================================
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Float getFloat(String key) {
		return getFloat(key, null);
	}

	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	// ~
	public Boolean getBoolean(String key, Boolean vDefault) {
		return get(key, Boolean.class, vDefault);
	}

	public Integer getInteger(String key, Integer vDefault) {
		return get(key, Integer.class, vDefault);
	}

	public Long getLong(String key, Long vDefault) {
		return get(key, Long.class, vDefault);
	}

	public Float getFloat(String key, Float vDefault) {
		return get(key, Float.class, vDefault);
	}

	public Double getDouble(String key, Double vDefault) {
		return get(key, Double.class, vDefault);
	}

	// ===================================General=============================================
	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String vDefault) {
		return get(key, String.class, vDefault);
	}

	public Date getDate(String key) {
		return getDate(key, null);
	}

	public Date getDate(String key, Date defaultDate) {
		return get(key, Date.class, defaultDate);
	}

	public <T extends Enum<T>> T getEnum(String key, Class<T> enumType) {
		return getEnum(key, enumType, null);
	}

	public <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defaultEnum) {
		return get(key, enumType, defaultEnum);
	}

	// ===================================Collection==========================================
	public Listx getList(String key) {
		return getList(key, null);
	}

	public Listx getList(String key, Listx defaultList) {
		return get(key, Listx.class, defaultList);
	}

	public Mapx getMap(String key) {
		return getMap(key, null);
	}

	public Mapx getMap(String key, Mapx defaultMap) {
		return get(key, Mapx.class, defaultMap);
	}

	// ===================================Standard============================================
	private <T> T get(String key, Class<T> toType, T _default) {
		return ConvertUtil.convert(get(key), toType, _default);
	}

	public Map<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.putAll(data);
		return map;
	}

	// ==================================SerializableString====================================
	@Override
	public String toString() {
		return data.toString();
	}
}
