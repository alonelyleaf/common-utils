package com.alonelyleaf.algorithm.offer.dynamic;

/**
 * 斐波那契数列
 * <p>
 * 求斐波那契数列的第 n 项，n <= 39。
 * <p>
 * f(n) = 0 n = 0
 * f(n) = 1 n = 1
 * f(n) = f(n-1) + f(n-1) n > 1
 *
 * @author bijl
 * @date 8/17/21
 */
public class Fibonacci101 {

    /**
     * 递归实现
     * <p>
     * 但是递归的问题是会重复计算.
     *
     * @param n
     * @return
     */
    public int Fibonacci2(int n) {
        if (n <= 1) {
            return n;
        }

        return Fibonacci2(n - 1) + Fibonacci2(n - 2);
    }


    /**
     * 动态规划是将一个问题划分成多个子问题求解，会把子问题的解缓存起来，从而避免重复求解子问题。
     *
     * @param n
     * @return
     */
    public int Fibonacci0(int n) {
        if (n <= 1) {
            return n;
        }
        int[] fib = new int[n + 1];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }

        return fib[n];
    }

    /**
     * 考虑到第 i 项只与第 i-1 和第 i-2 项有关，因此只需要存储前两项的值就能求解第 i 项，从而将空间复杂度由 O(N) 降低为 O(1)。
     *
     * @param n
     * @return
     */
    public int Fibonacci1(int n) {
        if (n <= 1) {
            return n;
        }

        int pre1 = 0, pre2 = 1;
        int fib = 0;
        for (int i = 2; i <= n; i++) {
            fib = pre1 + pre2;
            pre1 = pre2;
            pre2 = fib;
        }

        return fib;
    }


}
