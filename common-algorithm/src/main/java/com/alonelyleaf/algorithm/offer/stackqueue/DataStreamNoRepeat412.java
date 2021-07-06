package com.alonelyleaf.algorithm.offer.stackqueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 字符流中第一个不重复的字符
 * <p>
 * 请实现一个函数用来找出字符流中第一个只出现一次的字符。例如，当从字符流中只读出前两个字符 "go" 时，第一个只出现一次的字符是 "g"。
 * 当从该字符流中读出前六个字符“google" 时，第一个只出现一次的字符是 "l"。
 * <p>
 * 使用统计数组来统计每个字符出现的次数，本题涉及到的字符为都为 ASCII 码，因此使用一个大小为 128 的整型数组就能完成次数统计任务。
 * <p>
 * 使用队列来存储到达的字符，并在每次有新的字符从字符流到达时移除队列头部那些出现次数不再是一次的元素。因为队列是先进先出顺序，
 * 因此队列头部的元素为第一次只出现一次的字符。
 *
 * @author bijl
 * @date 7/5/21
 */
public class DataStreamNoRepeat412 {


    private int[] cnts = new int[128];
    private Queue<Character> queue = new LinkedList<>();

    public void Insert(char ch) {
        cnts[ch]++;
        queue.add(ch);

        // 判断当前队列中的元素，逐一检查队列中元素对应的数组位置上的个数
        while (!queue.isEmpty() && cnts[queue.peek()] > 1) {
            queue.poll();
        }
    }


    public char FirstAppearingOnce() {
        return queue.isEmpty() ? '#' : queue.peek();
    }

}
