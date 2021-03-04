package com.wk.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/3/4 17:14
 * @Description
 */
public class Solution_kthLargest {
    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }


    /*
    剑指 Offer 54. 二叉搜索树的第k大节点
    给定一棵二叉搜索树，请找出其中第k大的节点。
    示例 1:
    输入: root = [3,1,4,null,2], k = 1
       3
      / \
     1   4
      \
       2
    输出: 4
    1,2,3,4    4-1=3
    示例 2:
    输入: root = [5,3,6,2,4,null,null,1], k = 3
           5
          / \
         3   6
        / \
       2   4
      /
     1
    输出: 4
     */
    // 中序遍历 数据就有序了  之后从得到的数据中获取 倒数大的值
    //
    public int kthLargest(TreeNode root, int k) {
        List<Integer> vals = new ArrayList<>();
        inorder(root, vals);
        return vals.size() - k>=0? vals.get(vals.size()-k) :0;
    }

    public void inorder(TreeNode root,List<Integer> values){
        if (root == null) return;
        inorder(root.left, values);
        values.add(root.val);
        inorder(root.right,values);
    }

    public static void main(String[] args) {

    }
}
