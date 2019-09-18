package yyl.mvc.common.queue.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import yyl.mvc.common.queue.Distinct;
import yyl.mvc.common.queue.QueueStore;
import yyl.mvc.common.queue.QueueStoreBuilder;
import yyl.mvc.common.queue.QueueStoreManager;

/**
 * 队列管理器
 */
public class SimpleQueueStoreManager<T> implements QueueStoreManager<T> {

    private final ConcurrentMap<String, QueueStore<T>> queueMap = new ConcurrentHashMap<>(16);
    private final QueueStoreBuilder<T> queueBuilder = null;
    private final boolean dynamic;

    public SimpleQueueStoreManager() {
        this(NoneDistinct.instance());
    }

    public SimpleQueueStoreManager(Distinct<T> distinct) {
        this(distinct, (String[]) null);
    }

    public SimpleQueueStoreManager(QueueStoreBuilder<T> queueBuilder) {
        this(queueBuilder, (String[]) null);
    }

    public SimpleQueueStoreManager(Distinct<T> distinct, String... cacheNames) {
        this(new QueueStoreBuilder<T>() {
            @Override
            public QueueStore<T> build() {
                return new SimpleQueueStore<>(distinct);
            }
        }, cacheNames);
    }

    public SimpleQueueStoreManager(QueueStoreBuilder<T> queueBuilder, String... cacheNames) {
        if (ArrayUtils.isNotEmpty(cacheNames)) {
            Stream.of(cacheNames).distinct().forEach(name -> this.queueMap.put(name, createQueue(name)));
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    @Override
    public QueueStore<T> getQueue(String name) {
        QueueStore<T> queue = this.queueMap.get(name);
        if (queue == null && this.dynamic) {
            synchronized (this.queueMap) {
                queue = this.queueMap.get(name);
                if (queue == null) {
                    queue = createQueue(name);
                    this.queueMap.put(name, queue);
                }
            }
        }
        return queue;
    }

    /**
     * 移除队列
     * @param name 队列名称
     * @return 被移除的队列
     */
    public QueueStore<T> removeQueue(String name) {
        return queueMap.remove(name);
    }

    /**
     * 返回队列管理器中可用队列名称列表
     * @return 队列管理器中可用队列名称列表
     */
    @Override
    public Collection<String> getQueueStoreNames() {
        return Collections.unmodifiableSet(queueMap.keySet());
    }

    /**
     * 创建新的队列实例
     * @param name 队列名称
     * @return the 队列实现类
     */
    protected QueueStore<T> createQueue(String name) {
        if (queueBuilder == null) {
            return new SimpleQueueStore<T>();
        } else {
            return queueBuilder.build();
        }
    }
}
