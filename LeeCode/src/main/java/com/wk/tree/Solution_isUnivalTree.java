package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 12:48
 * @Description
 */
public class Solution_isUnivalTree {

    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }
    /*
    965. 单值二叉树
如果二叉树每个节点都具有相同的值，那么该二叉树就是单值二叉树。
只有给定的树是单值二叉树时，才返回 true；否则返回 false。

示例 1：
    1
   1  1
  1 1   1
输入：[1,1,1,1,1,null,1]
输出：true

示例 2：
输入：[2,2,2,5,2]
输出：false
     */

    public boolean isUnivalTree(TreeNode root) {
        if (root == null || (root.left == null && root.right==null)) return true;
        return isUnval(root, root.val);
    }
    //  递归实现
    public boolean isUnval(TreeNode node, int val){
        if (node == null) return true;
        return (node.val == val) && (isUnval(node.left,val)) && (isUnval(node.right,val));
    }


    // 层次遍历实现
    public boolean isUnivalTree2(TreeNode root){
        if (root == null || (root.left == null && root.right==null)) return true;
        Queue<TreeNode> nodes = new LinkedList<>();
        nodes.add(root);
        int val = root.val;
        boolean flag = true;
        while (!nodes.isEmpty()){
            TreeNode tmp = nodes.poll();
            if (tmp.val != val) {
                flag = false;
                break;
            }
            if (tmp.left != null){
                nodes.add(tmp.left);
            }
            if (tmp.right != null){
                nodes.add(tmp.right);
            }
        }
        return flag;
    }
}
