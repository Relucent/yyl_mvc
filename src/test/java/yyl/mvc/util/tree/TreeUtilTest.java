package yyl.mvc.util.tree;

import java.util.ArrayList;
import java.util.List;

import yyl.mvc.common.json.JsonUtil;
import yyl.mvc.common.tree.TreeUtil;

public class TreeUtilTest {


    public static void main(String[] args) {
        List<T> data = new ArrayList<T>();
        data.add(new T("100", null));
        data.add(new T("110", "100"));
        data.add(new T("111", "110"));
        data.add(new T("112", "110"));
        data.add(new T("120", "100"));
        data.add(new T("121", "120"));
        List<N> nodes = TreeUtil.buildTree(//
                null, // 父节点ID
                data, // data
                o -> new N(o.id), // 节点适配器
                (o, d, l) -> true, // 节点过滤器
                r -> r.id, // 节点ID访问器
                r -> r.parentId, // 节点父ID访问器
                (n, c) -> n.children = c, // 子节点设置器
                (a, b) -> a.id.compareTo(b.id)// 排序比较器
        );
        System.out.println(JsonUtil.encode(nodes));
    }


    static class T {
        public String id;
        public String parentId;

        public T(String id, String parentId) {
            this.id = id;
            this.parentId = parentId;
        }
    }

    static class N {
        public String id;
        public List<N> children;

        public N(String id) {
            this.id = id;
        }
    }
}
