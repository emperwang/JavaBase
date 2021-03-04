package com.wk.tree;

/**
 * @author: ekiawna
 * @Date: 2021/3/4 15:41
 * @Description
 */
public class Solution_isBalanced {
    /*
    给定一个二叉树，判断它是否是高度平衡的二叉树。

    本题中，一棵高度平衡二叉树定义为：
    一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1 。
    如:
        3
       / \
      9   20
         /  \
        15   7
   输入: root = [3,9,20,null,null, 15,7]
   输出: true

        1
       / \
      2   2
     / \
    3   3
   / \
  4   4
  输入: root = [1,2,2,3,3,4,4]
  输出: false

  输入: root = []
  输出: true
     */
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}

        TreeNode(int x) {
            this.val = x;
        }

        TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 自顶向下, 判断每一个子树是否是平衡的, 如果子树是平衡的, 那么整个树就是平衡的
     * @param root
     * @return
     */
    public boolean isBalanced(TreeNode root) {
        if (root == null) return true;
        return isBalanced(root.left) && isBalanced(root.right) && (Math.max(height(root.left), height(root.right)))<=1;
    }
    public int height(TreeNode root){
        if (root == null){
            return 0;
        }else{
            return Math.max(height(root.left), height(root.right))+1;
        }
    }

    /*
    自底向上, 判断每个子树是否是平衡的, 如果子树平衡, 那么整棵树就平衡
     */
    public boolean isBalanced2(TreeNode root) {
        return height2(root) >= 0;
    }
    public int height2(TreeNode root){
        if (root == null) return 0;
        int left = height2(root.left);
        int right = height2(root.right);
        if (left == -1 || right==-1 || (Math.abs(left-right))>1){
            return -1;
        }else{
            return Math.max(left, right)+1;
        }
    }

    public static void main(String[] args) {

    }
}
