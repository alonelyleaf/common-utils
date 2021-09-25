package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 判断平衡二叉树
 * <p>
 * 输入一棵二叉树，判断该二叉树是否是平衡二叉树。
 * 在这里，我们只需要考虑其平衡性，不需要考虑其是不是排序二叉树
 * 平衡二叉树（Balanced Binary Tree），具有以下性质：它是一棵空树或它的左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。
 * 注：我们约定空树是平衡二叉树。
 * <p>
 * https://www.nowcoder.com/practice/8b3b95850edb4115918ecebdf1b4d222?tpId=13&tqId=11192&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking&from=cyc_github
 *
 * @author bijl
 * @date 8/16/21
 */
public class IsBalancedTree552 {

    private boolean isBalance = true;

    public boolean IsBalanced_Solution(TreeNode root) {
        height(root);
        return isBalance;
    }

    private int height(TreeNode root) {
        if (root == null || !isBalance) {
            return 0;
        }

        int left = height(root.left);
        int right = height(root.right);
        if (Math.abs(left - right) > 1) {
            isBalance = false;
        }

        return 1 + Math.max(left, right);
    }
}
