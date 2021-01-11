package com.wk;

import sun.reflect.generics.tree.Tree;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: wk
 * @Date: 2021/1/11 11:08
 * @Description
 */
public class Solution_sortedArrayToBST {
   static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
      TreeNode(int x, TreeNode left, TreeNode right){
          this.val = x;
          this.left = left;
          this.right = right;
      }
  }
  /*
  将一个按照升序排列的有序数组，转换为一棵高度平衡二叉搜索树。
    本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
    示例:
    给定有序数组: [-10,-3,0,5,9],
    一个可能的答案是：[0,-3,9,-10,null,5]，它可以表示下面这个高度平衡二叉搜索树：
          0
         / \
       -3   9
       /   /
     -10  5
   */

    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length <= 0){
            return null;
        }
        return buildTreeFromArray(nums,0,nums.length-1);
    }
    // 递归使用中间数 作为根节点
    public TreeNode buildTreeFromArray(int[] nums, int start, int end){
        if (start > end) return null;
        int mid = (start+end)/2;
        final TreeNode root = new TreeNode(nums[mid]);
        root.left = buildTreeFromArray(nums,start,mid-1);
        root.right = buildTreeFromArray(nums,mid+1, end);
        return root;
    }

    /*
  1. 找到数组的中间值
  2. 中间值的左边为左子树
  3. 中间值的右边为右子树
  4. 使用两个指针 分别指向小于中间值的右边的最大值, 一个指针指向左边的最大值,
      同时移动连个指针来构造二叉树
 //////////////  理论上应该可行
 // 有问题
 */
    public TreeNode build2(int[]  nums){
        if (nums == null || nums.length <= 0){
            return null;
        }
        int len = nums.length;
        int mid = len / 2;
        int right = len-1;
        int left = mid-1;
        TreeNode root = new TreeNode(nums[mid]);
        TreeNode leftNode = root;
        TreeNode rightNode = root;
        boolean first = true;
        while (right>mid && left>=0){
            leftNode.left = new TreeNode(nums[left--]);
            leftNode = leftNode.left;
            if (first){
                rightNode.right = new TreeNode(nums[right--]);
                rightNode = rightNode.right;
                first = false;
            }else {
                rightNode.left = new TreeNode(nums[right--]);
                rightNode = rightNode.left;
            }
        }
        while (right>mid){
            if (rightNode.left == null){
             rightNode.left = new TreeNode(nums[right--]);
            }else {
                rightNode.right = new TreeNode(nums[right--]);
            }
        }
        while (left >= 0){
            if (leftNode.left == null){
                leftNode.left = new TreeNode(nums[left--]);
            }else {
                leftNode.right = new TreeNode(nums[left--]);
            }
        }
        return root;
    }

    public void printTree(TreeNode root){
        final LinkedBlockingDeque<TreeNode> nodes = new LinkedBlockingDeque<>();
        nodes.push(root);
        while (! nodes.isEmpty()){
            int levelSize = nodes.size();
            while (levelSize > 0) {
                final TreeNode top = nodes.pop();
                System.out.print(top.val + " ");
                if (top.left != null) {
                    nodes.addLast(top.left);
                }
                if (top.right != null) {
                    nodes.addLast(top.right);
                }
                levelSize--;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        final TreeNode root = new TreeNode(0);
        final TreeNode l1 = new TreeNode(-3);
        final TreeNode l2 = new TreeNode(-10);
        final TreeNode r1 = new TreeNode(9);
        final TreeNode r2 = new TreeNode(5);
        root.left = l1;
        root.right = r1;
        l1.left = l2;
        r1.right = r2;
        final Solution_sortedArrayToBST solutionSortedArrayToBST = new Solution_sortedArrayToBST();
        //final TreeNode root1 = solutionSortedArrayToBST.sortedArrayToBST(new int[]{-10,-3,0,5,9});
        final TreeNode root1 = solutionSortedArrayToBST.sortedArrayToBST(new int[]{0,1,2,3,4,5});
        solutionSortedArrayToBST.printTree(root1);
        final TreeNode treeNode = solutionSortedArrayToBST.build2(new int[]{1,3});
        solutionSortedArrayToBST.printTree(treeNode);
//        System.out.println();
//        solutionSortedArrayToBST.printTree(root);
    }
}
