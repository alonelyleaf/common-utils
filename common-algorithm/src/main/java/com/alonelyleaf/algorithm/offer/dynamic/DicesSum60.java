package com.alonelyleaf.algorithm.offer.dynamic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * n 个骰子的点数
 * <p>
 * 把 n 个骰子扔在地上，求点数和为 s 的概率。
 *
 * @author bijl
 * @date 8/18/21
 */
public class DicesSum60 {

    /**
     * 动态规划
     * 使用一个二维数组 dp 存储点数出现的次数，其中 dp[i][j] 表示前 i 个骰子产生点数 j 的次数。
     * <p>
     * 空间复杂度：O(N2)
     */
    public List<Map.Entry<Integer, Double>> dicesSum(int n) {
        int face = 6;
        int pointNum = face * n;
        long[][] dp = new long[n + 1][pointNum + 1];
        for (int i = 1; i <= face; i++) {
            dp[1][i] = 1;
        }

        // 逐个增加骰子
        for (int i = 2; i <= n; i++) {

            // 逐个计算可能出现的点数的此时
            for (int j = i; j <= pointNum; j++) {

                // 逐个计算当前骰子可能出现的点数
                for (int k = 1; k <= face; k++) {

                    // 当前j点出现的次数等于之前i-1个骰子出现 j-k 的次数 加上 当前骰子出现的次数
                    // k=1 时，之前 dp[i][j] =0,此时加上dp[i - 1][j - k]，当k变化时，逐一加上前边的值即可
                    dp[i][j] += dp[i - 1][j - k];
                }
            }
        }

        final double totalNum = Math.pow(6, n);
        List<Map.Entry<Integer, Double>> ret = new ArrayList<>();
        for (int i = n; i <= pointNum; i++) {
            ret.add(new AbstractMap.SimpleEntry<>(i, dp[n][i] / totalNum));
        }

        return ret;
    }
}
