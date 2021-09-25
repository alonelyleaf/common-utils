package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 二叉树的下一个结点
 * <p>
 * 给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回 。注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 *
 * @author bijl
 * @date 8/16/21
 */
public class GetNext8 {

    public class TreeLinkNode {

        int val;
        TreeLinkNode left = null;
        TreeLinkNode right = null;

        // 指向父结点的指针
        TreeLinkNode next = null;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }

    /**
     * ① 如果一个节点的右子树不为空，那么该节点的下一个节点是右子树的最左节点；
     * ② 否则，向上找第一个左链接指向的树包含该节点的祖先节点。
     *
     * @param pNode
     * @return
     */
    public TreeLinkNode GetNext(TreeLinkNode pNode) {

        if (pNode.right != null) {
            TreeLinkNode treeLinkNode = pNode.right;
            while (treeLinkNode.left != null) {
                treeLinkNode = treeLinkNode.left;
            }
            return treeLinkNode;
        } else {
            while (pNode.next != null) {
                TreeLinkNode parent = pNode.next;

                // 如果当前节点为父节点的左子节点，则下一遍历节点为父节点，否则继续向上遍历
                if (parent.left == pNode) {
                    return parent;
                }
                pNode = pNode.next;
            }
        }

        return null;
    }
}
