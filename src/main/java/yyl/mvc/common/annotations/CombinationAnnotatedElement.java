package yyl.mvc.common.annotations;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 组合注解 对JDK的原生注解机制做一个增强。<br>
 * 使用递归获取指定元素上的注解以及注解的注解，以实现复合注解的获取。<br>
 */
@SuppressWarnings("serial")
public class CombinationAnnotatedElement implements AnnotatedElement, Serializable {


    /** 注解类型与注解对象对应表 */
    private final Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
    /** 直接注解类型与注解对象对应表 */
    private final Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap = new HashMap<>();

    /**
     * 构造函数
     * @param annotatedElement 被注解的元素，可以是Class、Method、Field、Constructor、Package、ReflectPermission等
     */
    public CombinationAnnotatedElement(AnnotatedElement annotatedElement) {
        Annotation[] annotations = annotatedElement.getAnnotations();
        AnnotationExtractor.extract(annotations, annotationMap);
        Annotation[] declaredAnnotations = annotatedElement.getDeclaredAnnotations();
        AnnotationExtractor.extractDeclared(declaredAnnotations, declaredAnnotationMap);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        Annotation annotation = annotationMap.get(annotationClass);
        return (annotation == null) ? null : (T) annotation;
    }

    @Override
    public Annotation[] getAnnotations() {
        final Collection<Annotation> annotations = this.annotationMap.values();
        return annotations.toArray(new Annotation[annotations.size()]);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        final Collection<Annotation> annotations = this.declaredAnnotationMap.values();
        return annotations.toArray(new Annotation[annotations.size()]);
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return annotationMap.containsKey(annotationClass);
    }

    /** 注解提取器 */
    protected static class AnnotationExtractor {

        /** 元注解 */
        private static final Set<Class<? extends Annotation>> META_ANNOTATIONS = new HashSet<>();
        static {
            META_ANNOTATIONS.add(Target.class);
            META_ANNOTATIONS.add(Retention.class);
            META_ANNOTATIONS.add(Inherited.class);
            META_ANNOTATIONS.add(Documented.class);
            META_ANNOTATIONS.add(Override.class);
            META_ANNOTATIONS.add(Deprecated.class);
            META_ANNOTATIONS.add(SuppressWarnings.class);
        }

        /**
         * 递归提取注解，并将结果保存到对应表中，直到全部都是元注解为止
         * @param annotations 注解列表
         * @param annotationMap 注解类型与注解对象对应表
         */
        public static void extract(Annotation[] annotations,
                Map<Class<? extends Annotation>, Annotation> annotationMap) {
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (!META_ANNOTATIONS.contains(annotationType)) {
                    annotationMap.put(annotationType, annotation);
                    extract(annotationType.getAnnotations(), annotationMap);
                }
            }
        }

        /**
         * 递归提取注解（不包括继承），并将结果保存到对应表中，直到全部都是元注解为止
         * @param annotations 注解列表
         * @param annotationMap 注解类型与注解对象对应表
         */
        public static void extractDeclared(Annotation[] annotations,
                Map<Class<? extends Annotation>, Annotation> annotationMap) {
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (!META_ANNOTATIONS.contains(annotationType)) {
                    annotationMap.put(annotationType, annotation);
                    extractDeclared(annotationType.getDeclaredAnnotations(), annotationMap);
                }
            }
        }
    }
}
