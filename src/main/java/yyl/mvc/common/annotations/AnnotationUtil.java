package yyl.mvc.common.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;


/**
 * 注解工具类
 */
public class AnnotationUtil {

    /**
     * 获取元素的注解
     * @param annotatedElement 注解元素对象 {@link AnnotatedElement}
     * @return 注解对象列表
     */
    public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
        return annotatedElement == null ? null : annotatedElement.getAnnotations();
    }

    /**
     * 获取注解类的保留策略，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS
     * @param annotationType 注解类
     * @return 保留策略枚举
     */
    public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        return retention == null ? RetentionPolicy.CLASS : retention.value();
    }

    /**
     * 是否会保存到 JavaDoc 文档中
     * @param annotationType 注解类
     * @return 是否会保存到 JavaDoc文档中
     */
    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    /**
     * 是否可以被继承，默认为 false
     * @param annotationType 注解类
     * @return 是否可以被继承
     */
    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }

    /**
     * 获取注解类可以用来修饰哪些程序元素
     * @param annotationType 注解类
     * @return 注解修饰的程序元素数组
     */
    public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
        final Target target = annotationType.getAnnotation(Target.class);
        if (target == null) {
            return new ElementType[] {//
                    ElementType.TYPE, //
                    ElementType.FIELD, //
                    ElementType.METHOD, //
                    ElementType.PARAMETER, //
                    ElementType.CONSTRUCTOR, //
                    ElementType.LOCAL_VARIABLE, //
                    ElementType.ANNOTATION_TYPE, //
                    ElementType.PACKAGE//
            };
        }
        return target.value();
    }

    /**
     * 根据指定的注解创建一个注解元素对象{@link AnnotatedElement}
     * @param annotations 注解列表
     * @return 包含指定注解的注解元素对象
     */
    public static AnnotatedElement forAnnotations(final Annotation... annotations) {
        return new AnnotatedElement() {
            @Override
            @SuppressWarnings("unchecked")
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == annotationClass) {
                        return (T) annotation;
                    }
                }
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return annotations;
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return annotations;
            }
        };
    }
}
