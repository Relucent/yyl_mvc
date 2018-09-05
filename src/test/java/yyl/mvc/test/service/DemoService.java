package yyl.mvc.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yyl.mvc.plug.mybatis.MybatisHelper;
import yyl.mvc.test.mapper.DemoMapper;
import yyl.mvc.test.model.Demo;
import yyl.mvc.util.page.Page;
import yyl.mvc.util.page.Pagination;

@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public Page<Demo> getByPage(Pagination pagination, Demo condition) {
        return MybatisHelper.selectPage(pagination, () -> demoMapper.find(condition));
    }
}
