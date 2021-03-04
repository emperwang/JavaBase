package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/7 13:17
 * @Description
 */
public class Solution_mergeTwoLists {
    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    /*
    将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
    示例：
    输入：1->2->4, 1->3->4
    输出：1->1->2->3->4->4
     */
    // 修改链表的指针,穿针引线
    // O(n)
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null){
            return null;
        }
        ListNode l1Ptr = l1;
        ListNode end = new ListNode();
        l1 = end;
        while (l1Ptr != null && l2 != null){
            if (l1Ptr.val <= l2.val){
                end.next = l1Ptr;
                end = l1Ptr;
                l1Ptr = l1Ptr.next;
            }else{
                end.next = l2;
                end = l2;
                l2 = l2.next;
            }
        }
        while (l1Ptr != null){
            end.next = l1Ptr;
            end = l1Ptr;
            l1Ptr = l1Ptr.next;
        }

        while (l2 != null){
            end.next = l2;
            end = l2;
            l2 = l2.next;
        }
        return l1.next;
    }

    public static void main(String[] args) {
        final Solution_mergeTwoLists mergeTwoLists = new Solution_mergeTwoLists();
        final ListNode l3 = new ListNode(3);
        final ListNode l2 = new ListNode(2,l3);
        final ListNode l1 = new ListNode(1,l2);

        final ListNode k3 = new ListNode(5);
        final ListNode k2 = new ListNode(4,k3);
        final ListNode k1 = new ListNode(3,k2);

        ListNode node = mergeTwoLists.mergeTwoLists(l1, k1);
        while (node != null){
            System.out.println(node.val);
            node = node.next;
        }
    }
}

