package yyl.mvc.test.servlet3;

import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.http.HttpServlet;

/**
 * 使用ServletContainerInitializer实现，可以在容器启动时自动回调其onStartup方法，插入一些功能<br>
 * 使用时需要在对应的jar包的 META-INF/services 目录创建一个名为 javax.servlet.ServletContainerInitializer的文件， 该文件指定具体的ServletContainerInitializer实现类<br>
 */
@HandlesTypes({ HttpServlet.class, Filter.class })
public class HelloServletContainerInitializer implements ServletContainerInitializer {
	@Override
	public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
		System.out.println("Hello ServletContainerInitializer");
		for (Class<?> c : set) {
			System.out.println(c.getName());
		}
	}
}