package com.alonelyleaf.algorithm.tree.category;

/**
 * https://www.cnblogs.com/skywang12345/p/3576452.html
 *
 * https://blog.csdn.net/isea533/article/details/80345507
 *
 * 二叉排序树，也叫二叉搜索树（Binary Search Tree）
 * 1. 若任意节点的左子树不空，则左子树上所有结点的值均小于它的根结点的值；
 * 2. 任意节点的右子树不空，则右子树上所有结点的值均大于它的根结点的值；
 * 3. 任意节点的左、右子树也分别为二叉查找树。
 * 4. 没有键值相等的节点（no duplicate nodes）。
 *
 * @author bijl
 * @date 2019/5/30
 */
public class BinarySortTree<T extends Comparable<T>> {

    private BSTNode<T> rootNode;    // 根结点

    public BinarySortTree() {
        this.rootNode = null;
    }

    public BinarySortTree(BSTNode<T> rootNode) {
        this.rootNode = rootNode;
    }

    public BSTNode<T> getRootNode() {
        return rootNode;
    }

    public BinarySortTree<T> setRootNode(BSTNode<T> rootNode) {
        this.rootNode = rootNode;
        return this;
    }

    /*
     * 前序遍历"二叉树"
     */
    private void preOrder(BSTNode<T> tree) {
        if (tree != null) {
            System.out.print(tree.key + " ");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void preOrder() {
        preOrder(rootNode);
    }

    /*
     * 中序遍历"二叉树"
     */
    private void inOrder(BSTNode<T> tree) {
        if (tree != null) {
            inOrder(tree.left);
            System.out.print(tree.key + " ");
            inOrder(tree.right);
        }
    }

    public void inOrder() {
        inOrder(rootNode);
    }


    /*
     * 后序遍历"二叉树"
     */
    private void postOrder(BSTNode<T> tree) {
        if (tree != null) {
            postOrder(tree.left);
            postOrder(tree.right);
            System.out.print(tree.key + " ");
        }
    }

    public void postOrder() {
        postOrder(rootNode);
    }

    /*
     * 将结点插入到二叉树中
     *
     * 参数说明：
     *     tree 二叉树的
     *     z 插入的结点
     *
     * 实现：
     *  1. 从根节点向下找到该节点的父节点
     *  2. 添加节点，新加节点必然作为叶子节点
     */
    private void insert(BinarySortTree<T> bst, BSTNode<T> z) {
        int cmp;
        BSTNode<T> y = null;
        BSTNode<T> x = bst.rootNode;

        // 查找z的插入位置
        while (x != null) {
            y = x;
            cmp = z.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        z.parent = y;
        if (y == null) {
            bst.rootNode = z;
        } else { //与父节点比较，作为左子节点还是右子节点
            cmp = z.key.compareTo(y.key);
            if (cmp < 0) {
                y.left = z;
            } else {
                y.right = z;
            }
        }
    }

    /*
     * 新建结点(key)，并将其插入到二叉树中
     *
     * 参数说明：
     *     tree 二叉树的根结点
     *     key 插入结点的键值
     */
    public void insert(T key) {

        BSTNode<T> z = new BSTNode<T>(key, null, null, null);
        insert(this, z);
    }

    /*
     * 删除结点(z)，并返回被删除的结点
     *
     * 参数说明：
     *     bst 二叉树
     *     z 删除的结点
     *
     * 实现：
     * 1. 同时拥有左右子节点时，删除该节点的后继节点（修改其子节点和父节点的指向），并将此后继节点的值设置该节点
     * 2. 只有一个节点或者没有节点时，删除此节点，修改其子节点和父节点的指向
     */
    private BSTNode<T> remove(BinarySortTree<T> bst, BSTNode<T> z) {

        //将被删除节点的子节点child
        BSTNode<T> x = null;

        //要删除的节点 delete
        BSTNode<T> y = null;

        //只有一个节点或者没有节点时
        if ((z.left == null) || (z.right == null)){
            //z 就是要删除的节点
            y = z;
        } else{
            //当有两个子节点时，删除后继结点
            y = successor(z);
        }

        //获取子节点，不管是左是右
        if (y.left != null){
            x = y.left;
        } else{
            x = y.right;
        }

        //如果存在子节点，就关联被删节点的父节点
        if (x != null){
            x.parent = y.parent;
        }

        //如果父节点是空，说明要删的是根节点
        if (y.parent == null){
            //设置根为 child（此时根只有一个或没有节点）
            bst.rootNode = x;

        } else if (y == y.parent.left) {//要删的是左节点
            y.parent.left = x;//左节点关联子节点

        }else {//要删的是右节点
            y.parent.right = x;//右节点关联子节点
        }//如果要删的节点和一开始传入的不一样，就是后继的情况

        if (y != z)
            z.key = y.key;//后继的值传给本来要删除的节点
        //返回被删除的节点
        return y;
    }

    /*
     * 删除结点(z)，并返回被删除的结点
     *
     * 参数说明：
     *     tree 二叉树的根结点
     *     z 删除的结点
     */
    public void remove(T key) {
        BSTNode<T> z, node;

        if ((z = search(rootNode, key)) != null)
            if ((node = remove(this, z)) != null)
                node = null;
    }

    /*
     * (递归实现)查找"二叉树x"中键值为key的节点
     */
    private BSTNode<T> search(BSTNode<T> x, T key) {
        if (x == null)
            return x;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    public BSTNode<T> search(T key) {
        return search(rootNode, key);
    }

    /*
     * (非递归实现)查找"二叉树x"中键值为key的节点
     */
    private BSTNode<T> iterativeSearch(BSTNode<T> x, T key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);

            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return x;
        }

        return x;
    }

    public BSTNode<T> iterativeSearch(T key) {
        return iterativeSearch(rootNode, key);
    }

    /*
     * 查找最小结点：返回tree为根结点的二叉树的最小结点。
     */
    private BSTNode<T> minimum(BSTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        BSTNode<T> p = minimum(rootNode);
        if (p != null)
            return p.key;

        return null;
    }

    /*
     * 查找最大结点：返回tree为根结点的二叉树的最大结点。
     */
    private BSTNode<T> maximum(BSTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.right != null)
            tree = tree.right;
        return tree;
    }

    public T maximum() {
        BSTNode<T> p = maximum(rootNode);
        if (p != null)
            return p.key;

        return null;
    }

    /*
     * 找结点(x)的后继结点。即，查找"二叉树中数据值大于该结点"的"最小结点"。
     */
    public BSTNode<T> successor(BSTNode<T> x) {
        // 如果x存在右孩子，则"x的后继结点"为 "以其右孩子为根的子树的最小结点"。
        if (x.right != null)
            return minimum(x.right);

        // 如果x没有右孩子。则x有以下两种可能：
        // (01) x是"一个左孩子"，则"x的后继结点"为 "它的父结点"。
        // (02) x是"一个右孩子"，则查找"x的最低的父结点，并且该父结点要具有左孩子"，找到的这个"最低的父结点"就是"x的后继结点"。
        BSTNode<T> y = x.parent;
        while ((y != null) && (x == y.right)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    /*
     * 找结点(x)的前驱结点。即，查找"二叉树中数据值小于该结点"的"最大结点"。
     */
    public BSTNode<T> predecessor(BSTNode<T> x) {
        // 如果x存在左孩子，则"x的前驱结点"为 "以其左孩子为根的子树的最大结点"。
        if (x.left != null)
            return maximum(x.left);

        // 如果x没有左孩子。则x有以下两种可能：
        // (01) x是"一个右孩子"，则"x的前驱结点"为 "它的父结点"。
        // (01) x是"一个左孩子"，则查找"x的最低的父结点，并且该父结点要具有右孩子"，找到的这个"最低的父结点"就是"x的前驱结点"。
        BSTNode<T> y = x.parent;
        while ((y != null) && (x == y.left)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    /*
     * 销毁二叉树
     */
    private void destroy(BSTNode<T> tree) {
        if (tree == null)
            return;

        if (tree.left != null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);

        tree = null;
    }

    public void clear() {
        destroy(rootNode);
        rootNode = null;
    }

    /*
     * 打印"二叉查找树"
     *
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    private void print(BSTNode<T> tree, T key, int direction) {

        if (tree != null) {

            if (direction == 0)    // tree是根节点
                System.out.printf("%2d is root\n", tree.key);
            else                // tree是分支节点
                System.out.printf("%2d is %2d's %6s child\n", tree.key, key, direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }

    public void print() {
        if (rootNode != null)
            print(rootNode, rootNode.key, 0);
    }

    //--------------------------------------------------BSTNode----------------------------------------------------

    public class BSTNode<T extends Comparable<T>> {
        private T key;                // 关键字(键值)
        private BSTNode<T> left;      // 左孩子
        private BSTNode<T> right;     // 右孩子
        private BSTNode<T> parent;    // 父结点

        public BSTNode(T key, BSTNode<T> parent, BSTNode<T> left, BSTNode<T> right) {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T getKey() {
            return key;
        }

        public BSTNode<T> setKey(T key) {
            this.key = key;
            return this;
        }

        public BSTNode<T> getLeft() {
            return left;
        }

        public BSTNode<T> setLeft(BSTNode<T> left) {
            this.left = left;
            return this;
        }

        public BSTNode<T> getRight() {
            return right;
        }

        public BSTNode<T> setRight(BSTNode<T> right) {
            this.right = right;
            return this;
        }

        public BSTNode<T> getParent() {
            return parent;
        }

        public BSTNode<T> setParent(BSTNode<T> parent) {
            this.parent = parent;
            return this;
        }
    }
}
