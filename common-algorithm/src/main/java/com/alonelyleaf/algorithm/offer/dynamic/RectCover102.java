package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 矩形覆盖
 * <p>
 * 我们可以用 2*1 的小矩形横着或者竖着去覆盖更大的矩形。请问用 n 个 2*1 的小矩形无重叠地覆盖一个 2*n 的大矩形，总共有多少种方法？
 * <p>
 * http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/10.2%20矩形覆盖.html#题目链接
 *
 * @author bijl
 * @date 8/17/21
 */
public class RectCover102 {

    /**
     * n = 1时 s = 1
     * n = 2时 s = 2
     * n = 3时 s = f(1)+ f(2),即 f(n) = f(n-1) + f(n-2)
     *
     * @param n
     * @return
     */
    public int rectCover(int n) {

        if (n <= 1) {
            return n;
        }

        int pre1 = 0, pre2 = 1;
        int result = 0;
        for (int i = 2; i <= n; i++) {

            result = pre1 + pre2;
            pre1 = pre2;
            pre2 = result;
        }

        return result;
    }
}
