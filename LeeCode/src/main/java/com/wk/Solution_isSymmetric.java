package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/8 18:13
 * @Description
 */
public class Solution_isSymmetric {
    /*
    给定一个二叉树，检查它是否是镜像对称的。
    例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
        1
       / \
      2   2
     / \ / \
    3  4 4  3

    但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
        1
       / \
      2   2
       \   \
       3    3

    进阶：
    你可以运用递归和迭代两种方法解决这个问题吗？
     */
    class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
    public boolean isSymmetric(TreeNode root) {

        return false;
    }

    public static void main(String[] args) {

    }
}
