package com.zjh.tools;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建树的工具类
 * auth:zjh
 */
public class TreeUtil {
    /**
     * @param allNode        所有节点
     * @param relatePro1Name 关联属性名称1---nid
     * @param relatePro2Name 关联属性名称2---pid
     * @param childProName   孩子节点属性名称---children
     * @param rootValue      跟节点关联pid的值
     * @param <T>            泛型
     * @return
     */
    public static <T> List<T> buildTree(List<T> allNode
            , String relatePro1Name, String relatePro2Name, String childProName, Object rootValue) {
        ArrayList<T> res = CollUtil.newArrayList();
        for (T t : allNode) {
            Object property = BeanUtil.getProperty(t, relatePro2Name);
            if (rootValue.equals(property)) {
                res.add(t);
            }
        }
        findChildren(res, allNode, relatePro1Name, relatePro2Name, childProName);
        return res;
    }


    /**
     * 基于完整的树(必须是完整的树)根据关键词过滤构建新的树
     *
     * @param tree          完整的树
     * @param childProname  孩子节点属性名称
     * @param filterProname 过滤属性名称
     * @param keyword       关键词
     * @param <T>
     * @return
     */
    public static <T> List<T> buildTreeByKeyWord(List<T> tree
            , String childProname, String filterProname, String keyword) {
        ArrayList<T> res = CollUtil.newArrayList(tree);
        deleteChildren(res, childProname, filterProname, keyword);
        return res;
    }

    /**
     * 从根节点开始遍历，删除不包含关键字的节点，删除条件为自身不包含节点并且没有子节点
     * @param res
     * @param childProname  子节点属性名
     * @param filterProname 过滤节点属性名
     * @param keyword   关键字
     * @param <T>   树的类型
     */
    private static <T> void deleteChildren(ArrayList<T> res
            , String childProname, String filterProname, String keyword) {
        for (T node : res) {
            ArrayList<T> childen = (ArrayList<T>) ReflectUtil.getFieldValue(node, childProname);
            deleteChildren(childen, childProname, filterProname, keyword);
            for (int i = 0; i < childen.size(); i++) {
                T t = childen.get(i);
                ArrayList<T> c = (ArrayList<T>) ReflectUtil.getFieldValue(t, childProname);
                String fieldValue = ReflectUtil.getFieldValue(t, filterProname).toString();
                if (!fieldValue.contains(keyword) && CollUtil.isEmpty(c)) {
                    childen.remove(i);
                    i--;
                }
            }
            ReflectUtil.setFieldValue(node, childProname, childen);
        }
    }

    /**
     * 递归查找所有孩子节点
     * @param root  根节点
     * @param allNode   所有节点
     * @param relatePro1Name    关联字段名1
     * @param relatePro2Name    关联字段名2
     * @param childProName  孩子节点
     * @param <T>
     */
    private static <T> void findChildren(List<T> root, List<T> allNode, String relatePro1Name,
                                         String relatePro2Name, String childProName) {
        for (T t : root) {
            ArrayList<T> children = new ArrayList<T>();
            ReflectUtil.setFieldValue(t, childProName, children);
            Object rootval = BeanUtil.getProperty(t, relatePro1Name);
            for (T node : allNode) {
                Object nodeParentval = BeanUtil.getProperty(node, relatePro2Name);
                if (nodeParentval.equals(rootval)) {
                    children.add(node);
                }
            }
            findChildren(children, allNode, relatePro1Name, relatePro2Name, childProName);
        }
    }
}
