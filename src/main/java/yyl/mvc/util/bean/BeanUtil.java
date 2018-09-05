package yyl.mvc.util.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * BEAN操作工具类.提供一些Apache_Commons_BeanUtils反射方面缺失的封装。
 * @author YYL
 * @version 0.1 2012-10-13
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BeanUtil {

	/**
	 * 暴力设置当前类声明的private/protected属性。
	 * @param object 对象。
	 * @param propertyName 属性名。
	 * @param newValue 新值。
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static void setDeclaredProperty(Object object, String propertyName, Object newValue) throws IllegalAccessException, NoSuchFieldException {
		notNull(object, "object required");
		hasText(propertyName, "propertyName required");
		Field field = object.getClass().getDeclaredField(propertyName);
		setDeclaredProperty(object, field, newValue);
	}

	/**
	 * 暴力获取当前类声明的private/protected属性。
	 * @param object 对象。
	 * @param propertyName 属性名。
	 * @return 获取到的属性。
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	public static Object getDeclaredProperty(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
		notNull(object, "object required");
		hasText(propertyName, "propertyName required");
		Field field = object.getClass().getDeclaredField(propertyName);
		return getDeclaredProperty(object, field);
	}

	/**
	 * 暴力设置当前类声明的private/protected属性。
	 * @param object 对象。
	 * @param field 属性名。
	 * @param newValue 新值。
	 * @throws IllegalAccessException
	 */
	public static void setDeclaredProperty(Object object, Field field, Object newValue) throws IllegalAccessException {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		field.set(object, newValue);
		field.setAccessible(accessible);
	}

	/**
	 * 暴力获取当前类声明的private/protected属性。
	 * @param object 对象。
	 * @param field 属性。
	 * @return 获取到的属性。
	 * @throws IllegalAccessException
	 */
	public static Object getDeclaredProperty(Object object, Field field) throws IllegalAccessException {
		notNull(object, "object required");
		notNull(field, "field required");
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		Object result = field.get(object);
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 获得field的getter名称。
	 * @param type 类型。
	 * @param fieldName 属性名。
	 * @return 取值方法名。
	 */
	public static String getAccessorName(Class<?> type, String fieldName) {
		hasText(fieldName, "FieldName required");
		notNull(type, "Type required");
		if (type.getName().equals("boolean")) {
			return "is" + capitalize(fieldName);
		} else {
			return "get" + capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter名称。
	 * @param type 类型。
	 * @param fieldName 属性名。
	 * @return 取值方法名。
	 */
	public static Method getAccessor(Class type, String fieldName) {
		try {
			return type.getMethod(getAccessorName(type, fieldName));
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * 调用当前类声明的private/protected方法。
	 * @param object 对象。
	 * @param methodName 方法名。
	 * @param param 方法参数。
	 * @return 方法调用结果。
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object param)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokePrivateMethod(object, methodName, new Object[] { param });
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * @param object 对象实例
	 * @param propertyName 属性名
	 * @return 返回对应的Field
	 * @throws NoSuchFieldException 如果没有该Field时抛出
	 */
	public static Field forceGetDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		notNull(object, "object required");
		hasText(propertyName, "propertyName required");
		return forceGetDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * @param clazz 类型
	 * @param propertyName 属性名
	 * @return 返回对应的Field
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field forceGetDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
		notNull(clazz, "clazz required");
		hasText(propertyName, "propertyName required");
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException ex) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 * @param object 对象实例
	 * @param propertyName 属性名
	 * @return 强制获得属性值
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(final Object object, final String propertyName) throws NoSuchFieldException {
		notNull(object, "object required");
		hasText(propertyName, "propertyName required");
		final Field field = forceGetDeclaredField(object, propertyName);
		return AccessController.doPrivileged(new PrivilegedAction() {
			/** * run. */
			public Object run() {
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				Object result = null;
				try {
					result = field.get(object);
				} catch (IllegalAccessException e) {
					System.err.println("error wont happen");
				}
				field.setAccessible(accessible);
				return result;
			}
		});
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 * @param object 对象实例
	 * @param propertyName 属性名
	 * @param newValue 赋予的属性值
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static void forceSetProperty(final Object object, final String propertyName, final Object newValue) throws NoSuchFieldException {
		notNull(object, "object required");
		hasText(propertyName, "propertyName required");
		final Field field = forceGetDeclaredField(object, propertyName);
		AccessController.doPrivileged(new PrivilegedAction() {
			/** * run. */
			public Object run() {
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				try {
					field.set(object, newValue);
				} catch (IllegalAccessException e) {
					/* Ignore the error */
				}
				field.setAccessible(accessible);
				return null;
			}
		});
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 * @param object 对象实例
	 * @param methodName 方法名
	 * @param params 方法参数
	 * @return Object 方法调用返回的结果对象
	 * @throws NoSuchMethodException 如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(final Object object, final String methodName, final Object... params) throws NoSuchMethodException {
		notNull(object, "object required");
		hasText(methodName, "methodName required");
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}
		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException ex) {
				// 方法不在当前类定义,继续向上转型
			}
		}
		if (method == null) {
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);
		}
		final Method m = method;
		return AccessController.doPrivileged(new PrivilegedAction() {
			/** * run. */
			public Object run() {
				boolean accessible = m.isAccessible();
				m.setAccessible(true);
				Object result = null;
				try {
					result = m.invoke(object, params);
				} catch (Exception e) {
					handleReflectionException(e);
				}
				m.setAccessible(accessible);
				return result;
			}
		});
	}

	/**
	 * 按Field的类型取得Field列表.
	 * @param object 对象实例
	 * @param type 类型
	 * @return 属性对象列表
	 */
	public static List<Field> getFieldsByType(Object object, Class<?> type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}

		return list;
	}

	/**
	 * 按FieldName获得Field的类型.
	 * @param type 类型
	 * @param name 属性名
	 * @return 属性的类型
	 * @throws NoSuchFieldException 指定属性不存在时，抛出异常
	 */
	public static Class<?> getPropertyType(Class<?> type, String name) throws NoSuchFieldException {
		return forceGetDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称.
	 * @param type 类型
	 * @param fieldName 属性名
	 * @return getter方法名
	 * @throws NoSuchFieldException field不存在时抛出异常
	 */
	public static String getGetterName(Class<?> type, String fieldName) throws NoSuchFieldException {
		notNull(type, "Type required");
		hasText(fieldName, "FieldName required");

		Class<?> fieldType = forceGetDeclaredField(type, fieldName).getType();

		if ((fieldType == boolean.class) || (fieldType == Boolean.class)) {
			return "is" + capitalize(fieldName);
		} else {
			return "get" + capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 * @param type 类型
	 * @param fieldName 属性名
	 * @return getter方法对象
	 */
	public static Method getGetterMethod(Class<?> type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException ex) {
			/* Ignore the error */
		} catch (NoSuchFieldException ex) {
			/* Ignore the error */
		}
		return null;
	}

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private static void hasText(String text, String message) {
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	private static String capitalize(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return str;
		char firstChar = str.charAt(0);
		if (Character.isTitleCase(firstChar)) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
	}

	private static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof RuntimeException) {
			throw ((RuntimeException) ex);
		}
		throw new UndeclaredThrowableException(ex);
	}

	// 工具类私有构造
	private BeanUtil() {
	}
}