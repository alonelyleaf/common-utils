package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 二叉搜索树的后序遍历序列
 * <p>
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。假设输入的数组的任意两个数字都互不相同。
 *
 * @author bijl
 * @date 8/16/21
 */
public class VerifySquenceOfBST33 {

    public boolean VerifySquenceOfBST(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return false;
        }

        return verifySubTree(sequence, 0, sequence.length - 1);
    }

    private boolean verifySubTree(int[] sequence, int from, int to) {
        if (to - from <= 1) {
            return true;
        }

        // 后序遍历，根节点在最后
        int root = sequence[to];
        int cutIndex = from;

        // 向后遍历，找到右子树的起始节点
        while (cutIndex < to && sequence[cutIndex] < root) {
            cutIndex++;
        }

        // 从右子树起始节点开始，验证元素是否都大于根节点
        for (int i = cutIndex; i < to; i++) {
            if (sequence[i] < root) {
                return false;
            }
        }

        // 继续向下验证左右子树
        return verifySubTree(sequence, from, cutIndex - 1) && verifySubTree(sequence, cutIndex, to - 1);
    }
}
