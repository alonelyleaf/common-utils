package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 矩阵的最小路径和
 * <p>
 * 给定一个矩阵 m，从左上角开始每次只能向右或者向下走，最后到达右下角的位置，路径 上所有的数字累加起来就是路径和，返回所有的路径中最小的路径和。
 * <p>
 * 如果给定的 m 如下:
 * 1 3 5 9
 * 8 1 3 4
 * 5 0 6 1
 * 8 8 4 0
 * 路径 1，3，1，0，6，1，0 是所有路径中路径和最小的，所以返回 12。
 *
 * @author bijl
 * @date 8/18/21
 */
public class MinPathSum {

    /**
     * 完整动态规划
     * 1.使用 dp[i][j]记录到达当前位置的最小路径和
     * 2.由于只能向右或向下，则 dp[i][j] = Min(dp[i-1][j], dp[i][j-1]) + array[i][j],即为从上或从左到达当前位置的最小值 加上 当前位置值
     * 3.对于左边缘和上边缘，需要单独设置值
     *
     * @param m
     * @return
     */
    public int minPathSum0(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        int row = m.length;
        int col = m[0].length;
        int[][] dp = new int[row][col];
        dp[0][0] = m[0][0];
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + m[i][0];
        }

        for (int j = 1; j < col; j++) {
            dp[0][j] = dp[0][j - 1] + m[0][j];
        }

        for (int i = 1; i < row; i++) {

            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
            }
        }

        return dp[row][col];
    }

    /**
     * 动态规划
     * <p>
     * 每次只保留最新一层结果，则可以将解数组退化为一维。每层计算时，将结果更新到解数组中，即将解数组中的内容更新为当前层
     *
     * @param array
     * @return
     */
    public int minPxathSum(int[][] array) {

        if (array == null || array.length == 0 || array[0].length == 0) {
            return 0;
        }

        int cols = array[0].length;
        int[] dp = new int[cols];
        for (int[] value : array) {

            dp[0] += value[0];
            for (int j = 1; j < cols; j++) {
                dp[j] = Math.min(dp[j], dp[j - 1]) + value[j];
            }
        }

        return dp[cols - 1];
    }


}
