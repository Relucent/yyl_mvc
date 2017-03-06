package yyl.mvc.core.plug.ldap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * LDAP条目项实体类
 * @version  2012-10-13 1.0
 */
public class LdapEntry {

	private final String dn;
	private final Map<String, List<Object>> attributes = Maps.newHashMap();

	public LdapEntry(String dn) {
		this.dn = dn;
	}

	public String getDn() {
		return dn;
	}

	public void put(String key, Object value) {
		if (value != null) {
			attributes.put(convertKey(key), Lists.<Object> newArrayList(value));
		}
	}
	public void put(String key, Number value) {
		if (value != null) {
			put(key, value.toString());
		}
	}

	public void putAll(String key, List<Object> values) {
		attributes.put(convertKey(key), values);
	}

	public Object get(String key) {
		List<Object> values = getAll(key);
		return values == null || values.isEmpty() ? null : values.get(0);
	}

	public List<Object> getAll(String key) {
		return attributes.get(convertKey(key));
	}

	public void remove(String key) {
		attributes.remove(convertKey(key));
	}

	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	public Set<String> keySet() {
		return attributes.keySet();
	}

	protected String convertKey(Object key) {
		if (key != null) {
			return key.toString().toLowerCase();
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "dn:" + dn;
	}
}
