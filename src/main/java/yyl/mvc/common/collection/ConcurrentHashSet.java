package yyl.mvc.common.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 集合对象，通过{@link ConcurrentHashMap}实现的线程安全Set，性能高于 ConcurrentSkipListSet。<br>
 * @see Collections#newSetFromMap
 * @see ConcurrentSkipListSet
 * @param <E> 元素类型
 */
@SuppressWarnings("serial")
public class ConcurrentHashSet<E> extends AbstractSet<E> implements java.io.Serializable {

    // ==============================Fields==============================================
    /** 映射中的值对象，如果值为此对象表示有数据，否则无数据 */
    private static final Object PRESENT = new Object();
    /** 线程安全的Map对象 */
    private final ConcurrentMap<E, Object> map;

    // ==============================Constructors========================================
    /**
     * 构造<br>
     * 触发因子为默认的0.75
     */
    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    /**
     * 构造<br>
     * 触发因子为默认的0.75
     * @param initialCapacity 初始大小
     */
    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    /**
     * 构造
     * @param initialCapacity 初始大小
     * @param loadFactor 加载因子。此参数决定数据增长时触发的百分比
     */
    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * 构造
     * @param initialCapacity 初始大小
     * @param loadFactor 触发因子。此参数决定数据增长时触发的百分比
     * @param concurrencyLevel 线程并发度
     */
    public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * 从已有集合中构造
     * @param iter {@link Iterable}
     */
    public ConcurrentHashSet(Iterable<E> iter) {
        if (iter instanceof Collection) {
            final Collection<E> collection = (Collection<E>) iter;
            map = new ConcurrentHashMap<>((int) (collection.size() / 0.75f));
            this.addAll(collection);
        } else {
            map = new ConcurrentHashMap<>();
            for (E e : iter) {
                this.add(e);
            }
        }
    }

    // ==============================Methods=============================================
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}
