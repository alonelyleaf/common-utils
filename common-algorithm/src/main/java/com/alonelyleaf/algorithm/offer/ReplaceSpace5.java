package com.alonelyleaf.algorithm.offer;

/**
 * 替换空格
 *
 * @author bijl
 * @date 6/28/21
 */
public class ReplaceSpace5 {

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     * <p>
     * ① 在字符串尾部填充任意字符，使得字符串的长度等于替换之后的长度。因为一个空格要替换成三个字符（%20），所以当遍历到一个空格时，需要在尾部填充两个任意字符。
     * ② 令 P1 指向字符串原来的末尾位置，P2 指向字符串现在的末尾位置。P1 和 P2 从后向前遍历，当 P1 遍历到一个空格时，就需要令 P2 指向的位置依次填充 02%（注意是逆序的），
     * 否则就填充上 P1 指向字符的值。从后向前遍是为了在改变 P2 所指向的内容时，不会影响到 P1 遍历原来字符串的内容。
     * ③ 当 P2 遇到 P1 时（P2 <= P1），或者遍历结束（P1 < 0），退出。
     *
     * @param s string字符串
     * @return string字符串
     * @link http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/5.%20替换空格.html#题目描述
     */
    public String replaceSpace(String s) {
        // write code here
        StringBuilder sb = new StringBuilder(s);
        int p1 = sb.length() - 1;
        for (int i = 0; i <= p1; i++) {
            if (sb.charAt(i) == ' ') {
                sb.append("  ");
            }
        }

        int p2 = sb.length() - 1;
        while (p1 >= 0 && p2 > p1) {

            char c = sb.charAt(p1--);
            if (c == ' ') {
                sb.setCharAt(p2--, '0');
                sb.setCharAt(p2--, '2');
                sb.setCharAt(p2--, '%');
            } else {
                sb.setCharAt(p2--, c);
            }
        }

        return sb.toString();
    }

}
