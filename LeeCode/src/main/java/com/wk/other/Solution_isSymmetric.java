package com.wk.other;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: wk
 * @Date: 2021/1/8 18:13
 * @Description
 */
public class Solution_isSymmetric {
    /*
    给定一个二叉树，检查它是否是镜像对称的。
    例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
        1
       / \
      2   2
     / \ / \
    3  4 4  3

    但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
        1
       / \
      2   2
       \   \
       3    3

    进阶：
    你可以运用递归和迭代两种方法解决这个问题吗？
     */
   static class TreeNode {
      Integer val;
      TreeNode left;
      TreeNode right;
      TreeNode(Integer x) { val = x; }
  }
   /*
   使用递归去进行判断
   1. 左子树的右节点 和 右子树的左节点 比较且相同, 左子树的左节点和右子树的右节点
   那么可以使用两个指针分别指向两个子树的根节点,并且按照不同的方向移动 并进行比较,都相同则互为镜像
    */
    public boolean isSymmetric(TreeNode root) {
        return check(root,root);
        //return queueCheck(root,root);
    }
    public boolean isSymmetric2(TreeNode root) {
        if (root == null){
            return true;
        }
        if (root.right == null && root.left == null){
            return true;
        }else if (root.right != null && root.left != null){}
        else {
            return false;
        }

        return queueCheck(root.right,root.left);
    }
    private boolean check(TreeNode lrtoot, TreeNode rroot){
        if (lrtoot == null && rroot == null){
            return true;
        }
        if (lrtoot == null || rroot == null) return false;
        return (rroot.val == rroot.val) && check(lrtoot.left,rroot.right) && check(lrtoot.right, rroot.left);
    }

    /*
     同样可以使用队列把上面递归 修改为遍历
     1.每次从队列中取出两个节点相比较,相同,则继续
     2.存入队列中时,放入成对放入 左子树.left --> 右子树.right,  左子树.right --> 右子树.left
     */
    private boolean queueCheck(TreeNode lroot, TreeNode rroot){
        final LinkedBlockingDeque<TreeNode> deque = new LinkedBlockingDeque<>();
        if (lroot != null && rroot != null){
            deque.push(lroot);
            deque.push(rroot);
        }else {
            return false;
        }
        while (!deque.isEmpty()){
            final TreeNode top1 = deque.pop();
            final TreeNode top2 = deque.pop();
            if (top1.val != top2.val) return false;
            if (top1.left != null && top2.right != null){
                deque.push(top1.left);
                deque.push(top2.right);
            }else if (top1.left == null && top2.right == null){ }else{
                return false;
            }
            if (top1.right != null && top2.left != null){
                deque.push(top1.right);
                deque.push(top2.left);
            }else if (top1.right == null && top2.left == null){}
            else{
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode l1 = new TreeNode(2);
        TreeNode r1 = new TreeNode(2);
        TreeNode l2 = new TreeNode(3);
        TreeNode l3 = new TreeNode(4);
        TreeNode r3 = new TreeNode(4);
        TreeNode r4 = new TreeNode(3);
        root.left = l1;
        root.right=r1;
        l1.left = l2;
        l1.right = l3;
        r1.left = r3;
        r1.right = r4;

        final Solution_isSymmetric solutionIsSymmetric = new Solution_isSymmetric();
        System.out.println(solutionIsSymmetric.isSymmetric(root));
        System.out.println(solutionIsSymmetric.isSymmetric2(root));

        TreeNode root2 = new TreeNode(1);
        TreeNode ll1 = new TreeNode(2);
        TreeNode rr1 = new TreeNode(2);
        TreeNode ll3 = new TreeNode(3);
        TreeNode rr4 = new TreeNode(3);
        root2.left = ll1;
        root2.right = rr1;
        ll1.right = ll3;
        rr1.right = rr4;
        System.out.println(solutionIsSymmetric.isSymmetric(root2));
        System.out.println(solutionIsSymmetric.isSymmetric2(root2));

        TreeNode root3 = new TreeNode(1);
        TreeNode lll1 = new TreeNode(0);
        root3.left = lll1;
        System.out.println(solutionIsSymmetric.isSymmetric2(root3));
    }
}
