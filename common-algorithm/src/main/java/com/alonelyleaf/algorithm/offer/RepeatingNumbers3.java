package com.alonelyleaf.algorithm.offer;

/**
 * 数组中重复的数字
 *
 * @author bijl
 * @date 6/8/21
 */
public class RepeatingNumbers3 {

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     * <p>
     * 要求时间复杂度 O(N)，空间复杂度 O(1)。因此不能使用排序的方法，也不能使用额外的标记数组。
     * <p>
     * 对于这种数组元素在 [0, n-1] 范围内的问题，可以将值为 i 的元素调整到第 i 个位置上进行求解。
     * 在调整过程中，如果第 i 位置上已经有一个值为 i 的元素，就可以知道 i 值重复。
     *
     * @param numbers int整型一维数组
     * @return int整型
     * @link http://www.cyc2018.xyz/算法/剑指%20Offer%20题解/3.%20数组中重复的数字.html#解题思路
     */
    public int duplicate(int[] numbers) {

        for (int i = 0; i < numbers.length; i++) {

            // 如果元素不在应该的位置上，则进行判断和调整
            while (numbers[i] != i) {

                // 如果当前位置值与目标位置值一致，则为重复，返回
                if (numbers[i] == numbers[numbers[i]]) {
                    return numbers[i];
                }

                // 将当前位置值交换到目标位置，下次循环时则会判断位置上的新值
                swap(numbers, i, numbers[i]);
            }

            swap(numbers, i, numbers[i]);
        }

        return -1;
    }

    private void swap(int[] numbers, int i, int j) {

        int t = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = t;
    }
}
