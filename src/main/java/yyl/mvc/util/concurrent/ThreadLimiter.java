package yyl.mvc.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程限制器<br>
 * 内部维持一个计数和最大许可数<br>
 * 调用 acquire() 时，会阻塞线程，然后获得许可。<br>
 * 调用 release() 添加一个许可，从而可能释放一个正在阻塞的获取者。<br>
 * @see java.util.concurrent.Semaphore
 */
public class ThreadLimiter {

	// ==============================Fields===========================================
	private final AtomicInteger limit = new AtomicInteger(1);
	private final AtomicInteger alive = new AtomicInteger(0);
	private final ReentrantLock reentrantLock = new ReentrantLock();
	private final Condition condition = reentrantLock.newCondition();

	// ==============================Constructors======================================
	public ThreadLimiter() {
	}

	public ThreadLimiter(int limit) {
		setLimit(limit);
	}

	// ==============================Methods==========================================
	public void acquire() throws InterruptedException {
		int limit = this.limit.get();
		if (alive.get() >= limit) {
			try {
				reentrantLock.lock();
				while (alive.get() >= limit) {
					condition.await();
				}
			} finally {
				reentrantLock.unlock();
			}
		}
		alive.incrementAndGet();
	}

	public void release() {
		try {
			reentrantLock.lock();
			alive.decrementAndGet();
			condition.signal();
		} finally {
			reentrantLock.unlock();
		}
	}

	public int getAlive() {
		return alive.get();
	}

	public void setLimit(int limit) {
		if (limit < 1) {
			throw new IllegalArgumentException("limit must be greater than 0 : " + limit);
		}
		this.limit.set(limit);
	}

	public int getLimit() {
		return limit.get();
	}
}
