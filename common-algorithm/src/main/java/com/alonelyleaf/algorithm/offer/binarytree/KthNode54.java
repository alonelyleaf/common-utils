package com.alonelyleaf.algorithm.offer.binarytree;

import java.util.Arrays;
import java.util.List;

/**
 * 二叉搜索树的第k个结点
 * <p>
 * 给定一棵二叉搜索树，请找出其中的第k小的TreeNode结点。
 *
 * @author bijl
 * @date 8/16/21
 */
public class KthNode54 {

    private int num;
    private TreeNode result;

    TreeNode KthNode(TreeNode pRoot, int k) {
        num = k;
        inOrder(pRoot);
        return result;
    }

    void inOrder(TreeNode pRoot) {
        if (pRoot == null || num == 0) {
            return;
        }

        inOrder(pRoot.left);
        num--;
        if (num == 0) {
            result = pRoot;
        }
        inOrder(pRoot.right);
    }

    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(5, 3, 7, 2, 4, 6, 8);
    }
}
