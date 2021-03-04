package com.wk.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/3/4 16:13
 * @Description
 */
public class Solution_preorder {

    /*
    给定一个 N 叉树，返回其节点值的 前序遍历 。
    N 叉树 在输入中按层序遍历进行序列化表示，每组子节点由空值 null 分隔（请参见示例）。

进阶：
递归法很简单，你可以使用迭代法完成此题吗?
         1
       / | \
      3  2  4
    /  \
   5    6
   输入: root=[1,null,3,2,4,null,5,6]
   输出:[1,3,5,6,2,4]
     */
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    /*
        利用一个栈 来帮助实现
     */
    public List<Integer> preorder(Node root) {
        LinkedList<Node> stack = new LinkedList<>();
        ArrayList<Integer> output = new ArrayList<>();
        if (root == null) return output;
        stack.add(root);
        while (!stack.isEmpty()){
            Node node = stack.pollLast();
            output.add(node.val);
            Collections.reverse(node.children);
            for (Node child : node.children) {
                stack.add(child);
            }
        }
        return output;
    }

    public static void main(String[] args) {

    }
}
