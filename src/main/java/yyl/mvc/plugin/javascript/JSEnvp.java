package yyl.mvc.plugin.javascript;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 脚本环境变量
 */
public class JSEnvp {

    /** 符合变量名称的正则 */
    private static final Pattern MODULE_NAME_PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*");
    /** 模块工具类变量名 */
    private String moduleHelperConstName = JSModuleHelper.MODULE_HELPER_CONSTANT_NAME;
    /** 模块工具类 */
    private JSModuleHelper moduleHelper = new JSModuleHelper();

    /**
     * 定义模块（应该在JAVA中调用，用于向脚本中输出JAVA类作为交互）
     * @param name 模块名称
     * @param export 模块输出
     */
    public void define(String name, Supplier<Object> export) {
        moduleHelper.define(name, export);
    }

    /**
     * 设置模块工具类
     * @param moduleHelper 模块工具类
     */
    public void setModuleHelper(JSModuleHelper moduleHelper) {
        this.moduleHelper = moduleHelper;
    }

    /**
     * 修改模块工具类的引用名称（JavaScript中模块管理工具的常量名）
     * @param moduleHelperConstName 模块工具类的引用名称
     */
    public void setModuleHelperConstName(String moduleHelperConstName) {
        if (moduleHelperConstName != null && !MODULE_NAME_PATTERN.matcher(moduleHelperConstName).matches()) {
            throw new IllegalArgumentException("Const Naming does not conform to the specification:" + moduleHelperConstName);
        }
        this.moduleHelperConstName = moduleHelperConstName;
    }

    /**
     * 获得模块工具类
     * @return 模块工具类
     */
    protected JSModuleHelper getModuleHelper() {
        return moduleHelper;
    }

    /**
     * 访问模块工具类
     * @param action 为模块工具类执行的操作
     */
    protected void acceptModuleHellper(BiConsumer<String, JSModuleHelper> action) {
        if (moduleHelperConstName != null && moduleHelper != null) {
            action.accept(moduleHelperConstName, moduleHelper);
        }
    }
}
