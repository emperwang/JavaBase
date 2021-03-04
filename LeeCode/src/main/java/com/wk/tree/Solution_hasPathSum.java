package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: Sparks
 * @Date: 2021/3/4 20:27
 * @Description
 */
public class Solution_hasPathSum {
    /*
    路径总和
给你二叉树的根节点 root 和一个表示目标和的整数 targetSum ，
判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
叶子节点 是指没有子节点的节点。
示例 1：
      5
     / \
    4   8
   /   / \
  11  13   4
  / \      \
 7   2      1
输入：root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
输出：true

示例2
     1
    / \
   2   3
   输入: root=[1,2,3]  targetSum = 5;
   输出: false
     */
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
        使用队列进行广度优先搜索, 分别记录每一个叶子节点的路径和, 如果某个叶子节点的路径sum和 target相同,则结束; 如果没有相同的,
        那么就是不存在
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        Queue<TreeNode> nodes = new LinkedList<>();
        Queue<Integer> values = new LinkedList<>();
        if (root != null) {
            nodes.add(root);
            values.add(root.val);
        }
        while (!nodes.isEmpty()) {
            TreeNode node = nodes.poll();
            Integer value = values.poll();
            if (node.left == null && node.right == null) {
                if (value == targetSum) return true;
                continue;
            }
            if (node.left != null) {
                nodes.add(node.left);
                values.add(value + node.left.val);
            }
            if (node.right != null){
                nodes.add(node.right);
                values.add(value + node.right.val);
            }
        }
        return false;
    }
    // 递归实现
    // 时间复杂度: O(n)
    public boolean hasPathSum2(TreeNode root, int targetSum) {
        if (root == null) return false;
        if (root.left == null && root.right == null){
            return root.val == targetSum;
        }
        return hasPathSum2(root.right, targetSum - root.val) ||
                hasPathSum2(root.right, targetSum - root.val);
    }
    public static void main(String[] args) {

    }
}
