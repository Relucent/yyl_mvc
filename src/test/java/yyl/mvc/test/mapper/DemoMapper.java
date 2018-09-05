package yyl.mvc.test.mapper;

import java.util.List;

import yyl.mvc.core.plug.mybatis.annotation.MapperRepository;
import yyl.mvc.test.model.Demo;

@MapperRepository
public interface DemoMapper {

    Long insert(Demo model);

    void update(Demo model);

    Demo getById(Long id);

    Demo getByName(String name);

    List<Demo> find(Demo condition);

    // # List<Demo> find(Demo condition, RowBounds bounds);
}
