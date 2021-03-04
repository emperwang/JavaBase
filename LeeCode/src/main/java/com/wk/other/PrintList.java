package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/10/19 14:59
 * @Description
 */
/*
输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。

示例 1：

输入：head = [1,3,2]
输出：[2,3,1]

 */

class ListNode {
  int val;
  ListNode next;
  ListNode(int x) { val = x; }
}

public class PrintList {
    public  static int[] reversePrint(ListNode head) {
        /*
        1. 遍历一遍查看有几个元素,记为count
        2. 创建count长度的数组
        3. 再次遍历list,从数组的最后开始放置数据
         */
        int[] ints = {};
        ListNode tmp = head;
        if (head != null){
            int count = 1;
            while (head.next != null){
                count++;
                head=head.next;
            }
            head = tmp;
            System.out.println("count = "+count);
            int[] arr = new int[count];
            while (head != null) {
                arr[--count] = head.val;
                head=head.next;
            }
            return arr;
        }
        return ints;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        head.next = node1;
        node1.next=node2;
        final int[] ints = reversePrint(head);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}

