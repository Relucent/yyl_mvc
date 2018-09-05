package yyl.mvc.plug.hibernate.id;

import yyl.mvc.util.identifier.IdUtil;

/**
 * 32位UUID生成器
 */
public class UUID32Generator implements IdGenerator<String> {

	/**
	 * 获得长度为32的UUID
	 * @return 长度为32的UUID
	 */
	@Override
	public String generateId() {
		return IdUtil.uuid32();
	}

}
