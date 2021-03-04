package com.wk.other;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: wk
 * @Date: 2021/1/8 18:15
 * @Description
 */
public class Solution_maxDepth {
    /*
    给定一个二叉树，找出其最大深度。
    二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
    说明: 叶子节点是指没有子节点的节点。

    示例：
    给定二叉树 [3,9,20,null,null,15,7]，
        3
       / \
      9  20
        /  \
       15   7
    返回它的最大深度 3 。
     */
 static class TreeNode {
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
    public int maxDepth(TreeNode root) {
        if (root == null){
            return 0;
        }
        int left = maxDepth(root.left)+1;
        int right  = maxDepth(root.right)+1;
        return left>=right?left:right;
    }
    /*
     按照层去遍历 树
     */
    public int maxDepthLevel(TreeNode root){
        if (root == null) return 0;
        final LinkedBlockingDeque<TreeNode> nodes = new LinkedBlockingDeque<>();
        nodes.push(root);
        int depth = 0;
        while (! nodes.isEmpty()){
            //  这里每次操作的是同一层中的所有节点
            int size = nodes.size();
            while (size > 0){
                final TreeNode top = nodes.pop();
                if (top.left != null) nodes.addLast(top.left);
                if (top.right != null) nodes.addLast(top.right);
                size--;
            }
            depth++;
        }
        return depth;
    }

     public static void main(String[] args) {
         final TreeNode root1 = new TreeNode(3);
         final TreeNode l1 = new TreeNode(9);
         final TreeNode r1 = new TreeNode(20);
         final TreeNode r2 = new TreeNode(15);
         final TreeNode r3 = new TreeNode(7);
         final TreeNode r4 = new TreeNode(7);
         root1.left= l1;
         root1.right = r1;
         r1.left = r2;
         r1.right = r3;
         r3.left = r4;
         final Solution_maxDepth solutionMaxDepth = new Solution_maxDepth();
         System.out.println(solutionMaxDepth.maxDepth(root1));
         System.out.println(solutionMaxDepth.maxDepthLevel(root1));
     }
}
