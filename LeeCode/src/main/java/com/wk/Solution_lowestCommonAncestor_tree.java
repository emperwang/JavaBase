package com.wk;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: wk
 * @Date: 2021/1/20 15:25
 * @Description
 */
public class Solution_lowestCommonAncestor_tree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
        TreeNode(int x, TreeNode left,TreeNode rig) {
            val = x;
            this.left = left;
            this.right = rig;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeNode treeNode = (TreeNode) o;
            return val == treeNode.val;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }
    }
    /*
    给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
     */
    // 通过分层遍历二叉树, 来记录每个节点的父节点
    public TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        Map<TreeNode,TreeNode> parent = new HashMap<>();
        parent.put(root,null); // 根节点没有父节点
        LinkedBlockingQueue<TreeNode> queue= new LinkedBlockingQueue<>();
        queue.add(root);
        while (!(parent.containsKey(p) && parent.containsKey(q))){
            TreeNode tmp = queue.poll();
            // 记录父节点
            if (tmp.left != null){
                queue.offer(tmp.left);
                parent.put(tmp.left,tmp);
            }
            if (tmp.right != null){
                queue.offer(tmp.right);
                parent.put(tmp.right,tmp);
            }
        }
        // 获取p的父节点集合
        HashSet<TreeNode> ancestors = new HashSet<>();
        while (p != null){
            ancestors.add(p);
            p = parent.get(p);
        }
        // 获取q的父节点,查找最早在p父节点集合出现的q的父节点
        while (q != null){
            if (ancestors.contains(q)) return q;
            q = parent.get(q);
        }
        return null;
    }


    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root.val==p.val || root.val==q.val) {
            return root;
        }
        TreeNode left = lowestCommonAncestor2(root.left, p,q);
        TreeNode right = lowestCommonAncestor2(root.right,p,q);
        if (left == null){
            return right;
        }
        if (right == null){
            return left;
        }
        // 如果left  right都存在,说明p和q在root的分别在左子树和右子树,直接返回当前的root就可以
        return root;
    }
    /*
        3
       / \
      5   1
    /  \ / \
   6   2 0  8
      / \
     7   4
     */
    public static void main(String[] args) {
        // [3,5,1,6,2,0,8,null,null,7,4]  5  4
        final TreeNode root = new TreeNode(3);
        final TreeNode n4 = new TreeNode(4);
        final TreeNode n7 = new TreeNode(7);
        final TreeNode n2 = new TreeNode(2, n7, n4);
        final TreeNode n6 = new TreeNode(6);
        final TreeNode n0 = new TreeNode(0);
        final TreeNode n8 = new TreeNode(8);
        final TreeNode n1 = new TreeNode(1, n0, n8);
        final TreeNode n5 = new TreeNode(5, n6, n2);
        root.left = n5;
        root.right = n1;
        final Solution_lowestCommonAncestor_tree solutionLowestCommonAncestorTree = new Solution_lowestCommonAncestor_tree();
        final TreeNode node = solutionLowestCommonAncestorTree.lowestCommonAncestor1(root, n5, n4);
        System.out.println(node.val);

        final TreeNode node1 = solutionLowestCommonAncestorTree.lowestCommonAncestor2(root, n5, n4);
        System.out.println(node1.val);
    }

}
