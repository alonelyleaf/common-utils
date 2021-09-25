package com.alonelyleaf.algorithm.offer.stackqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 单调栈结构
 * <p>
 * 给定一个不含有重复值的数组 arr，找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i] 小的位置。返回所有位置相应的信息。
 * <p>
 * arr = {3,4,1,5,6,2,7}
 * 返回如下二维数组作为结果:
 * { {-1, 2}, { 0, 2}, {-1,-1}, { 2, 5}, { 3, 5}, { 2,-1},} { 5,-1}}
 * -1 表示不存在。所以上面的结果表示在 arr 中，0 位置左边和右边离 0 位置最近且值比 arr[0] 小的位置是-1 和 2;
 * 1 位置左边和右边离 1 位置最近且值比 arr[1]小的位置是 0 和 2;2 位置左 边和右边离 2 位置最近且值比 arr[2]小的位置是-1 和-1......
 * <p>
 * 进阶问题:给定一个可能含有重复值的数组 arr，找到每一个 i 位置左边和右边离 i 位置最 近且值比 arr[i]小的位置。返回所有位置相应的信息。
 *
 * @author bijl
 * @date 8/18/21
 */
public class GetNearLessNoRepeat {

    /**
     * 单调栈结构
     * <p>
     * 准备一个栈，记为 stack<Integer>，栈中放的元素是数组的位置，开始时 stack 为 空。
     * 如果找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i]小的位置，那么需要让 stack 从栈顶到栈底的位置所代表的值是严格递减的;
     * 如果找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i]大的位置，那么需要让 stack 从栈顶到栈底的位置所代表的值是严格递增的。
     *
     * @param arr
     * @return
     */
    public int[][] getNearLessNoRepeat(int[] arr) {
        int[][] res = new int[arr.length][2];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {

            // 保持当前栈中元素为递增，如果出现当前元素比栈顶元素小，则代表栈顶元素找到了右侧小值，左侧小值在栈顶的下一个位置
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int popIndex = stack.pop();
                int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
                res[popIndex][0] = leftLessIndex;
                res[popIndex][1] = i;
            }

            // 将当前元素加入栈中
            stack.push(i);
        }

        // 剩余栈中元素保持递增，则其左侧小值为其栈中元素，右侧不存在
        while (!stack.isEmpty()) {
            int popIndex = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
            res[popIndex][0] = leftLessIndex;
            res[popIndex][1] = -1;
        }

        return res;
    }


    /**
     * 单调栈结构（值可重复）
     * <p>
     * 由于可重复，对应重复值，为了保证栈中位置的顺序，栈中保持结果为list集合，相同元素放入对应集合中，不同元素新建list
     * <p>
     * 准备一个栈，记为 stack<Integer>，栈中放的元素是数组的位置，开始时 stack 为 空。
     * 如果找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i]小的位置，那么需要让 stack 从栈顶到栈底的位置所代表的值是严格递减的;
     * 如果找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i]大的位置，那么需要让 stack 从栈顶到栈底的位置所代表的值是严格递增的。
     *
     * @param arr
     * @return
     */
    public int[][] getNearLess(int[] arr) {
        int[][] res = new int[arr.length][2];
        Stack<List<Integer>> stack = new Stack<>();

        for (int i = 0; i < arr.length; i++) {

            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                List<Integer> popIs = stack.pop();
                // 取位于下面位置的列表中，最晚加入的那个
                int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);

                for (Integer popi : popIs) {
                    res[popi][0] = leftLessIndex;
                    res[popi][1] = i;
                }
            }

            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }

        while (!stack.isEmpty()) {
            List<Integer> popIs = stack.pop();
            // 取位于下面位置的列表中，最晚加入的那个
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);

            for (Integer popi : popIs) {
                res[popi][0] = leftLessIndex;
                res[popi][1] = -1;
            }
        }
        return res;
    }
}
