package yyl.mvc.util.collect;

import java.util.Collections;
import java.util.Iterator;

import yyl.mvc.util.collect.impl.ArrayListx;

/**
 * 集合相关工具类，主要用于获得扩展的集合对象.
 * @author YYL
 */
public class Listxs {

    /**
     * 创建一个List对象
     * @return List对象
     */
    public static Listx newListx() {
        return new ArrayListx();
    }

    /**
     * 创建一个List对象
     * @param elements List中的元素
     * @return List对象
     */
    public static Listx newListx(Object... elements) {
        Listx list = newListx();
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * 创建一个List对象
     * @param elements 元素迭代器
     * @return List对象
     */
    public static Listx newArrayList(Iterator<?> elements) {
        Listx list = newListx();
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return list;
    }
}
