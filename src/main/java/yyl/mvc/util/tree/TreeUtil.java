package yyl.mvc.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Tree类型工具类
 * @author YYL
 */
public class TreeUtil {

    /** 默认的路径分隔符 */
    public static final String ID_PATH_SEPARATOR = "/";

    /** 默认排序比较器(代表不排序) */
    private static final Comparator<?> NONE_COMPARATOR = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return 0;
        }
    };


    /**
     * 构建树模型
     * @param parentId 上级节点ID
     * @param data 数据
     * @param adapter 节点适配器
     * @param filter 节点适过滤器
     * @param access 节点ID获取器
     * @return 树模型
     */
    @SuppressWarnings("unchecked")
    public static <T, N extends Node<N>, I> List<N> buildTree(I parentId, List<T> data, NodeAdapter<T, N> adapter,
            NodeFilter<T> filter, IdAccess<T, I> access) {
        return buildTree(parentId, data, adapter, filter, access, (Comparator<N>) NONE_COMPARATOR, 0);
    }

    /**
     * 构建树模型
     * @param parentId 上级节点ID
     * @param data 数据
     * @param adapter 节点适配器
     * @param filter 节点适过滤器
     * @param access 节点ID获取器
     * @param comparator 排序比较器
     * @return 树模型
     */
    public static <T, N extends Node<N>, I> List<N> buildTree(I parentId, List<T> data, NodeAdapter<T, N> adapter,
            NodeFilter<T> filter, IdAccess<T, I> access, Comparator<N> comparator) {
        return buildTree(parentId, data, adapter, filter, access, comparator, 0);
    }

    /**
     * 构建树模型
     * @param parentId 上级节点ID
     * @param data 数据
     * @param adapter 节点适配器
     * @param filter 节点适过滤器
     * @param access 节点ID获取器
     * @param comparator 排序比较器
     * @param depth 节点层级
     * @return 树模型
     */
    private static <T, N extends Node<N>, I> List<N> buildTree(I parentId, List<T> data, NodeAdapter<T, N> adapter,
            NodeFilter<T> filter, IdAccess<T, I> access, Comparator<N> comparator, int depth) {
        List<N> nodes = new ArrayList<>();
        for (T model : data) {
            if (Objects.equals(parentId, access.getParentId(model))) {
                List<N> children = buildTree(access.getId(model), data, adapter, filter, access, comparator, depth + 1);
                if (filter.accept(model, depth, children.isEmpty())) {
                    N node = adapter.adapte(model);
                    node.setChildren(children);
                    nodes.add(node);
                }
            }
        }
        if (comparator != NONE_COMPARATOR) {
            Collections.sort(nodes, comparator);
        }
        return nodes;
    }

    /**
     * 递归设置 ID_PATH
     * @param data 数据
     * @param access ID获取器
     * @param parentId 上级ID
     * @param parentIdPath 上级ID路径
     */
    public static <T extends IdPathable, I> void recursiveSetIdPath(Collection<T> data, IdAccess<T, I> access,
            I parentId, String parentIdPath) {
        for (T model : data) {
            if (Objects.equals(parentId, access.getParentId(model))) {
                I id = access.getId(model);
                String idPath = parentIdPath + id + ID_PATH_SEPARATOR;
                model.setIdPath(idPath);
                recursiveSetIdPath(data, access, id, idPath);
            }
        }
    }

    /** ID获取器 */
    public static interface IdAccess<T, I> {
        /** 获得ID */
        I getId(T model);

        /** 获得父ID */
        I getParentId(T model);
    }

    /** 节点过滤器 */
    @FunctionalInterface
    public static interface NodeFilter<T> {
        /**
         * 判断树中是否应含有该节点
         * @param entity 节点代表的的对象
         * @param depth 节点在树中所在的层次
         * @return 如果树中不包含该节点，则返回true，否则返回false。
         */
        public boolean accept(T model, int depth, boolean leaf);
    }

    /** 节点适配器 */
    @FunctionalInterface
    public static interface NodeAdapter<T, N extends Node<N>> {
        /**
         * 将数据模型转换为树节点模型
         * @param model 数据模型
         * @return 树节点模型
         */
        N adapte(T model);
    }
}
