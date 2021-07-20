package com.alonelyleaf.algorithm.offer.doublepoint;

import java.util.ArrayList;

/**
 * 和为 S 的连续正数序列
 *
 * http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/57.2%20和为%20S%20的连续正数序列.html#题目描述
 *
 * 从最小值开始穷举，如果比目标值大，则左边减少值，如果比目标值大，则右边增加值。如果等于目标值，则构建结果并添加，然后向右移动，继续寻找
 *
 * @author bijl
 * @date 7/13/21
 */
public class FindContinuousSequence572 {

    public ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();

        int start = 1, end = 2;
        int curSum = 3;
        while (end < sum) {
            if (curSum > sum) {
                curSum -= start;
                start++;
            } else if (curSum < sum) {
                end++;
                curSum += end;
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = start; i <= end; i++) {
                    list.add(i);
                }
                ret.add(list);
                curSum -= start;
                start++;
                end++;
                curSum += end;
            }
        }

        return ret;
    }
}
