package com.wk;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2021/1/12 11:20
 * @Description
 */
public class Solution_getIntersectionNode {
    /*
    编写一个程序，找到两个单链表相交的起始节点。
    如下面的两个链表：
    在节点 c1 开始相交。



示例 1：
输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, skipB = 3
输出：Reference of the node with value = 8
输入解释：相交节点的值为 8 （注意，如果两个链表相交则不能为 0）。从各自的表头开始算起，链表 A 为 [4,1,8,4,5]，
链表 B 为 [5,0,1,8,4,5]。在 A 中，相交节点前有 2 个节点；在 B 中，相交节点前有 3 个节点。

注意：
    如果两个链表没有交点，返回 null.
    在返回结果后，两个链表仍须保持原有的结构。
    可假定整个链表结构中没有循环。
    程序尽量满足 O(n) 时间复杂度，且仅用 O(1) 内存
     */
        static  class ListNode {
         int val;
         ListNode next;
         ListNode(int x) {
             val = x;
             next = null;
         }
        ListNode(int x,  ListNode n) {
            val = x;
            next = n;
        }
     }
     /*
       从末尾开始遍历,找到第一个不同的,前一个就是相交的点
       1.单链表翻转
       2. 从末尾开始遍历对比,找到第一个不同的
       对于连续相交的合适
       list: 1234
       list2: 3     此种情况不合适
      */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode revA = reverseNode(headA);
        ListNode revB = reverseNode(headB);
        printList(revA);
        printList(revB);
        ListNode pre = null;
        while (revA != null && revB!=null){
            if (revA.val != revB.val) {
                return pre;
            }
            pre = revA;
            revA = revA.next;
            revB = revB.next;
        }
        return pre;
    }
    // 翻转单向链表
    public ListNode reverseNode(ListNode list){
        if (list == null || list.next == null) return list;
        ListNode pre = list;
        ListNode curl = list.next;
        while (curl != null){
            ListNode temp = curl.next;
            curl.next = pre;
            pre = curl;
            curl = temp;
        }
        list.next = null;
        return pre;
    }
    /*
     将headA存储到hash表中, 遍历headB,如果存在于hash表中,则此节点就是相交的开始节点
     */
    public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        Map<Integer,Object> maps = new HashMap<>();
        int i = 0;
        while (headA != null){
            maps.put(headA.val,i++);
            headA=headA.next;
        }
        while (headB != null){
            if (maps.containsKey(headB.val)){
                return headB;
            }
            headB = headB.next;
        }
        return null;
    }

    private void printList(ListNode list){
        if (list == null ) return;
        while (list != null){
            System.out.print(list.val+" ");
            list = list.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final ListNode l4 = new ListNode(4);
        final ListNode l3 = new ListNode(3,l4);
        final ListNode l2 = new ListNode(2,l3);
        final ListNode l1 = new ListNode(1,l2);
        final ListNode ll1 = new ListNode(4);
        final ListNode ll2 = new ListNode(3,ll1);
        final ListNode lll1 = new ListNode(3);
        final Solution_getIntersectionNode solutionGetIntersectionNode = new Solution_getIntersectionNode();
        //solutionGetIntersectionNode.printList(l1);
//        System.out.println();
//        final ListNode reverseNode = solutionGetIntersectionNode.reverseNode(l1);
//        solutionGetIntersectionNode.printList(reverseNode);
        //final ListNode node = solutionGetIntersectionNode.getIntersectionNode(l1, ll2);
        //System.out.println(node==null?null:node.val);

        final ListNode node = solutionGetIntersectionNode.getIntersectionNode2(l1, lll1);
        System.out.println(node==null?null:node.val);
    }
}
