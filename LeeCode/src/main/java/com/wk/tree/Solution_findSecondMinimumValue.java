package com.wk.tree;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 12:53
 * @Description
 */
public class Solution_findSecondMinimumValue {
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
671. 二叉树中第二小的节点
给定一个非空特殊的二叉树，每个节点都是正数，并且每个节点的子节点数量只能为 2 或 0。如果一个节点有两个子节点的话，
那么该节点的值等于两个子节点中较小的一个。
更正式地说，root.val = min(root.left.val, root.right.val) 总成立。
给出这样的一个二叉树，你需要输出所有节点中的第二小的值。如果第二小的值不存在的话，输出 -1 。

示例 1：
    2
  2    5
      5  7
输入：root = [2,2,5,null,null,5,7]
输出：5
解释：最小的值是 2 ，第二小的值是 5 。

示例 2：
        2
      2   2
输入：root = [2,2,2]
输出：-1
解释：最小的值是 2, 但是不存在第二小的值。
 */
    /*
        1. 根节点是最小值

     */
    public int findSecondMinimumValue(TreeNode root) {
        if (root == null) return -1;
        return secVal(root,root.val);
    }

    public int secVal(TreeNode node, int val){
        if (node == null) return -1;
        if (node.val > val){
            return node.val;
        }
        int rig = secVal(node.right, val);
        int left = secVal(node.left, val);
        if (rig == -1) return left;
        if (left == -1) return rig;
        return Math.min(rig, left);
    }
}
