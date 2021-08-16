package com.alonelyleaf.algorithm.offer.linkedlist;

/**
 * 复杂链表的复制
 *
 * 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针指向任意一个节点），返回结果为复制后复杂链表的 head。
 *
 * @author bijl
 * @date 7/27/21
 */
public class CloneRandomListNode35 {

    public static class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }

    /**
     * 第一步，在每个节点的后面插入复制的节点。
     * 第二步，对复制节点的 random 链接进行赋值。
     * 第三步，拆分。
     */
    public RandomListNode Clone(RandomListNode pHead) {

        if (pHead == null) {
            return null;
        }

        // 第一步，在每个节点的后面插入复制的节点。
        RandomListNode cur = pHead;
        while (cur != null) {
            RandomListNode newNode = new RandomListNode(cur.label);
            newNode.next = cur.next;
            cur.next = newNode;
            cur = newNode.next;
        }

        // 第二步，对复制节点的 random 链接进行赋值。
        cur = pHead;
        while (cur != null) {
            RandomListNode newClone = cur.next;
            if (cur.random != null) {
                newClone.random = cur.random.next;
            }
            cur = newClone.next;
        }

        // 第三步，拆分，通过循环，使当前节点每次指向下下个节点，恢复原有链表并提取出新链表
        cur = pHead;
        RandomListNode newListHead = cur.next;
        while (cur.next != null) {
            RandomListNode next = cur.next;
            cur.next = next.next;
            cur = next;
        }

        return newListHead;
    }

}
