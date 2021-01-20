package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/20 13:04
 * @Description
 */
public class Solution_invertTree {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    /*
    翻转一棵二叉树。
    示例：
    输入：
         4
       /   \
      2     7
     / \   / \
    1   3 6   9

    输出：
         4
       /   \
      7     2
     / \   / \
    9   6 3   1
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

}
