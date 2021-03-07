package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: Sparks
 * @Date: 2021/3/6 7:06
 * @Description
 */
public class Solution_mergeTrees {
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
    617. 合并二叉树
给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
你需要将他们合并为一个新的二叉树。合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，
否则不为 NULL 的节点将直接作为新二叉树的节点。

示例 1:
输入:
	Tree 1                     Tree 2
          1                         2
         / \                       / \
        3   2                     1   3
       /                           \   \
      5                             4   7
输出:
合并后的树:
	     3
	    / \
	   4   5
	  / \   \
	 5   4   7
     */
    // 思路一: 先序遍历两棵树, 对遍历后的两个数组合并, 再重建树
    // 思路二: 同时先序遍历两棵树, 对相同位置的节点进行合并
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {

        return preOrder(root1, root2);
    }
    /*
      三种情况:
      1. 两个节点同时存在, 那么最终的节点,就是合并后的两个节点
      2. 两个节点都不存在, 那么对应位置节点也不存在
      3. 两个节点存在一个, 那对应节点就是存在的那个节点
     */
    public TreeNode preOrder(TreeNode node1, TreeNode node2){
        if (node2 == null) return node1;
        if (node2 == null && node1 == null) return null;
        if (node1 == null) return node2;
        TreeNode merged = new TreeNode(node1.val+node2.val);
        merged.left = preOrder(node1.left, node2.left);
        merged.right = preOrder(node1.right, node2.right);
        return merged;
    }

    // 利用广度遍历 求解
    public TreeNode mergeTrees2(TreeNode root1, TreeNode root2) {

        return BFS(root1, root2);
    }

    public TreeNode BFS(TreeNode node1, TreeNode node2){
        if (node2 == null) return node1;
        if (node2 == null && node1 == null) return null;
        if (node1 == null) return node2;
        Queue<TreeNode> merged = new LinkedList<>();
        Queue<TreeNode> queueNode1 = new LinkedList<>();
        Queue<TreeNode> queueNode2 = new LinkedList<>();
        TreeNode mergedNode = new TreeNode(node1.val+node2.val);
        merged.add(mergedNode);
        queueNode1.add(node1);
        queueNode2.add(node2);
        while (!queueNode1.isEmpty() && !queueNode2.isEmpty()){
            TreeNode mergeTmp = merged.poll();
            TreeNode tmpNode1 = queueNode1.poll();
            TreeNode tmpNode2 = queueNode2.poll();
            if (tmpNode1.left != null || tmpNode2.left != null){
                if (tmpNode1.left != null && tmpNode2.left != null){
                    TreeNode tmp = new TreeNode(tmpNode1.left.val + tmpNode2.left.val);
                    mergeTmp.left = tmp;
                    merged.add(tmp);
                    queueNode1.add(tmpNode1.left);
                    queueNode2.add(tmpNode2.left);
                }else if (tmpNode1.left != null){
                    /*
                     这里直接赋值 怎么理解呢?
                     	1				1
                      2  null       null  2
                    3			        nll 3
                    这里两棵树, 当遍历 1->2    1->null 节点时, 其实因为第二棵树为null,即其下面已经没有子节点了,
                    所以这里直接把第一棵树的 左子树赋值给 merged树就可以, 因为其后面的子节点不需要再进行什么合并操作了
                     */
                    mergeTmp.left = tmpNode1.left;
                    /*
                    TreeNode tmp = new TreeNode(tmpNode1.left.val);
                    mergeTmp.left = tmp;
                    merged.add(tmp);
                    queueNode1.add(tmpNode1.left);
                     */
                }else if (tmpNode2.left != null){
                    mergeTmp.left = tmpNode2.left;
                }
            }
            if (tmpNode1.right != null || tmpNode2.right != null){
                if (tmpNode1.right != null && tmpNode2.right != null){
                    TreeNode tmp = new TreeNode(tmpNode1.right.val + tmpNode2.right.val);
                    mergeTmp.right = tmp;
                    merged.add(tmp);
                    queueNode1.add(tmpNode1.right);
                    queueNode2.add(tmpNode2.right);
                }else if (tmpNode1.right != null){
                    mergeTmp.right = tmpNode1.right;
                }else if (tmpNode2.right != null){
                    mergeTmp.right = tmpNode2.right;
                }
            }
        }
        return mergedNode;
    }
}
