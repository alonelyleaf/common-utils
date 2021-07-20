package com.alonelyleaf.algorithm.offer.doublepoint;

/**
 * 翻转单词序列
 * <p>
 * Input:
 * "I am a student."
 * <p>
 * Output:
 * "student. a am I"
 *
 * @author bijl
 * @link http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/58.1%20翻转单词顺序列.html
 * @date 7/20/21
 */
public class ReverseSentence581 {

    /**
     * 先翻转每个单词，再翻转整个字符串。
     * <p>
     * 题目应该有一个隐含条件，就是不能用额外的空间。虽然 Java 的题目输入参数为 String 类型，需要先创建一个字符数组使得空间复杂度为 O(N)，
     * 但是正确的参数类型应该和原书一样，为字符数组，并且只能使用该字符数组的空间。任何使用了额外空间的解法在面试时都会大打折扣，包括递归解法。
     *
     * @param str
     * @return
     */
    public String reverseSentence(String str) {
        int n = str.length();
        char[] chars = str.toCharArray();
        int i = 0, j = 0;
        while (j <= n) {
            // 如果遇到空格或到达末尾，则对单词进行翻转，末尾翻转是为了翻转最后一个单词
            if (j == n || chars[j] == ' ') {
                reverse(chars, i, j - 1);
                i = j + 1;
            }
            j++;
        }
        reverse(chars, 0, n - 1);
        return new String(chars);
    }

    private void reverse(char[] c, int i, int j) {
        while (i < j) {
            swap(c, i++, j--);
        }
    }

    private void swap(char[] c, int i, int j) {
        char t = c[i];
        c[i] = c[j];
        c[j] = t;
    }

}
