package com.wk.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 12:50
 * @Description
 */
public class Solution_isCousins {
    class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
/*
993. 二叉树的堂兄弟节点
在二叉树中，根节点位于深度 0 处，每个深度为 k 的节点的子节点位于深度 k+1 处。
如果二叉树的两个节点深度相同，但 父节点不同 ，则它们是一对堂兄弟节点。
我们给出了具有唯一值的二叉树的根节点 root ，以及树中两个不同节点的值 x 和 y 。
只有与值 x 和 y 对应的节点是堂兄弟节点时，才返回 true 。否则，返回 false。

示例 1：
        1
      2       3
    4  nil nil nil
输入：root = [1,2,3,4], x = 4, y = 3
输出：false

示例 2：
        1
     2       3
  nil   4  nil   5
输入：root = [1,2,3,null,4,null,5], x = 5, y = 4
输出：true
 */
    /*
     使用容器记录每个节点对应的深度 以及 其父节点
     */
    Map<Integer, Integer> depth;
    Map<Integer, TreeNode> parents;
    public boolean isCousins(TreeNode root, int x, int y) {
        depth = new HashMap<>();
        parents = new HashMap<>();
        dfs(root, null);
        return (depth.get(x) == depth.get(y) && parents.get(x) != parents.get(y));
    }

    public void dfs(TreeNode node, TreeNode parent){
        if (node != null){
            depth.put(node.val, parent!=null?1+depth.get(parent.val):0);
            parents.put(node.val, parent);
            dfs(node.left, node);
            dfs(node.right, node);
        }
    }
}
