package com.wk.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/3/4 16:32
 * @Description
 */
public class Solution_levelOrder {
class TreeNode {
 int val;
 TreeNode left;
 TreeNode right;
 TreeNode(int x) { val = x; }
}
/*
从上到下按层打印二叉树，同一层的节点按从左到右的顺序打印，每一层打印到一行。

例如:
给定二叉树: [3,9,20,null,null,15,7],
    3
   / \
  9  20
    /  \
   15   7

返回其层次遍历结果：
[
  [3],
  [9,20],
  [15,7]
]
 */
    /*
     使用要给队列 暂时缓存每一层的节点
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        LinkedList<TreeNode> queues = new LinkedList<>();
        if (root != null) queues.add(root);
        while (queues.size() > 0){
            List<Integer> tmp = new ArrayList<>();
            // 开始遍历时 就记录当前层的节点数 使用 i记录
            for (int i = queues.size(); i > 0; i--){
                TreeNode node = queues.poll();
                tmp.add(node.val);
                if (node.left != null) queues.add(node.left);
                if (node.right != null) queues.add(node.right);
            }
            result.add(tmp);
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
