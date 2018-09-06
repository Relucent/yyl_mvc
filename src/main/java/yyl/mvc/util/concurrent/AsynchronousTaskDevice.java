package yyl.mvc.util.concurrent;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步任务处理器<br>
 */
public class AsynchronousTaskDevice<P, R> {

	// ==============================Fields===========================================
	private final Processor<P, R> processor;
	private final ExecutorService executorService;
	private final Map<P, Future<R>> futures = new ConcurrentHashMap<>();
	private final Object futuresLock = new byte[0];

	// ==============================Constructors======================================
	public AsynchronousTaskDevice(Builder<P, R> builder) {
		this.processor = builder.processor;
		this.executorService = Executors.newFixedThreadPool(builder.threads);
	}

	// ==============================Methods==========================================
	/**
	 * 提交一个任务请求
	 * @param request 任务请求
	 * @return 异步计算的结果
	 */
	public Future<R> submit(P request) {
		synchronized (futuresLock) {

			Future<R> future = futures.get(request);

			if (future != null) {
				return future;
			}

			Callable<R> callable = newCallable(request);

			future = executorService.submit(callable);

			futures.put(request, future);

			return future;
		}
	}

	/**
	 * 根据需要创建任务的对象
	 * @param request 任务请求
	 */
	private Callable<R> newCallable(final P request) {
		return new Callable<R>() {
			@Override
			public R call() throws Exception {
				try {
					return processor.execute(request);
				} finally {
					cleanFuture(request);
				}
			}
		};
	}

	/**
	 * 清理异步计算的结果缓存
	 * @param request 任务请求
	 */
	private void cleanFuture(P request) {
		synchronized (futuresLock) {
			futures.remove(request);
		}
	}

	/**
	 * 销毁方法，释放资源<br>
	 * (启动一次顺序关闭，执行以前提交的任务，但不接受新任务)
	 */
	public void destroy() {
		executorService.shutdown();
	}

	// ==============================InnerClass=======================================
	public static interface Processor<P, R> {
		R execute(P request);
	}

	// ==============================Builder==========================================
	public static class Builder<P, R> {
		private int threads = 1;
		private Processor<P, R> processor;

		public Builder<P, R> setThreads(int threads) {
			this.threads = threads;
			return this;
		}

		public Builder<P, R> setProcessor(Processor<P, R> processor) {
			this.processor = processor;
			return this;
		}

		public AsynchronousTaskDevice<P, R> build() {
			if (threads < 1) {
				threads = 1;
			}
			if (processor == null) {
				throw new IllegalArgumentException("processor == null");
			}
			return new AsynchronousTaskDevice<>(this);
		}
	}
}
