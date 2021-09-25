package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 删除链表的倒数第 N 个结点
 *
 * @author bijl
 * @date 8/22/21
 */
public class RemoveNthFromEnd {

    public ListNode removeNthFromEnd(ListNode head, int n) {

        ListNode cur = head;
        // 向后遍历，使 cur 到达倒数第n个节点，倒数第n，只需移动n-1次
        while (--n > 0 && cur.next != null) {
            cur = cur.next;
        }

        if (n > 0) {
            return null;
        }

        ListNode p0 = new ListNode(-1);
        p0.next = head;
        ListNode p1 = p0;

        // 从虚拟头节点和cur节点同时向后移动，当cur到达尾节点时，p0到达倒数第n节点的前一节点
        while (cur.next != null) {
            p0 = p0.next;
            cur = cur.next;
        }

        p0.next = p0.next.next;

        return p1.next;
    }

    public static void main(String[] args) {

        ListNode head = new ListNode(1);
        ListNode cur = head;
        for (int i = 2; i <= 5; i++) {
            ListNode node = new ListNode(i);
            cur.next = node;
            cur = node;
        }

        RemoveNthFromEnd removeNthFromEnd = new RemoveNthFromEnd();
        ListNode newHead = removeNthFromEnd.removeNthFromEnd(head, 6);

        System.out.println();
    }
}
