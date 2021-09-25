package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 二叉树的镜像
 * <p>
 * 操作给定的二叉树，将其变换为源二叉树的镜像。即左右节点交换
 *
 * @author bijl
 * @date 8/16/21
 */
public class Mirror27 {

    public TreeNode Mirror(TreeNode root) {
        if (root == null) {
            return root;
        }
        swap(root);
        Mirror(root.left);
        Mirror(root.right);
        return root;
    }

    private void swap(TreeNode root) {
        TreeNode t = root.left;
        root.left = root.right;
        root.right = t;
    }
}
