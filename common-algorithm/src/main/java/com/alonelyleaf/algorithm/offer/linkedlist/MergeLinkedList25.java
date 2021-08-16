package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 合并两个排序的链表
 *
 * @author bijl
 * @date 7/26/21
 */
public class MergeLinkedList25 {

    /**
     * 递归
     *
     * @param list1
     * @param list2
     * @return
     */
    public ListNode merge(ListNode list1, ListNode list2) {

        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }


        if (list1.val <= list2.val) {
            list1.next = merge(list1.next, list2);
            return list1;
        } else {
            list2.next = merge(list1, list2.next);
            return list2;
        }
    }


    /**
     * 迭代
     *
     * @param list1
     * @param list2
     * @return
     */
    public ListNode merge2(ListNode list1, ListNode list2) {

        ListNode head = new ListNode(-1);
        ListNode cur = head;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                cur.next = list1.next;
                list1 = list1.next;
            } else {
                cur.next = list2.next;
                list2 = list2.next;
            }
        }


        cur = cur.next;
        if (list1 != null) {
            cur.next = list1;
        }

        if (list2 != null) {
            cur.next = list2;
        }

        return head.next;
    }
}
