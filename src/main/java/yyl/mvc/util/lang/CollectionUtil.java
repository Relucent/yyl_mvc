package yyl.mvc.util.lang;

import java.lang.reflect.Array;
import java.util.List;

/**
 * 集合工具类
 * @author YYL
 */
public class CollectionUtil {

    /**
     * 获得集合第一个元素
     * @param collection 集合对象
     * @return 集合第一个元素,如果数组为空返回NULL
     */
    public static <T> T getFirst(List<T> collection) {
        return (collection == null || collection.isEmpty()) ? null : collection.get(0);
    }

    /**
     * 获得数组第一个元素
     * @param array 数组对象    
     * @return 数组第一个元素,如果数组为空返回NULL
     */
    public static <T> T getFirst(T[] array) {
        return (array == null || array.length == 0) ? null : array[0];
    }

    /**
     * 转换集合对象为数组对象
     * @param collection 集合对象
     * @param componentType 集合元素类型
     * @return 数组对象
     */
    public static <T> T[] toArray(List<T> collection, Class<T> componentType) {
        int length = collection == null ? 0 : collection.size();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(componentType, length);
        if (length == 0) {
            return array;
        }
        return collection.toArray(array);
    }

}
