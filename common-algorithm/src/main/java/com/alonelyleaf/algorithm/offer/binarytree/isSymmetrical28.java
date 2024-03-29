package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 对称的二叉树
 * <p>
 * 请实现一个函数，用来判断一棵二叉树是不是对称的。注意，如果一个二叉树同此二叉树的镜像是同样的，定义其为对称的。
 * <p>
 * https://www.nowcoder.com/practice/ff05d44dfdb04e1d83bdbdab320efbcb?tpId=13&tqId=11211&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking&from=cyc_github
 *
 * @author bijl
 * @date 8/16/21
 */
public class isSymmetrical28 {

    boolean isSymmetrical(TreeNode pRoot) {
        if (pRoot == null) {
            return true;
        }
        return isSymmetrical(pRoot.left, pRoot.right);
    }

    /**
     * 对比两个子树是否对称
     *
     * @param t1
     * @param t2
     * @return
     */
    boolean isSymmetrical(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 == null || t2 == null) {
            return false;
        }
        if (t1.val != t2.val) {
            return false;
        }

        // 继续对比两个子树的节点，注意对称是树1的左节点与树2的右节点
        return isSymmetrical(t1.left, t2.right) && isSymmetrical(t1.right, t2.left);
    }
}
