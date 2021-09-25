package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 连续子数组的最大和
 * <p>
 * 输入一个整型数组，数组里有正数也有负数。数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。要求时间复杂度为 O(n).
 *
 * @author bijl
 * @date 8/17/21
 */
public class FindGreatestSumOfSubArray42 {


    /**
     * 动态规划
     * <p>
     * 向后遍历时，保存前边的和，如果前边元素的和为正数则有效，否则从当前元素重新计算
     *
     * @param array
     * @return
     */
    public int FindGreatestSumOfSubArray(int[] array) {

        if (array == null || array.length == 0) {
            return 0;
        }

        int max = array[0];
        int sum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (sum > 0) {
                sum += array[i];
            } else {
                sum = array[i];
            }

            max = Math.max(max, sum);
        }

        return max;
    }
}
