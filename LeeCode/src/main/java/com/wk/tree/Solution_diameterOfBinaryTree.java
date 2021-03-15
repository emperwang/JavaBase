package com.wk.tree;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 12:52
 * @Description
 */
public class Solution_diameterOfBinaryTree {
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
 543. 二叉树的直径
给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
这条路径可能穿过也可能不穿过根结点。
示例 :
给定二叉树
          1
         / \
        2   3
       / \
      4   5
返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
  */
     /*
       简单理解,求解 tree的直径, 即是一个节点 左节点的深度 + 右节点的深度 + 1
      */
    int ans;
    public int diameterOfBinaryTree(TreeNode root) {
        ans = 1;
        depth(root);
        return ans-1;
    }

    public int depth(TreeNode node){
        if (node == null) return 0;
        int L = depth(node.left);   // 左节点深度
        int R = depth(node.right);  // 右节点深度
        ans = Math.max(ans, L+R+1); // 更新ans, 即此node的宽度,也就是 左节点深度+右节点深度 + 1
        return Math.max(L, R) + 1;  // 此是节点对应的深度
    }
}
