package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/18 10:43
 * @Description
 */
public class Solution_reverseList {
    static class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
    ListNode(int x, ListNode n){
        this.val = x;
        this.next = n;
    }
}
    /*
    反转一个单链表。
    示例:
    输入: 1->2->3->4->5->NULL
    输出: 5->4->3->2->1->NULL
    进阶:
    你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode pre = head;
        ListNode cur = head.next;
        ListNode temp = cur.next;
        while (cur != null){
            temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        head.next = null;
        return pre;
    }

    public ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return  newHead;
    }

    public void printList(ListNode head){
        while (head != null){
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final ListNode l5 = new ListNode(5);
        final ListNode l4 = new ListNode(4,l5);
        final ListNode l3 = new ListNode(3,l4);
        final ListNode l2 = new ListNode(2,l3);
        final ListNode l1 = new ListNode(1,l2);
        final Solution_reverseList reverseList = new Solution_reverseList();
        reverseList.printList(l1);
        final ListNode listNode = reverseList.reverseList2(l1);
        reverseList.printList(listNode);
//        Integer obj1 = null;
//        List<Integer> lists = new ArrayList<>();
//        lists.add(obj1);
//        System.out.println(lists.size());
    }
}
