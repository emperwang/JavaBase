package com.wk.tree;

/**
 * @author: Sparks
 * @Date: 2021/3/4 20:50
 * @Description
 */
public class Solution_sumRootToLeaf {
    /*
    1022. 从根到叶的二进制数之和

给出一棵二叉树，其上每个结点的值都是 0 或 1 。每一条从根到叶的路径都代表一个从最高有效位开始的二进制数。例如，
如果路径为 0 -> 1 -> 1 -> 0 -> 1，那么它表示二进制数 01101，也就是 13 。
对树上的每一片叶子，我们都要找出从根到该叶子的路径所表示的数字。
返回这些数字之和。题目数据保证答案是一个 32 位 整数。

示例 1：
        1
       / \
     0     1
    / \   / \
   0   1 0   1
输入：root = [1,0,1,0,1,0,1]
输出：22
解释：(100) + (101) + (110) + (111) = 4 + 5 + 6 + 7 = 22
     */
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

    public int sumRootToLeaf(TreeNode root) {
        return  sum_detail(root,0);
    }

    public int sum_detail(TreeNode node, int sum){
        if (node == null) return 0;
        sum = sum  * 2 + node.val;
        if (node.left == null && node.right == null) { //已经遍历到了 叶子节点
            return sum;
        }
        // 左节点
        int left = sum_detail(node.left, sum);
        // 右节点
        int right = sum_detail(node.right, sum);
        return left + right;
    }
}
