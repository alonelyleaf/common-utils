package com.alonelyleaf.algorithm.interview;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里二面
 *
 * @author bijl
 * @date 8/11/21
 */
public class Interview {

    //1. 输入：最大子串和如【2,4，-8,3,-1,7,4，-2】
    //输出：最大子串和应为 13以及 对应子串{3，-1,7,4}，
    public int MaxSubArray(int[] array) {
        int max = array[0];
        int temp = array[0];
        int indexFirst = 0;
        int indexLast = 0;
        for (int i = 1; i < array.length; i++) {
            if (temp < 0) {
                temp = array[i];
                indexFirst = i;
            } else {
                temp += array[i];
            }

            if (max < temp) {
                max = temp;
                indexLast = i;
            }
        }


        return max;
    }


    public void printSum(int[] a) {

        int len = a.length;
        int sum = a[0];
        int left = 0, right = 0, p = 0, q = 0;

        while (q < len) {
            int cur = getSum(a, p, q);
            if (cur > sum) {
                sum = cur;
                left = p;
                right = q;
            }

            q++;

            // 如果当前和为负数，则从下一元素重新开始
            if (cur > 0) {
                p = q;
            }
        }

        // 打印结果
        System.out.println();
    }

    public void printSum2(int[] a) {

        int len = a.length;
        int sum = a[0];
        int left = 0, right = 0;
        for (int i = 0; i < len; i++) {

            for (int j = i; j < len; j++) {
                int cur = getSum(a, i, j);
                if (cur > sum) {
                    sum = cur;
                    left = i;
                    right = j;
                }
            }
        }

        System.out.println(sum);
        System.out.println("{");
        for (int i = left; i <= right; i++) {

            System.out.print(a[i]);
            if (i != right) {
                System.out.print(",");
            }
        }
        System.out.println("}");
    }

    public int getSum(int[] a, int l, int r) {
        if (l == r) {
            return a[l];
        }

        int sum = 0;
        for (int i = l; i <= r; i++) {
            sum += a[i];
        }
        return sum;
    }


    //2.
//* 有一个字符串它的构成是词+空格的组合，如“苹果 梨 梨 苹果”
//要求输入一个匹配模式（简单的以字符来写）， 比如 aabb, 来判断该字符串是否符合该模式
//举个例子：
//1. pattern = "abba", str="苹果 梨 梨 桔子" 返回 ture
//2. pattern = "aabc", str="苹果 苹果 梨 梨" 返回 false
    public boolean checkMatch(String pattern, String str) {

        Map<String, Character> map = new HashMap<>();
        Map<Character, String> charMap = new HashMap<>();
        int p = 0, q = 0, pi = 0;
        while (q < str.length()) {
            if (str.charAt(q) == ' ' || q == str.length() - 1) {

                String word;
                if (q == str.length() - 1) {
                    word = str.substring(p);
                } else {
                    word = str.substring(p, q);
                }

                Character ch = map.get(word);
                Character targetCh = pattern.charAt(pi);
                // 如果当前对应字符为空，且目标字符未出现过，则加入
                if (ch == null && charMap.get(targetCh) == null) {

                    ch = targetCh;
                    map.put(word, targetCh);
                    charMap.put(targetCh, word);
                }

                if (ch == null || targetCh != ch) {
                    return false;
                }

                pi++;
                p = q + 1;
            }
            q++;
        }

        return true;
    }

}
