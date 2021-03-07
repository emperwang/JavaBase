package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: Sparks
 * @Date: 2021/3/6 6:44
 * @Description
 */
public class Solution_maxDepth {
/*
剑指 Offer 55 - I. 二叉树的深度

输入一棵二叉树的根节点，求该树的深度。从根节点到叶节点依次经过的节点（含根、叶节点）形成树的一条路径，
最长路径的长度为树的深度。

例如：
给定二叉树 [3,9,20,null,null,15,7]，
    3
   / \
  9  20
    /  \
   15   7
返回它的最大深度 3 。
 */
    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }
    // 递归实现
    // 从下到上, 计算 左右子树的高度
    public int maxDepth(TreeNode root) {
        return height(root);
    }
    public int height(TreeNode node){
        if (node == null) return 0;
        int left = height(node.left);
        int right = height(node.right);
        return Math.max(left,right) + 1;
    }

    // 层序遍历, 计算层数
    public int maxDepth2(TreeNode node){

        return bfs(node);
    }
    // 广度优先搜索: breadth - first - search
    public int bfs(TreeNode node){
        if (node == null) return  0;
        Queue<TreeNode> stack = new LinkedList<>();
        stack.add(node);
        int depth = 0;
        while (!stack.isEmpty()){
            TreeNode tmp = stack.poll();
            for (int i=stack.size(); i >0; i++){
                if (tmp.left!= null) stack.add(tmp.left);
                if (tmp.right != null) stack.add(tmp.right);
            }
            depth++;
        }
        return depth;
    }

}
