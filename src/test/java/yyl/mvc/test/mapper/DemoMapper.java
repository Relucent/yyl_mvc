package yyl.mvc.test.mapper;

import yyl.mvc.core.plug.mybatis.annotation.Mapper;
import yyl.mvc.core.util.page.Page;
import yyl.mvc.core.util.page.Pagination;
import yyl.mvc.test.model.Demo;

@Mapper
public interface DemoMapper {

	Long insert(Demo model);

	void update(Demo model);

	Demo getById(Long id);

	Demo getByName(String name);

	Page<Demo> getByPage(Pagination pagination);

	//# List<Demo> getByPage01(Demo conditions, RowBounds bounds);

	//# PageList<Demo> getByPage02(Demo conditions, PageBounds bounds);
}
