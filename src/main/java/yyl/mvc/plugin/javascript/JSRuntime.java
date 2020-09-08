package yyl.mvc.plugin.javascript;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JavaScript 运行时（环境），通过这个运行时可以实现Java语言与JavaScript之间的相互调用。
 */
public class JSRuntime {

    private static final JSRuntime GLOBAL_RUNTIME = new JSRuntime(new JSEnvp());

    private final ScriptableObject root;

    /**
     * 构造函数
     * @param envp 环境属性
     */
    private JSRuntime(JSEnvp envp) {
        root = invokeContext(Context::initSafeStandardObjects);
        envp.acceptModuleHellper((name, helper) -> ScriptableObject.putProperty(root, name, new JSModuleSafeRequireWrapper(helper)));
    }

    /**
     * 获得全局默认的脚本环境
     * @return 默认的脚本环境
     */
    public static JSRuntime getRuntime() {
        return GLOBAL_RUNTIME;
    }

    /**
     * 获得脚本运行时
     * @param 脚本环境属性
     * @return 脚本运行时
     */
    public static JSRuntime getRuntime(JSEnvp envp) {
        return new JSRuntime(envp);
    }

    /**
     * 执行脚本
     * @param command 脚本命令
     * @return 脚本返回值
     */
    public Object exec(String command) {
        return invokeContext(context -> context.evaluateString(root, command, null, 0, null));
    }

    /**
     * 执行脚本
     * @param reader 脚本的文本流
     * @return 脚本返回值
     * @throws IOException IO异常
     */
    public Object exec(Reader reader) throws IOException {
        AtomicReference<IOException> throwableReference = new AtomicReference<>();
        Object result = invokeContext(context -> {
            try {
                return context.evaluateReader(root, reader, null, 0, null);
            } catch (IOException e) {
                throwableReference.set(e);
                return null;
            }
        });
        isPresentThrow(throwableReference.get());
        return result;
    }

    /**
     * 执行脚本(带参数)
     * @param command 脚本命令
     * @param parameters 输入的参数
     * @return 脚本返回值
     */
    public Object exec(String command, Map<String, Object> parameters) {
        return invokeContext(context -> {
            Scriptable scope = context.initStandardObjects(root);
            Optional.ofNullable(parameters).ifPresent(p -> p.forEach((name, value) -> {
                ScriptableObject.putProperty(scope, name, value);
            }));
            return context.evaluateString(scope, command, null, 0, null);
        });
    }

    /**
     * 调用执行的脚本的运行时上下文
     * @param <T> 调用返回值类型
     * @param action 调用内容
     * @return 调用返回值
     */
    private <T> T invokeContext(Function<Context, T> action) {
        Context context = Context.enter();
        try {
            context.setOptimizationLevel(-1);
            context.setLanguageVersion(Context.VERSION_ES6);
            return action.apply(context);
        } finally {
            Context.exit();
        }
    }

    /**
     * 如果异常存在则抛出异常
     * @param <X> 异常类型
     * @param throwable 异常类型
     * @throws X 抛出的异常
     */
    private <X extends Throwable> void isPresentThrow(X throwable) throws X {
        if (throwable != null) {
            throw throwable;
        }
    }
}
