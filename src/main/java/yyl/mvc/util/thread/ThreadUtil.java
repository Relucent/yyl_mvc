package yyl.mvc.util.thread;

public class ThreadUtil {
	/**
	 * 线程休眠
	 * @param millis 休眠时间
	 */
	public static void sleepQuietly(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
