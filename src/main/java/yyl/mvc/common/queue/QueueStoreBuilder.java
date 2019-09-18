package yyl.mvc.common.queue;

@FunctionalInterface
public interface QueueStoreBuilder<T> {
    QueueStore<T> build();
}
