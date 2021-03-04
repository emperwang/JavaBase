package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/20 13:12
 * @Description
 */
public class Solution_deleteNode {
    class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
    /*
    请编写一个函数，使其可以删除某个链表中给定的（非末尾）节点。传入函数的唯一参数为 要被删除的节点 。
    现有一个链表 -- head = [4,5,1,9]，它可以表示为:
        4 --> 5 --> 1--> 9

    示例 1：
    输入：head = [4,5,1,9], node = 5
    输出：[4,1,9]
    解释：给定你链表中值为 5 的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9.
     */

    public void deleteNode(ListNode node) {
      node.val = node.next.val;
      node.next = node.next.next;
    }

    public static void main(String[] args) {

    }
}
