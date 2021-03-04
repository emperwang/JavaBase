package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/13 13:09
 * @Description
 */
public class Solution_removeElements {

  static class ListNode {
     int val;
     ListNode next;
     ListNode(int x) { val = x; }
     ListNode(int x,ListNode n) { val = x; this.next = n;}
 }
 /*
 删除链表中等于给定值 val 的所有节点。

    示例:
    输入: 1->2->6->3->4->5->6, val = 6
    输出: 1->2->3->4->5
  */
    // 1. 添加一个哨兵节点来进行删除
    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
       // 借助哨兵节点
        final ListNode sentinel = new ListNode(-1);
        sentinel.next = head;
        ListNode pre = sentinel, cur = sentinel.next;
        while (cur != null){
            if (cur.val==val){
                pre.next = cur.next;
            }else{
                pre = cur;
            }
            cur = cur.next;
        }
        return sentinel.next;
    }
    // 2. 删除头结点时 需要特殊考虑
    public ListNode removeElements2(ListNode head, int val) {
        if (head == null) return null;
        while (head != null && head.val==val){
            head = head.next;
        }
        if (head == null) return null;
        ListNode pre = head, cur = head.next;
        while (cur != null){
            if (cur.val == val){
                pre.next = cur.next;
            }else{
                pre = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    // 递归
    public ListNode removeElements3(ListNode head, int val){
        if (head == null) return null;
        head.next = removeElements3(head.next,val);
        if (head.val == val){
            return head.next;
        }else{
            return head;
        }
    }

    public static void main(String[] args) {
        final ListNode l6 = new ListNode(6);
        final ListNode l5 = new ListNode(5,l6);
        final ListNode l4 = new ListNode(1,l5);
        final ListNode l3 = new ListNode(6,l4);
        final ListNode l2 = new ListNode(2,l3);
        final ListNode l1 = new ListNode(1,l2);
        final ListNode ll1 = new ListNode(1);
        final Solution_removeElements solutionRemoveElements = new Solution_removeElements();
        final ListNode node = solutionRemoveElements.removeElements(l1, 6);
        System.out.println(node);
    }
}
