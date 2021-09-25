package com.alonelyleaf.algorithm.offer.greedy;

/**
 * 股票的最大利润
 * <p>
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * <p>
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/description/?utm_source=LCUS&utm_medium=ip_redirect&utm_campaign=transfer2china
 *
 * @author bijl
 * @date 8/16/21
 */
public class MaxProfit63 {

    /**
     * 使用贪心策略，假设第 i 轮进行卖出操作，买入操作价格应该在 i 之前并且价格最低。因此在遍历数组时记录当前最低的买入价格，
     * 并且尝试将每个位置都作为卖出价格，取收益最大的即可。
     *
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {

        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int maxProfit = 0;
        int minPrice = prices[0];
        for (int i = 1; i < prices.length; i++) {
            minPrice = Math.min(minPrice, prices[i]);
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }

        return maxProfit;
    }

}
