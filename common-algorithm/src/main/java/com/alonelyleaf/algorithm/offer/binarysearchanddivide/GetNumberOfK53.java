package com.alonelyleaf.algorithm.offer.binarysearchanddivide;

/**
 * 数字在排序数组中出现的次数
 *
 * @author bijl
 * @date 8/16/21
 */
public class GetNumberOfK53 {

    /**
     * 正常二分查找
     *
     * @param nums
     * @param K
     * @return
     */
    public int binarySearch0(int[] nums, int K) {
        int l = 0, h = nums.length - 1;
        while (l <= h) {
            int m = l + (h - l) / 2;
            if (nums[m] == K) {
                return m;
            } else if (nums[m] < K) {
                l = m + 1;
            } else {
                h = m - 1;
            }
        }
        return -1;
    }

    public int binarySearch(int[] nums, int K) {
        int l = 0, h = nums.length;
        while (l < h) {
            int m = l + (h - l) / 2;
            if (nums[m] >= K) {
                h = m;
            } else {
                l = m + 1;
            }
        }

        return l;
    }


    public int GetNumberOfK(int[] nums, int K) {

        int first = binarySearch(nums, K);
        int last = binarySearch(nums, K + 1);

        return (first == nums.length || nums[first] != K) ? 0 : last - first;
    }

    public static void main(String[] args) {

    }
}
