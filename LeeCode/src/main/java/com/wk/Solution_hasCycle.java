package com.wk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wk
 * @Date: 2021/1/11 18:55
 * @Description
 */
public class Solution_hasCycle {
    /*
    给定一个链表，判断链表中是否有环。
    如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，
    我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
    注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
    如果链表中存在环，则返回 true 。 否则，返回 false 。

    进阶：
    你能用 O(1)（即，常量）内存解决此问题吗？

    示例 1：
    输入：head = [3,2,0,-4], pos = 1
    输出：true
    解释：链表中有一个环，其尾部连接到第二个节点。

    示例 2：
    输入：head = [1,2], pos = 0
    输出：true
    解释：链表中有一个环，其尾部连接到第一个节点。

    示例 3：
    输入：head = [1], pos = -1
    输出：false
    解释：链表中没有环。

    提示：
        链表中节点的数目范围是 [0, 104]
        -105 <= Node.val <= 105
        pos 为 -1 或者链表中的一个 有效索引 。
     */
 static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
  }
    /*
     1.  使用一个容器存储遍历到的节点
     2. 如果继续遍历,发现遍历到得节点存在于容器中,则存在环
     */
    public boolean hasCycle(ListNode head) {
        if (head == null) return false;
        List<ListNode> lists = new ArrayList<>();
        while (head != null){
            if (lists.contains(head)) return true;
            lists.add(head);
            head = head.next;
        }
        return false;
    }

    /*
       快慢指针,当快慢指针重叠时, 说明存在环
     */
    public boolean hasCycle2(ListNode head){
        if (head == null) return false;
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast){
            if (fast == null || fast.next == null){
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

    public static void main(String[] args) {
        final ListNode l1 = new ListNode(1);
        final ListNode l2 = new ListNode(1);
        final ListNode l3 = new ListNode(1);
        final ListNode l4 = new ListNode(1);
        final ListNode l5 = new ListNode(1);
        final ListNode l6 = new ListNode(1);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l1;
        final Solution_hasCycle solutionHasCycle = new Solution_hasCycle();
        System.out.println(solutionHasCycle.hasCycle(l1));
        System.out.println(solutionHasCycle.hasCycle2(l1));
    }
}
