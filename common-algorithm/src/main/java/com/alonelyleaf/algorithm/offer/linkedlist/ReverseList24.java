package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 翻转链表
 *
 * @author bijl
 * @date 7/26/21
 */
public class ReverseList24 {

    /**
     * 递归
     *
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode next = head.next;
        head.next = null;
        ListNode newHead = reverseList(next);
        next.next = head;

        return newHead;
    }

    /**
     * 迭代
     * <p>
     * 使用头插法
     *
     * @param head
     * @return
     */
    public ListNode reverseList2(ListNode head) {

        ListNode newList = new ListNode(-1);
        while (head != null) {
            ListNode next = head.next;
            head.next = newList.next;
            newList.next = head;
            head = next;
        }

        return newList.next;
    }
}
