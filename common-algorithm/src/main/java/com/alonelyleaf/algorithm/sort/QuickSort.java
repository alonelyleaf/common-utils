package com.alonelyleaf.algorithm.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * https://github.com/CyC2018/CS-Notes/blob/master/notes/%E7%AE%97%E6%B3%95%20-%20%E6%8E%92%E5%BA%8F.md
 *
 * 快速排序
 *
 * @author bijl
 * @date 2020/4/8
 */
public class QuickSort<T extends Comparable<T>> extends Sort<T> {

    @Override
    public void sort(T[] nums) {
        shuffle(nums); //打乱数组
        sort(nums, 0, nums.length - 1);
    }

    private void sort(T[] nums, int l, int h) {
        if (h <= l)
            return;
        int j = partition(nums, l, h);
        sort(nums, l, j - 1);
        sort(nums, j + 1, h);
    }

    private void shuffle(T[] nums) {
        List<Comparable> list = Arrays.asList(nums);
        Collections.shuffle(list);
        list.toArray(nums);
    }

    private int partition(T[] nums, int l, int h) {
        int i = l, j = h + 1;
        T v = nums[l]; //获取基准值
        while (true) {
            while (less(nums[++i], v) && i != h) ; //找到一个大于等于基准值的数
            while (less(v, nums[--j]) && j != l) ; //找到一个小于等于基准值的数
            if (i >= j)
                break;
            swap(nums, i, j);  //交换两个数，以保证左右两个组内的数分别小于和大于基准值
        }
        swap(nums, l, j);
        return j;
    }
}
