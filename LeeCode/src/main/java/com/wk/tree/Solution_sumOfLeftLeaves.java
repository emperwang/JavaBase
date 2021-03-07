package com.wk.tree;

/**
 * @author: Sparks
 * @Date: 2021/3/6 8:26
 * @Description
 */
public class Solution_sumOfLeftLeaves {
    /*
    404. 左叶子之和

计算给定二叉树的所有左叶子之和。
示例：
    3
   / \
  9  20
    /  \
   15   7
在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
     */
    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
    }
    public int sumOfLeftLeaves(TreeNode root) {
        if (root==null) return 0;
        return sumLeftLeaf(root);
    }
    // 这里是计算所有的左节点 之和
    // 要求是 计算所有 左叶子节点之和
    public int sumLeft(TreeNode node){
        int sum=0;
        if (node == null || node.left == null) return sum;
        sum += node.left.val;
        sum += sumLeft(node.left);
        sum += sumLeft(node.right);
        return sum;
    }

    public int sumLeftLeaf(TreeNode node){
        int sum=0;
        /*
        if (node == null || node.left == null) return sum;
        sum += isleaf(node.left)?node.left.val:sumLeftLeaf(node.left);
         */
        if (node.left != null) {
            // 左叶子节点, 所以每次都累加的是 左叶子节点的值
            sum += isleaf(node.left) ? node.left.val : sumLeftLeaf(node.left);
        }
        if (node.right != null && !isleaf(node.right)) {
            sum += sumLeftLeaf(node.right);
        }
        return sum;
    }

    public boolean isleaf(TreeNode node){
        if (node == null) return false;
        if (node.left == null && node.right == null) return true;
        return false;
    }

}
