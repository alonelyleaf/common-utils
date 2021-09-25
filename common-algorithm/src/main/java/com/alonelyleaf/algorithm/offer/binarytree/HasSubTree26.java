package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 树的子结构
 *
 * @author bijl
 * @date 8/16/21
 */
public class HasSubTree26 {

    public boolean hasSubtree(TreeNode root1, TreeNode root2) {

        if (root1 == null || root2 == null) {

            return false;
        }

        // 完整树，左子树，右子树
        return isSubtreeWithRoot(root1, root2) || hasSubtree(root1.left, root2) || hasSubtree(root1.right, root2);
    }

    /**
     * 判断两个树是否相同
     *
     * @param root1
     * @param root2
     * @return
     */
    public boolean isSubtreeWithRoot(TreeNode root1, TreeNode root2) {

        if (root2 == null) {
            return true;
        }

        if (root1 == null) {
            return false;
        }

        if (root1.val != root2.val) {
            return false;
        }

        return isSubtreeWithRoot(root1.left, root2.left) && isSubtreeWithRoot(root1.right, root2.right);
    }
}
