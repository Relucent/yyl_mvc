package yyl.mvc.util.tree;

import java.util.List;
/**
 * 树结构节点模型
 * @author YYL
 */
public interface Node<N extends Node<N>> {

    /** 获得子节点 */
    List<N> getChildren();

    /** 设置子节点 */
    void setChildren(List<N> children);
}
