package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 删除链表中重复的结点
 *
 * @author bijl
 * @date 7/21/21
 */
public class deleteDuplicationNode {

    public ListNode deleteDuplication(ListNode pHead) {

        if (pHead == null || pHead.next == null) {
            return pHead;
        }

        ListNode next = pHead.next;

        // 如果头部元素与下一元素相同，则继续向后寻找，直到一个不同的元素，抛弃前边相同元素，开始递归
        if (pHead.val == next.val) {
            while (next != null && pHead.val == next.val) {
                next = next.next;
            }
            return deleteDuplication(next);

            // 如果头部元素与下一元素不同，则头部元素可以保留，并从下一元素开始递归，找到下一不重复元素，并挂到头部节点的下一个上
        } else {
            pHead.next = deleteDuplication(pHead.next);
            return pHead;
        }
    }
}
