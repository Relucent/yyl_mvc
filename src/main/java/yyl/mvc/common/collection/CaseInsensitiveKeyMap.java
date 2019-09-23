package yyl.mvc.common.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 忽略键大小写的 MAP <br>
 * 内部实现会将键转换为小写，基于HashMap，不保证线程安全 <br>
 */
public class CaseInsensitiveKeyMap<V> implements Map<String, V> {

    // =================================Fields=================================================
    private final Map<String, V> map;

    // =================================Constructs=============================================
    /**
     * 构造函数
     */
    public CaseInsensitiveKeyMap() {
        map = new HashMap<>();
    }

    /**
     * 构造函数
     * @param initialCapacity 初始大小
     * @param loadFactor 负载因子
     */
    public CaseInsensitiveKeyMap(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * 构造函数
     * @param sample 模板映射表
     */
    public CaseInsensitiveKeyMap(Map<? extends String, ? extends V> sample) {
        map = new HashMap<>(sample.size());
        putAll(sample);
    }

    // =================================Methods================================================
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        String convertedKey = convertKey(key);
        return map.containsKey(convertedKey);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        String convertedKey = convertKey(key);
        return map.get(convertedKey);
    }

    @Override
    public V put(String key, V value) {
        String convertedKey = convertKey(key);
        return map.put(convertedKey, value);
    }

    @Override
    public V remove(Object key) {
        String convertedKey = convertKey(key);
        return map.remove(convertedKey);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        for (Map.Entry<? extends String, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /**
     * 将输入键转换为另一个对象以存储在映射中。
     * @param key 转换的键
     * @return 转换后的键
     */
    protected String convertKey(Object key) {
        if (key == null) {
            return "";
        }
        final char[] chars = key.toString().toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            chars[i] = Character.toLowerCase(Character.toUpperCase(chars[i]));
        }
        return new String(chars);
    }
}
