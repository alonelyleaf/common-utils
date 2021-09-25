package com.alonelyleaf.algorithm.offer.dynamic;

import java.util.Arrays;

/**
 * 跳台阶
 *
 * @author bijl
 * @date 8/17/21
 */
public class JumpFloor103 {

    /**
     * 跳台阶
     * <p>
     * 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     * <p>
     * n = 1时 s = 1
     * n = 2时 s = 2
     * n = 3时 s = f(1)+ f(2),即 f(n) = f(n-1) + f(n-2)
     *
     * @param n
     * @return
     */
    public int JumpFloor(int n) {
        if (n <= 2) {
            return n;
        }
        int pre2 = 1, pre1 = 2;
        int result = 0;
        for (int i = 2; i < n; i++) {
            result = pre2 + pre1;
            pre2 = pre1;
            pre1 = result;
        }
        return result;
    }

    /**
     * 变态跳台阶
     * <p>
     * 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级... 它也可以跳上 n 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     * <p>
     * 跳上 n-1 级台阶，可以从 n-2 级跳 1 级上去，也可以从 n-3 级跳 2 级上去...，那么 f(n-1) = f(n-2) + f(n-3) + ... + f(0)
     * 同样，跳上 n 级台阶，可以从 n-1 级跳 1 级上去，也可以从 n-2 级跳 2 级上去... ，那么 f(n) = f(n-1) + f(n-2) + ... + f(0)
     * 综上可得 f(n) - f(n-1) = f(n-1)
     * 即 f(n) = 2*f(n-1)
     *
     * @param target
     * @return
     */
    public int jumpFloorII(int target) {
        int[] dp = new int[target];
        Arrays.fill(dp, 1);
        for (int i = 1; i < target; i++) {
            for (int j = 0; j < i; j++) {
                dp[i] += dp[j];
            }
        }
        return dp[target - 1];
    }

    public int jumpFloorIII(int target) {
        if (target <= 1) {
            return target;
        }
        int result = 1;
        for (int i = 2; i <= target; i++) {
            result *= 2;
        }

        return result;
    }
}
