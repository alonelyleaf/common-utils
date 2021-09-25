package com.alonelyleaf.algorithm.offer.search;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 字符串的排列
 * <p>
 * 输入一个字符串，打印出该字符串中字符的所有排列，你可以以任意顺序返回这个字符串数组。例如输入字符串abc,则按字典序打印出由字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。
 * 输入一个字符串,长度不超过9(可能有字符重复),字符只包括大小写字母。
 *
 * @author bijl
 * @date 8/17/21
 */
public class StringPermutation38 {

    private ArrayList<String> ret = new ArrayList<>();

    public ArrayList<String> Permutation(String str) {
        if (str.length() == 0) {
            return ret;
        }

        // 将字符串转为数组并进行排序，因为题目要求任意字符排序，且可以重复，这样可以让相同字符移动到一起
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        backtracking(chars, new boolean[chars.length], new StringBuilder());
        return ret;
    }

    private void backtracking(char[] chars, boolean[] hasUsed, StringBuilder s) {
        if (s.length() == chars.length) {
            ret.add(s.toString());
            return;
        }
        for (int i = 0; i < chars.length; i++) {

            // 如果当前字符已使用，则跳过
            if (hasUsed[i]) {
                continue;
            }

            // 保证不重复，如果当前字符与前一字符相同，且前一字符还未使用，则跳过
            if (i != 0 && chars[i] == chars[i - 1] && !hasUsed[i - 1]) {
                continue;
            }

            // 将当前字符加入
            hasUsed[i] = true;
            s.append(chars[i]);

            // 继续向后遍历
            backtracking(chars, hasUsed, s);

            // 完成从此字符向后的遍历，将此字符从串中移除
            s.deleteCharAt(s.length() - 1);
            hasUsed[i] = false;
        }
    }
}
