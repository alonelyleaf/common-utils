package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 换钱的方法数
 *
 * @author bijl
 * @date 8/19/21
 */
public class Coins {


    /**
     * 给定数组 arr，arr 中所有的值都为正数且不重复。每个值代表一种面值的货币，每种面值 的货币可以使用任意张，再给定一个整数 aim，代表要找的钱数，求换钱有多少种方法。
     */
    public int coins(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {

            return 0;
        }

        return process1(arr, 0, aim);
    }

    /**
     * 暴力递归
     * <p>
     * 1.每次计算当前货币使用张数后，在加上后续货币的使用方式数
     * 2.会存在大量的计算
     *
     * @param arr
     * @param index
     * @param aim
     * @return
     */
    public int process1(int[] arr, int index, int aim) {
        int res = 0;
        if (index == arr.length) {
            res = aim == 0 ? 1 : 0;
        } else {
            for (int i = 0; arr[i] * i <= aim; i++) {
                res += process1(arr, index + 1, aim - arr[i] * i);
            }
        }

        return res;
    }

    /**
     * 动态规划
     *
     * @param arr
     * @param aim
     * @return
     */
    public int coins2(int[] arr, int aim) {

        int[][] dp = new int[arr.length][aim + 1];

        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }

        for (int j = 1; arr[j] * j <= aim; j++) {

            dp[0][arr[0] * j] = 1;
        }

        int num = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                num = 0;
                for (int k = 0; j - arr[i] * k >= 0; k++) {
                    num += dp[i - 1][j - arr[i] * k];
                }

                dp[i][j] = num;
            }
        }

        return dp[arr.length - 1][aim];
    }

    //------------------------------------------ 求货币使用最少张数 -------------------------------------------------

    /**
     * 给定数组 arr，arr 中所有的值都为正数且不重复。每个值代表一种面值的货币，每种面值 的货币可以使用任意张，再给定一个整数 aim，代表要找的钱数，求组成 aim 的最少货币数。
     *
     * @param arr
     * @param aim
     * @return
     */
    public int minCoins(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return -1;
        }

        return process(arr, 0, aim);
    }


    /**
     * 暴力递归
     * <p>
     * 1.每次确定当前位置货币数后，向后查找后续的货币数，相加获取总货币数
     * 2.到达末尾时，如果剩余金额不为0，则说明无法凑整
     *
     * @param arr
     * @param index
     * @param rest
     * @return
     */
    public int process(int[] arr, int index, int rest) {
        if (index == arr.length) {
            return rest == 0 ? 0 : -1;
        }
        int res = -1;
        for (int k = 0; arr[index] * k <= rest; k++) {
            int next = process(arr, index + 1, rest - arr[index] * k);
            if (next != -1) {
                res = res == -1 ? next + k : Math.min(res, next + k);
            }
        }

        return res;
    }

    public int minCoins2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return -1;
        }

        int[][] dp = new int[arr.length][aim + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }

        for (int j = 0; arr[0] * j <= aim; j++) {
            if (arr[0] * j == aim) {
                dp[0][j] = 1;
            }
        }

        return dp[arr.length - 1][aim];
    }

}
