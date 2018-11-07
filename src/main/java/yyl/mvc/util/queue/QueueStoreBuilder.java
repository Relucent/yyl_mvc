package yyl.mvc.util.queue;

@FunctionalInterface
public interface QueueStoreBuilder<T> {
    QueueStore<T> build();
}
