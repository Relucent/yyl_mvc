package yyl.mvc.util.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Throwables;

/**
 * 命令执行器
 */
public class CommandExecutor {
	// ==============================Fields===========================================
	protected static final String ENCODING = "utf-8";

	// ==============================Methods==========================================
	/**
	 * 执行命令
	 * @param commands 命令
	 * @return 命令执行结果
	 */
	public CommandResult exec(String[] commands) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(commands);
			CountDownLatch latch = new CountDownLatch(2);
			StringWriter input = new StringWriter();
			StringWriter error = new StringWriter();
			new Thread(new StreamWorker(latch, process.getInputStream(), input)).start();
			new Thread(new StreamWorker(latch, process.getErrorStream(), error)).start();
			latch.await();
			int exitValue = process.waitFor();
			return new CommandResult(input.toString(), error.toString(), exitValue);
		} catch (Exception e) {
			String input = "!";
			String error = Throwables.getStackTraceAsString(e);
			return new CommandResult(input, error, -1);
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
			}
		}
	}

	// ==============================InternalClass========================================
	/** 用于读取流信息，防止线程阻断 */
	class StreamWorker implements Runnable {

		private final CountDownLatch latch;
		private final InputStream input;
		private final StringWriter writer;
		protected static final String ENCODING = "utf-8";

		public StreamWorker(final CountDownLatch latch, InputStream input, StringWriter writer) {
			this.latch = latch;
			this.input = input;
			this.writer = writer;
		}

		@Override
		public void run() {
			try {
				IOUtils.copy(input, writer, ENCODING);
			} catch (IOException e) {
				e.printStackTrace();
				writer.append("!!!");
			} finally {
				IOUtils.closeQuietly(input);
				latch.countDown();
			}
		}
	}
}
