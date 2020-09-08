package yyl.mvc.plugin.javascript;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import yyl.mvc.common.logging.Logger;

/**
 * 模块工具类
 */
public class JSModuleHelper {

    public static final String MODULE_HELPER_CONSTANT_NAME = "_";
    private final Logger logger = Logger.getLogger(JSModuleHelper.class);
    private Map<String, Supplier<Object>> exports = new ConcurrentHashMap<>();

    /**
     * 定义模块输出类（应该在JAVA中调用，用于向脚本中输出JAVA类作为交互）
     * @param name 模块名称
     * @param export 模块输出
     */
    public void define(String name, Supplier<Object> export) {
        if (name == null) {
            logger.warn("The defined export module name is null");
        }
        if (export == null) {
            logger.warn("The defined export module is null");
        }
        if (name != null && export != null) {
            exports.put(name, export);
        }
    }

    /**
     * 根据名称获得模块对象（应当在脚本中去调用，获取注册的JAVA对象）
     * @param name 注册的模块名称
     * @return 模块
     */
    public Object require(String name) {
        Supplier<Object> export = exports.get(name);
        if (export == null) {
            logger.warn("The export module({}) not defined", name);
            return null;
        }
        return export.get();
    }
}
