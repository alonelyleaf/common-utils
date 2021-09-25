package com.alonelyleaf.algorithm.offer.binarytree;

/**
 * 序列化二叉树
 * <p>
 * 请实现两个函数，分别用来序列化和反序列化二叉树。
 * <p>
 * https://www.nowcoder.com/practice/cf7e25aa97c04cc1a68c8f040e71fb84?tpId=13&tqId=11214&tPage=1&rp=1&ru=/ta/coding-interviews&qru=/ta/coding-interviews/question-ranking&from=cyc_github
 *
 * @author bijl
 * @date 8/16/21
 */
public class SerializeTree37 {

    private String deserializeStr;

    public String serialize(TreeNode root) {

        if (root == null) {
            return "#";
        }

        return root.val + " " + serialize(root.left) + " " + serialize(root.right);
    }

    public TreeNode deserialize(String str) {

        deserializeStr = str;
        return deserialize();
    }

    public TreeNode deserialize() {

        if (deserializeStr.length() == 0) {
            return null;
        }
        int index = deserializeStr.indexOf(" ");
        String val = index == -1 ? deserializeStr : deserializeStr.substring(0, index);
        deserializeStr = index == -1 ? "" : deserializeStr.substring(index + 1);

        if ("#".equals(val)) {
            return null;
        }

        TreeNode treeNode = new TreeNode(Integer.valueOf(val));
        treeNode.left = deserialize();
        treeNode.right = deserialize();
        return treeNode;
    }
}
