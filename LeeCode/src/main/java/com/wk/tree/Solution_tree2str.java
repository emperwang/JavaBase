package com.wk.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: Sparks
 * @Date: 2021/3/4 21:08
 * @Description
 */
public class Solution_tree2str {
    /*
    606. 根据二叉树创建字符串
    你需要采用前序遍历的方式，将一个二叉树转换成一个由括号和整数组成的字符串。
    空节点则用一对空括号 "()" 表示。而且你需要省略所有不影响字符串与原始二叉树之间的一对一映射关系的空括号对。
    示例 1:
    输入: 二叉树: [1,2,3,4]
           1
         /   \
        2     3
       /
      4
    输出: "1(2(4))(3)"
    解释: 原本将是“1(2(4)())(3())”，
    在你省略所有不必要的空括号对之后，
    它将是“1(2(4))(3)”。

    示例 2:
    输入: 二叉树: [1,2,3,null,4]
           1
         /   \
        2     3
         \
          4
    输出: "1(2()(4))(3)"
    解释: 和第一个示例相似，
    除了我们不能省略第一个对括号来中断输入和输出之间的一对一映射关系。

    分析:
    1. 有左右两个节点
        1
       / \
      2   3  输出: 1 (2(4)(5))(3)
     / \
    4   5
    2. 没有子节点
        如上面的3 不需要为空节点添加括号
    3. 只有左子节点
        1
       / \
      2   3  输出: 1 (2(4))(3)
     /
    4
    4. 只有右子节点
        1
       / \
      2   3  输出: 1 (2()(5))(3)
       \
        5
     */

    class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }
    public String tree2str(TreeNode t) {
        if (t == null){
            return "";
        }
        if (t.left == null && t.right == null){
            return t.val + "";
        }
        if (t.right == null){
            return new StringBuilder().append(t.val).append("(")
                    .append(tree2str(t.left)).append(")").toString();
        }
        return new StringBuilder().append(t.val).append("(")
                .append(tree2str(t.left))
                .append(")(")
                .append(tree2str(t.right)).append(")").toString();
    }

    public String tree2str2(TreeNode t){
        if (t == null) return "";
        LinkedList<TreeNode> stack = new LinkedList<>();
        stack.add(t);
        LinkedList<TreeNode> visited = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        while (! stack.isEmpty()){
            TreeNode node = stack.peekLast();
            if (visited.contains(node)){
                builder.append(")");
                stack.pollLast();
            }else{
                visited.add(node);
                builder.append("(").append(node.val);
                if (node.left == null && node.right != null){
                    builder.append("()");
                }
                if (node.right != null){
                    stack.add(node.right);
                }
                if (node.left != null){
                    stack.add(node.left);
                }
            }
        }
        return builder.toString().substring(1,builder.length()-1);
    }
}
