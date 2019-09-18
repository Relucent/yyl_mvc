package yyl.mvc.plugin.hibernate.id;

import java.io.Serializable;

/**
 * 唯一编码(ID)生成器
 * @param <I> 唯一编码的数据类型
 */
public interface IdGenerator<I extends Serializable> {

	/**
	 * 获得唯一编码(ID)
	 * @return 唯一编码
	 */
	I generateId();
}
