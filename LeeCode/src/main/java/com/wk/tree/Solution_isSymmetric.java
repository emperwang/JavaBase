package com.wk.tree;

/**
 * @author: Sparks
 * @Date: 2021/3/6 8:09
 * @Description
 */
public class Solution_isSymmetric {

    class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
    /*
    剑指 Offer 28. 对称的二叉树
请实现一个函数，用来判断一棵二叉树是不是对称的。如果一棵二叉树和它的镜像一样，那么它是对称的。
例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
    1
   / \
  2   2
 / \ / \
3  4 4  3
中序: 3,2,4,1,4,2,3
先序: 1,2,3,4,2,4,3
但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
    1
   / \
  2   2
   \   \
   3    3
示例 1：
输入：root = [1,2,2,3,4,4,3]
输出：true
     */
    /*
       如果是镜像对称的话, 对于任意一个节点都有:
       1. node.left.val = node.right.val
       2. node.left.left.val = node.right.right.val
       3. node.left.right.val = node.right.left.val
     */
    public boolean isSymmetric(TreeNode root) {
      if (root == null) return true;
      return recurSymmetric(root.left, root.right);
    }

    public boolean recurSymmetric(TreeNode left, TreeNode right){
      if (left == null && right == null) return true;
      if (left ==null || right == null || left.val != right.val)return false;
      return recurSymmetric(left.left, right.right) && recurSymmetric(left.right,right.left);
    }
}
