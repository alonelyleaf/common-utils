package com.alonelyleaf.algorithm.offer.doublepoint;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 和为 S 的两个数字
 * <p>
 * http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/57.1%20和为%20S%20的两个数字.html#题目描述
 *
 * @author bijl
 * @date 7/13/21
 */
public class FindNumbersWithSum571 {

    public ArrayList<Integer> FindNumbersWithSum(int[] array, int sum) {
        int p = 0, q = array.length - 1;
        while (p < q) {
            int cur = array[p] + array[q];
            if (cur == sum) {
                return new ArrayList<>(Arrays.asList(array[p], array[q]));
            }

            if (cur < sum) {
                p++;
            } else {
                q--;
            }
        }

        return new ArrayList<>();
    }

}
