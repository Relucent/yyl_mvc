package yyl.mvc.core.util.cmd;

/**
 * 命令执行结果
 */
public class CommandResult {

	// ==============================Fields===========================================
	public final String stdout;
	public final String stderr;
	public final Integer code;

	// ==============================Constructors=======================================
	public CommandResult(String stdout, String stderr, Integer code) {
		this.stdout = stdout;
		this.stderr = stderr;
		this.code = code;
	}

	// ==============================Methods==========================================
	public String getStdout() {
		return stdout;
	}

	public String getStderr() {
		return stderr;
	}

	public Integer getCode() {
		return code;
	}
}
