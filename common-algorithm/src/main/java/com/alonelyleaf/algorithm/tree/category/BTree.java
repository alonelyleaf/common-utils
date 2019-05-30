package com.alonelyleaf.algorithm.tree.category;

/**
 * B树（英语：B-tree）是一种自平衡的树，能够保持数据有序。这种数据结构能够让查找数据、顺序访问、插入数据及删除的动作，都在对数时间内完成。
 *
 * 红黑树是将输入存入内存的一种内部查找树。而B树是前面平衡树算法的扩展，它支持保存在磁盘或者网络上的符号表进行外部查找，
 * 这些文件可能比我们以前考虑的输入要大的多（难以存入内存）。既然内容保存在磁盘中，那么自然会因为树的深度过大而造成磁盘
 * I/O读写过于频繁（磁盘读写速率是有限制的），进而导致查询效率低下。那么降低树的深度自然很重要了。因此，我们引入了B树，多路查找树。
 *
 * https://blog.csdn.net/herry816/article/details/89525077
 * https://blog.csdn.net/tclxspy/article/details/52535605
 *
 * B树作为一种多路搜索树（并不是二叉的）：
 * 　　1) 定义任意非叶子结点最多只有M个儿子；且M>2；
 * 　　2) 根结点的儿子数为[2, M]；
 * 　　3) 除根结点以外的非叶子结点的儿子数为[M/2, M]；
 * 　　4) 每个结点存放至少M/2-1（取上整）和至多M-1个关键字；（至少2个关键字）
 * 　　5) 非叶子结点的关键字个数=指向儿子的指针个数-1；
 * 　　6) 非叶子结点的关键字：K[1], K[2], …, K[M-1]；且K[i] < K[i+1]；
 * 　　7) 非叶子结点的指针：P[1], P[2], …, P[M]；其中P[1]指向关键字小于K[1]的子树，P[M]指向关键字大于K[M-1]的子树，
 *        其它P[i]指向关键字属于(K[i-1], K[i])的子树；
 *
 * @author bijl
 * @date 2019/5/30
 */
public class BTree {



    public class BTNode<K extends Comparable<K>, V> {

        // 构成B树的最小度数
        public final static int MIN_DEGREE = 3;
        // 除根节点外，每个结点中总键数的下限
        public final static int LOWER_BOUND_KEYNUM = MIN_DEGREE - 1;
        // 包含根节点外，每个结点中总键数的上限
        public final static int UPPER_BOUND_KEYNUM = (MIN_DEGREE * 2) - 1;

        protected boolean mIsLeaf;// 标记此节点是否为叶子结点
        protected int mCurrentKeyNum;// 此节点的键数量计数器
        protected BTKeyValue<K, V>[] mKeys;// 用于存键值对的数组
        protected BTNode<K, V>[] mChildren;// 用于存子结点的数组

        /**
         * 构造函数
         */
        @SuppressWarnings("unchecked")
        public BTNode() {
            mIsLeaf = true;
            mCurrentKeyNum = 0;
            mKeys = new BTKeyValue[UPPER_BOUND_KEYNUM];
            mChildren = new BTNode[UPPER_BOUND_KEYNUM + 1];
        }

        protected BTNode<?, ?> getChildNodeAtIndex(BTNode<?, ?> btNode, int keyIdx, int nDirection) {
            if (btNode.mIsLeaf) {
                return null;
            }
            keyIdx += nDirection;
            if ((keyIdx < 0) || (keyIdx > btNode.mCurrentKeyNum)) {
                throw new IllegalArgumentException();
            }

            return btNode.mChildren[keyIdx];
        }

        /**
         * 返回btNode节点中位于keyIdx位上的键左边的子结点
         *
         * @param btNode
         * @param keyIdx
         * @return
         */
        protected BTNode<?, ?> getLeftChildAtIndex(BTNode<?, ?> btNode, int keyIdx) {
            return getChildNodeAtIndex(btNode, keyIdx, 0);
        }

        /**
         * 返回btNode节点中位于keyIdx位上的键右边的子结点
         *
         * @param btNode
         * @param keyIdx
         * @return
         */
        protected BTNode<?, ?> getRightChildAtIndex(BTNode<?, ?> btNode, int keyIdx) {
            return getChildNodeAtIndex(btNode, keyIdx, 1);
        }

        /**
         * @param parentNode
         * @param keyIdx
         * @return 返回父结点的keyIdx位上的子结点的左兄弟结点
         */
        protected BTNode<?, ?> getLeftSiblingAtIndex(BTNode<?, ?> parentNode, int keyIdx) {
            return getChildNodeAtIndex(parentNode, keyIdx, -1);
        }

        /**
         * @param parentNode
         * @param keyIdx
         * @return 返回父结点的keyIdx位上的子结点的右兄弟结点
         */
        protected BTNode<?, ?> getRightSiblingAtIndex(BTNode<?, ?> parentNode, int keyIdx) {
            return getChildNodeAtIndex(parentNode, keyIdx, 1);
        }


        /**
         * 判断父结点的keyIdx位上的子结点是否存在左兄弟结点
         *
         * @param parentNode
         * @param keyIdx
         * @return
         */
        protected boolean hasLeftSiblingAtIndex(BTNode<?, ?> parentNode, int keyIdx) {
            if (keyIdx - 1 < 0) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * 判断父结点的keyIdx位上的子结点是否存在右兄弟结点
         *
         * @param parentNode
         * @param keyIdx
         * @return
         */
        protected boolean hasRightSiblingAtIndex(BTNode<?, ?> parentNode, int keyIdx) {
            if (keyIdx + 1 > parentNode.mCurrentKeyNum) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * @param <K>
     * @param <V>
     * @author Herry
     */
    public class BTKeyValue<K extends Comparable<K>, V> {

        protected K mKey;
        protected V mValue;

        public BTKeyValue(K mKey, V mValue) {
            super();
            this.mKey = mKey;
            this.mValue = mValue;
        }
    }
}
