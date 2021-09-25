package com.alonelyleaf.algorithm.offer.search;

/**
 * 矩阵中的路径
 * <p>
 * 判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。路径可以从矩阵中的任意一个格子开始，每一步可以在矩阵中向上下左右移动一个格子。如果一条路径经过了矩阵中的某一个格子，则该路径不能再进入该格子。
 *
 * @author bijl
 * @date 8/17/21
 */
public class MatrixHasPath12 {

    private final static int[][] next = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    private int rows;
    private int cols;

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     * <p>
     * 使用回溯法（backtracking）进行求解，它是一种暴力搜索方法，通过搜索所有可能的结果来求解问题。回溯法在一次搜索结束时需要进行回溯（回退），
     * 将这一次搜索过程中设置的状态进行清除，从而开始一次新的搜索过程。例如下图示例中，从 f 开始，下一步有 4 种搜索可能，如果先搜索 b，需要将 b 标记为已经使用，
     * 防止重复使用。在这一次搜索结束之后，需要将 b 的已经使用状态清除，并搜索 c。
     *
     * @param matrix string字符串
     * @param rows   int整型
     * @param cols   int整型
     * @param str    string字符串
     * @return bool布尔型
     */
    public boolean hasPath(String matrix, int rows, int cols, String str) {

        if (rows == 0 || cols == 0) {
            return false;
        }

        this.rows = rows;
        this.cols = cols;

        char[][] array = buildMatrix(matrix.toCharArray(), rows, cols);
        char[] path = str.toCharArray();
        boolean[][] marked = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                if (backTracking(array, path, marked, 0, i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean backTracking(char[][] matrix, char[] pathList, boolean[][] marked, int pathLen, int r, int c) {
        if (pathLen == pathList.length) {
            return true;
        }

        // 如果节点不合法，或节点值不等于路径值，或已走过，则为false
        if (r < 0 || r >= rows || c < 0 || c >= cols || matrix[r][c] != pathList[pathLen] || marked[r][c]) {
            return false;
        }

        marked[r][c] = true;
        for (int[] n : next) {
            if (backTracking(matrix, pathList, marked, pathLen + 1, r + n[0], c + n[1])) {
                return true;
            }
        }

        marked[r][c] = false;
        return false;
    }

    public char[][] buildMatrix(char[] array, int rows, int cols) {
        char[][] matrix = new char[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = array[index++];
            }
        }

        return matrix;
    }
}
