package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 最长递增子序列
 * <p>
 * 给定数组 arr，返回 arr 的最长递增子序列。 【举例】
 * + dp[i + 1][R]);
 *
 * @author bijl
 * @date 8/19/21
 */
public class LongestSubArray {

    public int[] longestSubArray(int[] arr) {
        int[] dp = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {

            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                // 如果当前位置元素比前一元素大，则判断
                if (arr[i] > arr[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        return dp;
    }

}
