package yyl.mvc.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yyl.mvc.core.plug.mybatis.MybatisHelper;
import yyl.mvc.core.util.page.Page;
import yyl.mvc.core.util.page.Pagination;
import yyl.mvc.test.mapper.DemoMapper;
import yyl.mvc.test.model.Demo;

@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public Page<Demo> getByPage(Pagination pagination, Demo condition) {
        return MybatisHelper.selectPage(pagination, () -> demoMapper.find(condition));
    }
}
