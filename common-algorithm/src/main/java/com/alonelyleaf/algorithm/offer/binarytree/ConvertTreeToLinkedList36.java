package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 二叉搜索树与双向链表
 * <p>
 * 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。
 * <p>
 * 注意:
 * 1.要求不能创建任何新的结点，只能调整树中结点指针的指向。当转化完成以后，树中节点的左指针需要指向前驱，树中节点的右指针需要指向后继
 * 2.返回链表中的第一个节点的指针
 * 3.函数返回的TreeNode，有左右指针，其实可以看成一个双向链表的数据结构
 *
 * @author bijl
 * @date 8/16/21
 */
public class ConvertTreeToLinkedList36 {


    private TreeNode pre = null;
    private TreeNode head = null;


    /**
     * 通过额外变量记录前驱节点及头节点，
     * 中序遍历二叉树，
     * 对于当前节点，根据前驱节点设置当前节点的前驱节点及前驱节点的后置节点
     *
     * @param pRootOfTree
     * @return
     */
    public TreeNode Convert(TreeNode pRootOfTree) {
        inOrder(pRootOfTree);
        return head;
    }

    public void inOrder(TreeNode node) {
        if (node == null) {
            return;
        }

        inOrder(node.left);

        node.left = pre;
        if (pre != null) {
            pre.right = node;
        }
        pre = node;
        if (head == null) {
            head = node;
        }

        inOrder(node.right);
    }
}
