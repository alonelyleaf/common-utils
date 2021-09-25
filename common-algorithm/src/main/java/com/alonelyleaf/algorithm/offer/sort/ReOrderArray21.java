package com.alonelyleaf.algorithm.offer.sort;

/**
 * 调整数组顺序使奇数位于偶数前面
 * <p>
 * 需要保证奇数和奇数，偶数和偶数之间的相对位置不变，这和书本不太一样。例如对于 [1,2,3,4,5]，调整后得到 [1,3,5,2,4]，而不能是 {5,1,3,4,2} 这种相对位置改变的结果。
 *
 * @author bijl
 * @date 8/17/21
 */
public class ReOrderArray21 {


    /**
     * 额外建立数组保存最新结果
     * <p>
     * 创建一个新数组，时间复杂度 O(N)，空间复杂度 O(N)。
     *
     * @param array
     * @return
     */
    public int[] reOrderArray1(int[] array) {
        // 遍历计算统计奇数个数
        int oddCnt = 0;
        for (int n : array) {
            if (n % 2 == 1) {
                oddCnt++;
            }
        }

        // 创建结果数组
        int[] ret = new int[array.length];
        int i = 0, j = oddCnt;

        // 遍历原数组，并按照结果放到结果数组中
        for (int n : array) {
            if (n % 2 == 1) {
                ret[i++] = n;
            } else {
                ret[j++] = n;
            }
        }

        return ret;
    }


    /**
     * 使用冒泡思想，每次都将当前偶数上浮到当前最右边。时间复杂度 O(N2)，空间复杂度 O(1)，时间换空间。
     *
     * @param nums
     * @return
     */
    public int[] reOrderArray2(int[] nums) {
        int N = nums.length;
        for (int i = N - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (isEven(nums[j]) && !isEven(nums[j + 1])) {
                    swap(nums, j, j + 1);
                }
            }
        }
        return nums;
    }

    private boolean isEven(int x) {
        return x % 2 == 0;
    }

    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
