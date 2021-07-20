package com.alonelyleaf.algorithm.offer.doublepoint;

/**
 * 左旋转字符串
 * <p>
 * 汇编语言中有一种移位指令叫做循环左移（ROL），现在有个简单的任务，就是用字符串模拟这个指令的运算结果。对于一个给定的字符序列 S，
 * 请你把其循环左移 K 位后的序列输出（保证 K 小于等于 S 的长度）。例如，字符序列S=”abcXYZdef”,要求输出循环左移 3 位后的结果，即“XYZdefabc”。
 *
 * <p>
 * 将字符串 S 从第 K 位置分隔成两个子字符串，并交换这两个子字符串的位置。
 *
 * @author bijl
 * @date 7/20/21
 */
public class LeftRotateString582 {

    public String LeftRotateString(String str, int n) {
        if (n >= str.length()) {
            return str;
        }

        char[] arr = str.toCharArray();
        reverse(arr, 0, n - 1);
        reverse(arr, n, str.length() - 1);
        reverse(arr, 0, str.length() - 1);
        return new String(arr);
    }

    public void reverse(char[] arr, int i, int j) {
        while (i < j) {
            swap(arr, i++, j--);
        }
    }

    public void swap(char[] arr, int i, int j) {
        char c = arr[i];
        arr[i] = arr[j];
        arr[j] = c;
    }
}
