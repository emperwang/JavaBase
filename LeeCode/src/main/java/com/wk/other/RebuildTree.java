package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/10/19 16:12
 * @Description
 */
/*
输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。

例如，给出
前序遍历 preorder = [3,9,20,15,7]
中序遍历 inorder = [9,3,15,20,7]

返回如下的二叉树：
    3
   / \
  9  20
    /  \
   15   7
 */
public class RebuildTree {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) {
            val = x;
        }
    }

    int[] preorder = {3,9,20,15,7};
    int[] midorder = {9,3,15,20,7};

    public TreeNode buildTree(int[] preorder, int[] midorder) {
        // 1. 没有数据时,则返回null
        if (preorder == null || preorder.length<=0 || midorder == null|| midorder.length<=0){
            return null;
        }
        // 2. 前序只有一个节点时,直接作为根节点返回
        if (preorder.length == 1){
            return new TreeNode(preorder[0]);
        }
        // 3. 创建根节点
        TreeNode root = new TreeNode(preorder[0]);
        // 4.1 先在中序中查找根节点位置
        int index=0;
        while (!(midorder[index++] == preorder[0])){}
        System.out.println("index="+index);
        // 4.2 获取左子树的前序  中序
        int[] preLeftList = new int[index-1];
        int[] midLeftList = new int[index-1];
        for (int i=1;i<index;i++){
            preLeftList[i-1] = preorder[i];
            midLeftList[i-1] = midorder[i-1];
        }

        root.left = buildTree(preLeftList,midLeftList);
        // 5.1 获取右子树前序  中序
        int[] preRightList = new int[preorder.length - index];
        int[] midRightList = new int[midorder.length - index];
        int j = 0;
        for (int i=index;i<preorder.length;i++){
            preRightList[j] = preorder[i];
            midRightList[j] = midorder[i];
            j++;
        }
        root.right = buildTree(preRightList,midRightList);
        return root;
    }

    public static void main(String[] args) {
        final RebuildTree tree = new RebuildTree();
        final TreeNode treeNode = tree.buildTree(tree.preorder, tree.midorder);
        System.out.println(treeNode);
    }
}
