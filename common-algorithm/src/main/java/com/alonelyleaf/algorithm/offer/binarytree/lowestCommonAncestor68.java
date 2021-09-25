package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 树中两个节点的最低公共祖先
 * <p>
 * http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/68.%20树中两个节点的最低公共祖先.html#_68-1-二叉查找树
 *
 * @author bijl
 * @date 8/16/21
 */
public class lowestCommonAncestor68 {


    /**
     * 二叉查找树
     * <p>
     * 在二叉查找树中，两个节点 p, q 的公共祖先 root 满足 root.val >= p.val && root.val <= q.val。
     * 如果p,q 值在根节点的同一侧，则可以去子树中寻找最近祖先节点。
     *
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return root;
        }

        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor(root.left, p, q);
        }

        if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor(root.right, p, q);
        }
        return root;
    }

    /**
     * 普通二叉树
     * <p>
     * 在左右子树中查找是否存在 p 或者 q，如果 p 和 q 分别在两个子树中，那么就说明根节点就是最低公共祖先。
     *
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {

        // 到达空节点，或目标节点，则返回
        if (root == null || root == p || root == q) {
            return root;
        }

        // 在子树中寻找目标节点
        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);

        // 如果左子树中不存在，则返回右子树
        // 如果左子树中存在，但右子树中不存在，则返回左子树的节点
        // 如果都不存在，则为null
        // 如果都分别在两个子树，则当前根节点即为最近公共父节点

        // 相当于将目标节点向上移动，不论是目标节点还是公共根
        return (left == null) ? right : (right == null ? left : root);
    }

}
