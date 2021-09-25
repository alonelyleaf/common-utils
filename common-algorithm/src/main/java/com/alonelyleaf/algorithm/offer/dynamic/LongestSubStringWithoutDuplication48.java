package com.alonelyleaf.algorithm.offer.dynamic;

import java.util.Arrays;

/**
 * 最长不含重复字符的子字符串
 * <p>
 * 输入一个字符串（只包含 a~z 的字符），求其最长不含重复字符的子字符串的长度。例如对于 arabcacfr，最长不含重复字符的子字符串为 acfr，长度为 4。
 *
 * @author bijl
 * @date 8/18/21
 */
public class LongestSubStringWithoutDuplication48 {


    /**
     * 动态规划
     * <p>
     * （1）建立数组，保存 每个字符上一次出现的位置，用以判断重复
     * （2）建立变量，记录最大长度及当前长度
     * （3）从左往右遍历数组，如果未出现过或不再当前长度序列中，则增加当前长度。否则比较并设置最大长度，并将当前长度改为从上次出现之后到当前位置
     * （4）每次遍历字符，都将字符放入数组中，用于记录上次出现的位置
     *
     * @param str
     * @return
     */
    public int longestSubStringWithoutDuplication(String str) {

        int maxLen = 0;
        int curLen = 0;

        //记录每个字符上一次出现的位置索引
        int[] preIndexs = new int[26];

        Arrays.fill(preIndexs, -1);
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i) - 'a';
            int preI = preIndexs[c];

            // 如果未出现过，或出现的位置在当前长度之前即不包含在当前长度中，则增加
            if (preI == -1 || i - preI > curLen) {
                curLen++;
            } else {
                maxLen = Math.max(maxLen, curLen);
                curLen = i - preI;
            }

            preIndexs[c] = i;
        }

        maxLen = Math.max(maxLen, curLen);

        return maxLen;
    }

}
