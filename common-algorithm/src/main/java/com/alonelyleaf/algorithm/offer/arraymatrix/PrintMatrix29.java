package com.alonelyleaf.algorithm.offer.arraymatrix;

import java.util.ArrayList;

/**
 * 顺时针打印矩阵
 *
 * @author bijl
 * @date 6/29/21
 */
public class PrintMatrix29 {

    /**
     * 一层一层从外到里打印，观察可知每一层打印都有相同的处理步骤，唯一不同的是上下左右的边界不同了。因此使用四个变量 r1, r2, c1, c2 分别存储上下左右边界值，
     * 从而定义当前最外层。打印当前最外层的顺序：从左到右打印最上一行->从上到下打印最右一行->从右到左打印最下一行->从下到上打印最左一行。应当注意只有在 r1 != r2 时才打印最下一行，
     * 也就是在当前最外层的行数大于 1 时才打印最下一行，这是因为当前最外层只有一行时，继续打印最下一行，会导致重复打印。打印最左一行也要做同样处理。
     *
     * @param matrix
     * @return
     * @link http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/29.%20顺时针打印矩阵.html#题目描述
     */
    public ArrayList<Integer> printMatrix(int[][] matrix) {
        ArrayList<Integer> result = new ArrayList<>();
        int r1 = 0, r2 = matrix.length - 1;
        int c1 = 0, c2 = matrix[0].length - 1;
        while (r1 <= r2 && c1 <= c2) {
            for (int i = c1; i <= c2; i++) {
                result.add(matrix[r1][i]);
            }

            for (int i = r1 + 1; i <= r2; i++) {
                result.add(matrix[i][c2]);
            }

            if (r1 != r2) {
                for (int i = c2 - 1; i >= c1; i--) {
                    result.add(matrix[r2][i]);
                }
            }

            if (c1 != c2) {
                for (int i = r2 - 1; i > r1; i--) {
                    result.add(matrix[i][c1]);
                }
            }

            r1++;
            c1++;
            r2--;
            c2--;
        }

        return result;
    }

}
