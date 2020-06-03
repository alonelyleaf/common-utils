package com.alonelyleaf.algorithm.tree.exercise;

/**
 * 如何获取一组有序数列可构建的二叉搜索树的数量
 *
 * @author bijl
 * @date 2020/6/10
 */
public class BinaryTreeNumber {

    /**
     *
     * 基于动态规划思想，实现算法，步骤如下：
     *
     * 1. 声明一个动态规划数组 dp，长度为 n+1（序列长度为 n），第 i 个元素代表使用 i 个有序数字可以构建的二叉搜索树的数量，初始 dp[0] = 1, dp[1] = 1；
     *
     * 2. 对于 n 个有序数字（n>=2），其可构建的二叉搜索树数量如何计算？可让每个数字作为根元素，构建的所有二叉搜索树的和即可。
     *
     * 3. 对于有序数列的第 i 个数字，其作为根元素可构建的二叉搜索树的数量为：前 i-1 个元素构建的左子树数量 * 后 n-i 个元素构建的右子树数量，即 dp[i-1] * dp[n-i]。
     *
     * 算法复杂度分析：
     *
     * 算法需要嵌套遍历循环，因此时间复杂度为 O(n*n)；需要创建一个长度为 n 的动态规划数组辅助运算，因此空间复杂度为 O(n)。
     */


    public int nums(int n){

        if (n<=1){
            return n;
        }

        int[] dp = new int[n+1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++){
            for (int j = 1; j <= i;j++){
                dp[i] += dp[j-1] * dp[i-j]; //左子树数量 * 右子树数量
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {

        BinaryTreeNumber binaryTreeNumber = new BinaryTreeNumber();

        System.out.println(binaryTreeNumber.nums(3));
    }
}
