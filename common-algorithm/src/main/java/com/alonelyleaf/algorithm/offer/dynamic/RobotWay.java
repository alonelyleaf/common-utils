package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 机器人达到指定位置方法数
 * <p>
 * 暴力方式到动态规划的优化，详见程序员代码面试指南
 * <p>
 * 套路大体步骤如下。
 * 前提:你的尝试过程是无后效性的。
 * 1)找到什么可变参数可以代表一个递归状态，也就是哪些参数一旦确定，返回值就确定了。
 * 2)把可变参数的所有组合映射成一张表，有 1 个可变参数就是一维表，2 个可变参数就是二维表......
 * 3)最终答案要的是表中的哪个位置，在表中标出。
 * 4)根据递归过程的 base case，把这张表最简单、不需要依赖其他位置的那些位置填好值。
 * 5)根据递归过程非base case的部分，也就是分析表中的普遍位置需要怎么计算得到，那
 * 么这张表的填写顺序也就确定了。
 * 6)填好表，返回最终答案在表中位置的值。
 * <p>
 * 题目描述
 * <p>
 * 假设有排成一行的 N 个位置，记为 1~N，N 一定大于或等于 2。开始时机器人在其中的 M 位置上(M 一定是 1~N 中的一个)，机器人可以往左走或者往右走，
 * 如果机器人来到 1 位置， 那么下一步只能往右来到 2 位置;如果机器人来到 N 位置，那么下一步只能往左来到 N-1 位置。
 * 规定机器人必须走 K 步，最终能来到 P 位置(P 也一定是 1~N 中的一个)的方法有多少种。给 定四个参数 N、M、K、P，返回方法数。
 * <p>
 * 例如：N=5,M=2,K=3,P=3
 * 上面的参数代表所有位置为 1 2 3 4 5。机器人最开始在 2 位置上，必须经过 3 步，最后到
 * 达 3 位置。走的方法只有如下 3 种: 1)从2到1，从1到2，从2到3 2)从2到3，从3到2，从2到3 3)从2到3，从3到4，从4到3
 * 所以返回方法数 3。
 * <p>
 * N=3,M=1,K=3,P=3
 * 上面的参数代表所有位置为 1 2 3。机器人最开始在 1 位置上，必须经过 3 步，最后到达 3 位置。怎么走也不可能，所以返回方法数 0。
 *
 * @author bijl
 * @date 8/18/21
 */
public class RobotWay {


    /**
     * 暴力递归，检查所有可能的行动路径
     *
     * @param P
     * @return
     */
    public int ways1(int N, int M, int K, int P) {
        // 参数无效直接返回 0
        if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
            return 0;
        }
        // 总共 N 个位置，从 M 点出发，还剩 K 步，返回最终能达到 P 的方法数
        return walk(N, M, K, P);
    }

    /**
     * 只能在 1~N 这些位置上移动，当前在 cur 位置，走完 rest 步之后，停在 P 位置的方法数作为返回值返回
     *
     * @param N    : 位置为 1 ~ N，固定参数
     * @param cur  : 当前在 cur 位置，可变参数
     * @param rest : 还剩 res 步没有走，可变参数
     * @param P    : 最终目标位置是 P，固定参数
     * @return
     */
    public int walk(int N, int cur, int rest, int P) {

        // 如果没有剩余步数了，当前的 cur 位置就是最后的位置
        // 如果最后的位置停在 P 上，那么之前做的移动是有效的
        // 如果最后的位置没在 P 上，那么之前做的移动是无效的
        if (rest == 0) {
            return cur == P ? 1 : 0;
        }

        // 如果还有 rest 步要走，而当前的 cur 位置在 1 位置上，那么当前这步只能从 1 走向 2
        // 后续的过程就是来到 2 位置上，还剩 rest-1 步要走
        if (cur == 1) {
            return walk(N, 2, rest - 1, P);
        }

        // 如果还有 rest 步要走，而当前的 cur 位置在 N 位置上，那么当前这步只能从 N 走向 N-1
        // 后续的过程就是来到 N-1 位置上，还剩 rest-1 步要走
        if (cur == N) {
            return walk(N, N - 1, rest - 1, P);
        }

        // 如果还有 rest 步要走，而当前的 cur 位置在中间位置上，那么可以走向左，也可以走向右
        // 走向左之后，后续的过程就是，来到 cur-1 位置上，还剩 rest-1 步要走
        // 走向右之后，后续的过程就是，来到 cur+1 位置上，还剩 rest-1 步要走
        // 走向左、走向右是截然不同的方法，所以总方法数都要算上
        return walk(N, cur - 1, rest - 1, P) + walk(N, cur + 1, rest - 1, P);
    }

    /**
     * 将暴力优化为动态规划
     *
     * @return
     */
    public int ways2(int N, int M, int K, int P) {
        // 参数无效直接返回 0
        if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
            return 0;
        }
        int[][] dp = new int[K + 1][N + 1];
        dp[0][P] = 1;
        for (int i = 1; i <= K; i++) {
            for (int j = 1; j <= N; j++) {
                if (j == 1) {
                    dp[i][j] = dp[i - 1][2];
                } else if (j == N) {
                    dp[i][j] = dp[i - 1][N - 1];
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j + 1];
                }
            }
        }

        return dp[K][M];
    }


    /**
     * 动态规划+空间压缩的解法
     *
     * @param N
     * @param M
     * @param K
     * @param P
     * @return
     */
    public int ways3(int N, int M, int K, int P) { // 参数无效直接返回 0
        if (N < 2 || K < 1 || M < 1 || M > N || P < 1 || P > N) {
            return 0;
        }

        int[] dp = new int[N + 1];
        dp[P] = 1;
        for (int i = 1; i <= K; i++) {

            //上层左上角的值，因为只保存一层，左上角的值会被前边计算覆盖，需要单独变量存储
            int leftUp = dp[1];
            for (int j = 1; j < N; j++) {

                // 上一层当前位置的值
                int tmp = dp[j];

                //如果在首位，则值为上层位置2的值
                if (j == 1) {
                    dp[j] = dp[2];
                } else if (j == N) {
                    dp[j] = leftUp;
                } else {
                    dp[j] = leftUp + dp[j + 1];
                }

                leftUp = tmp;
            }
        }

        return dp[N];
    }
}
