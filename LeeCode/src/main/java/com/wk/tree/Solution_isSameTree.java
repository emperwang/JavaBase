package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: Sparks
 * @Date: 2021/3/7 9:58
 * @Description
 */
public class Solution_isSameTree {

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
 100. 相同的树
给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
示例 1：
        1       1
       2 3     2  3
输入：p = [1,2,3], q = [1,2,3]
输出：true

    1      1
   2         2
输入：p = [1,2], q = [1,null,2]
输出：false
  */
    // 递归实现
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p != null && q == null) {
            return false;
        }
        if (p == null && q != null){
            return false;
        }

        if (p.val != q.val){
            return false;
        }
        return isSameTree(p.left,q.left) && isSameTree(p.right, q.right);
    }
    /*
        广度优先遍历
        1. 两个节点都为空 为true
        2. 两个节点一个为空 另一个不是, false
        3. 两个节点 不同  false
        4. 子节点 不同  false
     */
    public boolean isSameTree2(TreeNode p, TreeNode q){
        if (p == null && q == null) {
            return true;
        }
        if (p != null && q == null) {
            return false;
        }
        if (p == null && q != null){
            return false;
        }
        Queue<TreeNode> queue1 = new LinkedList<>();
        Queue<TreeNode> queue2 = new LinkedList<>();
        queue1.add(p);
        queue2.add(q);
        while (!queue1.isEmpty() && !queue2.isEmpty()){
            TreeNode node1 = queue1.poll();
            TreeNode node2 = queue2.poll();
            if (node1.val != node2.val){
                return false;
            }
            TreeNode left1 = node1.left, left2 = node2.left, right1 = node1.right, right2= node2.right;
            if (left1==null ^ left2 ==null){
                return false;
            }
            if (right1==null ^ right2 == null){
                return false;
            }
            if (left1 !=null){
                queue1.add(left1);
            }
            if (left2 !=null){
                queue2.add(left2);
            }
            if (right1 !=null){
                queue1.add(right1);
            }
            if (right2 !=null){
                queue2.add(right2);
            }
        }
        return queue1.isEmpty() && queue2.isEmpty();
    }


    public static void main(String[] args) {

    }
}
