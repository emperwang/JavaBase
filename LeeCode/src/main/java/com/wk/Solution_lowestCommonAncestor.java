package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/20 13:10
 * @Description
 */
public class Solution_lowestCommonAncestor {
    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }
    /*
    给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
    百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，
    满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”

    例如，给定如下二叉搜索树:  root = [6,2,8,0,4,7,9,null,null,3,5]
            6
           / \
         2     8
       /  \   /  \
     0     4 7    9
          / \
         3   5

    示例 1:
    输入: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
    输出: 6
    解释: 节点 2 和节点 8 的最近公共祖先是 6。
     */
    /*
    左子树的所有节点都小于当前节点，右子树的所有节点都大于当前节点，并且每棵子树都具有上述特点，所以这题就好办了，从更节点开始遍历
    如果两个节点值都小于根节点，说明他们都在根节点的左子树上，我们往左子树上找
    如果两个节点值都大于根节点，说明他们都在根节点的右子树上，我们往右子树上找
    如果一个节点值大于根节点，一个节点值小于根节点，说明他们他们一个在根节点的左子树上一个在根节点的右子树上，
    那么根节点就是他们的最近公共祖先节点。
     */
    public TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
        while ((p.val-root.val) * (q.val-root.val) > 0){
            root = (p.val>root.val) ? root.right:root.left;
        }
        return root;
    }

    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (p.val <= root.val && q.val >= root.val){
            return root;
        }
        if (p.val <= root.val &&q.val<= root.val){
            root = root.left;
            lowestCommonAncestor2(root,p,q);
        }
        if (p.val>root.val && q.val>root.val){
            root = root.right;
            lowestCommonAncestor2(root,p,q);
        }
        return null;
    }

    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if ((p.val-root.val)*(q.val-root.val) <=0) {
            return root;
        }
        return lowestCommonAncestor3((p.val<root.val?root.left:root.right),p,q);
    }

    public static void main(String[] args) {

    }
}
