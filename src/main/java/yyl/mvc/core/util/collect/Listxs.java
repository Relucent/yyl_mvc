package yyl.mvc.core.util.collect;

import yyl.mvc.core.util.collect.impl.ArrayListx;

/**
 * 集合相关工具类，主要用于获得扩展的集合对象.
 * @author YYL
 */

public class Listxs {
	/**
	 * 获得一个List对象
	 * @return List对象
	 */
	public static Listx newListx() {
		return new ArrayListx();
	}
}
