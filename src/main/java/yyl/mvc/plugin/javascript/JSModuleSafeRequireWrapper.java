package yyl.mvc.plugin.javascript;

/**
 * 定义模块输出包装类（用于输出JAVA对象到脚本中）
 */
public class JSModuleSafeRequireWrapper {

    private final JSModuleHelper helper;

    public JSModuleSafeRequireWrapper(JSModuleHelper helper) {
        this.helper = helper;
    }

    public Object require(String name) {
        return helper.require(name);
    }
}
