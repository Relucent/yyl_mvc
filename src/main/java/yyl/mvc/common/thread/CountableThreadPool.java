package yyl.mvc.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可数线程池<br>
 */
public class CountableThreadPool {

	// ==============================Fields===========================================
	private ExecutorService executorService;

	private ThreadLimiter limiter = new ThreadLimiter();

	// ==============================Constructors======================================
	public CountableThreadPool() {
		this.executorService = Executors.newCachedThreadPool();
	}

	// ==============================Methods==========================================
	public void execute(final Runnable runnable) throws InterruptedException {
		limiter.acquire();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {
					limiter.release();
				}
			}
		});
	}

	public int getThreadAlive() {
		return limiter.getAlive();
	}

	public int getMaxPoolSize() {
		return limiter.getLimit();
	}

	public void setMaxPoolSize(int nThreads) {
		limiter.setLimit(nThreads);
	}

	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
