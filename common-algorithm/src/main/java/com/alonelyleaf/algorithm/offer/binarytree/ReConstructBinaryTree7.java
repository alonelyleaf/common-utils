package com.alonelyleaf.algorithm.offer.binarytree;

import java.util.HashMap;
import java.util.Map;

/**
 * 重建二叉树
 * <p>
 * https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6?tpId=13&tqId=11157&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking&from=cyc_github
 *
 * @author bijl
 * @date 8/16/21
 */
public class ReConstructBinaryTree7 {

    private Map<Integer, Integer> indexForInOrders = new HashMap<>();

    /**
     * 前序遍历的第一个值为根节点的值，使用这个值将中序遍历结果分成两部分，左部分为树的左子树中序遍历结果，右部分为树的右子树中序遍历的结果。然后分别对左右子树递归地求解。
     * <p>
     * 输入：
     * [1,2,4,7,3,5,6,8],[4,7,2,1,5,3,8,6]
     */
    public TreeNode reConstructBinaryTree(int[] pre, int[] vin) {

        for (int i = 0; i < vin.length; i++) {
            indexForInOrders.put(vin[i], i);
        }

        return reConstructBinaryTree(pre, 0, pre.length, 0);
    }

    public TreeNode reConstructBinaryTree(int[] pre, int preL, int preR, int vinL) {
        if (preL > preR) {
            return null;
        }

        TreeNode root = new TreeNode(pre[preL]);
        int inIndex = indexForInOrders.get(root.val);
        int leftTreeSize = inIndex - vinL;
        root.left = reConstructBinaryTree(pre, preL + 1, preL + leftTreeSize, vinL);
        root.right = reConstructBinaryTree(pre, preL + leftTreeSize + 1, preR, vinL + leftTreeSize + 1);
        return root;
    }
}
