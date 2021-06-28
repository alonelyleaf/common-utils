package com.alonelyleaf.algorithm.offer;

/**
 * 二维数组中的查找
 *
 * @author bijl
 * @date 6/28/21
 */
public class TwoDimensionalArraySearch4 {

    /**
     * Consider the following array:
     * [
     * [1,   4,  7, 11, 15],
     * [2,   5,  8, 12, 19],
     * [3,   6,  9, 16, 22],
     * [10, 13, 14, 17, 24],
     * [18, 21, 23, 26, 30]
     * ]
     * <p>
     * Given target = 5, return true.
     * Given target = 20, return false.
     * <p>
     * 要求时间复杂度 O(M + N)，空间复杂度 O(1)。其中 M 为行数，N 为 列数。
     * <p>
     * 该二维数组中的一个数，小于它的数一定在其左边，大于它的数一定在其下边。因此，从右上角开始查找，就可以根据 target 和当前元素的大小关系来快速地缩小查找区间，
     * 每次减少一行或者一列的元素。当前元素的查找区间为左下角的所有元素。
     *
     * @param target
     * @param array
     * @return
     * @link http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/4.%20二维数组中的查找.html#解题思路
     */
    public boolean find(int target, int[][] array) {

        if (array == null || array.length == 0 || array[0].length == 0) {
            return false;
        }

        int rows = array.length, cols = array[0].length;
        // 从右上角开始
        int r = 0, c = cols - 1;
        while (c >= 0 && r < rows) {
            if (target == array[r][c]) {
                return true;
            }

            if (target > array[r][c]) {
                r++;
            } else {
                c--;
            }
        }

        return false;
    }

}
