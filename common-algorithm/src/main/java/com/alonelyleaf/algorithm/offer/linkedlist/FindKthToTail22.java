package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 链表中倒数第 K 个结点
 * <p>
 * 设链表的长度为 N。设置两个指针 P1 和 P2，先让 P1 移动 K 个节点，则还有 N - K 个节点可以移动。此时让 P1 和 P2 同时移动，
 * 可以知道当 P1 移动到链表结尾时，P2 移动到第 N - K 个节点处，该位置就是倒数第 K 个节点。
 *
 * @author bijl
 * @date 7/22/21
 */
public class FindKthToTail22 {


    public ListNode findKthToTail(ListNode head, int k) {
        if (head == null) {
            return null;
        }

        ListNode p1 = head;
        while (p1 != null && k-- > 0) {
            p1 = p1.next;
        }

        ListNode p2 = head;
        while (p1.next != null) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p2;
    }
}
