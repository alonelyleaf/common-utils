package com.alonelyleaf.algorithm.offer.binarysearchanddivide;

/**
 * 旋转数组的最小数字
 * <p>
 * 把一个有序数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个非递减排序的数组的一个旋转，输出旋转数组的最小元素。
 *
 * @author bijl
 * @date 8/16/21
 */
public class MinNumberInRotateArray11 {

    public int minNumberInRotateArray(int[] nums) {

        if (nums == null || nums.length == 0) {
            return 0;
        }

        int l = 0, h = nums.length - 1;
        while (l < h) {
            int m = l + (h - l) / 2;

            //
            if (nums[l] == nums[m] && nums[m] == nums[h]) {
                return minNumber(nums, l, h);
            } else if (nums[m] <= nums[h]) {
                h = m;
            } else {
                l = m + 1;
            }
        }

        return nums[l];
    }

    private int minNumber(int[] nums, int l, int h) {
        for (int i = l; i < h; i++) {
            if (nums[i] > nums[i + 1]) {
                return nums[i + 1];
            }
        }
        return nums[l];
    }
}
