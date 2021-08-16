package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 两个链表的第一个公共结点
 *
 * @author bijl
 * @date 7/27/21
 */
public class FindFirstCommonNode52 {

    /**
     * a1 -> a2 -> c1 -> c2 -> c3
     * <p>
     * b1 -> b2 - > b3 -> c1 -> c2 -> c3
     * <p>
     * 设 A 的长度为 a + c，B 的长度为 b + c，其中 c 为尾部公共部分长度，可知 a + c + b = b + c + a。
     * <p>
     * 当访问链表 A 的指针访问到链表尾部时，令它从链表 B 的头部重新开始访问链表 B；同样地，当访问链表 B 的指针访问到链表尾部时，
     * 令它从链表 A 的头部重新开始访问链表 A。这样就能控制访问 A 和 B 两个链表的指针能同时访问到交点。
     *
     * @param pHead1
     * @param pHead2
     * @return
     */
    public ListNode findFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        ListNode l1 = pHead1, l2 = pHead2;
        while (l1 != l2) {
            l1 = (l1 == null) ? pHead2 : l1.next;
            l2 = (l2 == null) ? pHead1 : l2.next;
        }

        return l1;
    }

}
