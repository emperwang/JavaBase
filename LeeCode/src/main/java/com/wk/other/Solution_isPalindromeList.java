package com.wk.other;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wk
 * @Date: 2021/1/20 13:09
 * @Description
 */
public class Solution_isPalindromeList {
    /*
    请判断一个链表是否为回文链表。
    示例 1:
    输入: 1->2
    输出: false

    示例 2:
    输入: 1->2->2->1
    输出: true

    进阶：
    你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？
     */
        static  class ListNode {
         int val;
         ListNode next;
         ListNode() {}
         ListNode(int val) { this.val = val; }
         ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     }
     /*
     将当前节点存储到 数组中进行 判断
      */
    public boolean isPalindrome(ListNode head) {
        if (head== null || head.next == null) return false;
        List<Integer> lists = new ArrayList<>();
        while (head != null){
            lists.add(head.val);
            head = head.next;
        }
        int i=0;
        int end = lists.size()-1;
        while (i < lists.size()/2){
            while (i < lists.size()/2 && (lists.get(i) == lists.get(end))){
                i++;
                end--;
            }
            if (i == lists.size()/2) return true;
        }
        return false;
    }
    public boolean isPalindrome2(ListNode head) {
        if (head== null || head.next == null) return false;
        List<Integer> lists = new ArrayList<>();
        while (head != null){
            lists.add(head.val);
            head = head.next;
        }
        int i=0;
        int end = lists.size()-1;
        while (i < lists.size()/2){
            if (lists.get(i) != lists.get(end)) return false;
            i++;
            end--;
        }
        return true;
    }
    /*
    快慢指针
       1 2 3 4 5 6 7 7 6 5 4 3 2 1
    p =1 2 3 4 5 6 7
    pp=1 2 3 4 5 6 7
    s =1 2 3 4 5 6 7 7
    f =1 3 5 7 6 4 2 null
     */
    public boolean isPalindrome3(ListNode head) {
        if (head==null || head.next == null) return true;
        ListNode endOfFirst = endOfList(head);
        ListNode secondReversr = reverseList(endOfFirst.next);
        if (secondReversr==null) return false;
        boolean res = true;
        while (res && secondReversr!= null){
            if (head.val != secondReversr.val){
                res = false;
            }
            head = head.next;
            secondReversr = secondReversr.next;
        }
        return res;
    }

    public ListNode reverseList(ListNode node){
        if (node == null || node.next == null) return node;
        ListNode pre = null;
        ListNode cur = node.next;
        while (cur != null){
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        node.next = null;
        return pre;
    }

    // 利用快慢指针找到 链表的中间节点
    public ListNode endOfList(ListNode head){
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null&& fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    public static void main(String[] args) {
        final ListNode l6 = new ListNode(1);
        final ListNode l5 = new ListNode(2,l6);
        final ListNode l4 = new ListNode(3,l5);
        final ListNode l3 = new ListNode(3,l4);
        //final ListNode l2 = new ListNode(2,l3);
        final ListNode l2 = new ListNode(0);
        final ListNode l1 = new ListNode(0,l2);

        final Solution_isPalindromeList palindromeList = new Solution_isPalindromeList();
        System.out.println(palindromeList.isPalindrome3(l1));
    }
}
