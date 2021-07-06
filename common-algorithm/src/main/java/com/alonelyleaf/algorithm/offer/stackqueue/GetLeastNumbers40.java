package com.alonelyleaf.algorithm.offer.stackqueue;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 最小的K个数
 *
 * @author bijl
 * @date 7/1/21
 */
public class GetLeastNumbers40 {


    /**
     * 使用小顶堆实现
     *
     * @param input
     * @param k
     * @return
     */
    public static ArrayList<Integer> getLeastNumbers(int[] input, int k) {

        if (k > input.length || k <= 0) {
            return new ArrayList<>();
        }

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        for (int num : input) {
            minHeap.add(num);
        }

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int a = 0; a < k; a++) {
            arrayList.add(minHeap.poll());
        }

        return arrayList;
    }

    /**
     * 使用大顶堆实现
     *
     * @param nums
     * @param k
     * @return
     */
    public ArrayList<Integer> GetLeastNumbers_Solution(int[] nums, int k) {

        if (k > nums.length || k <= 0) {
            return new ArrayList<>();
        }
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int num : nums) {
            maxHeap.add(num);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }
        return new ArrayList<>(maxHeap);
    }


    /**
     * 快速选择
     * 会改变原数组的元素位置
     * todo 快速排序
     *
     * @param nums
     * @param k
     * @return
     */
    public ArrayList<Integer> GetLeastNumbers_Solution2(int[] nums, int k) {
        ArrayList<Integer> ret = new ArrayList<>();
        if (k > nums.length || k <= 0) {
            return ret;
        }
        findKthSmallest(nums, k - 1);
        /* findKthSmallest 会改变数组，使得前 k 个数都是最小的 k 个数 */
        for (int i = 0; i < k; i++) {
            ret.add(nums[i]);
        }
        return ret;
    }

    public void findKthSmallest(int[] nums, int k) {
        int l = 0, h = nums.length - 1;
        while (l < h) {
            int j = partition(nums, l, h);
            if (j == k) {
                break;
            }
            if (j > k) {
                h = j - 1;
            } else {
                l = j + 1;
            }
        }
    }

    private int partition(int[] nums, int l, int h) {
        int p = nums[l];     /* 切分元素 */
        int i = l, j = h + 1;
        while (true) {
            while (i != h && nums[++i] < p) ;
            while (j != l && nums[--j] > p) ;
            if (i >= j)
                break;
            swap(nums, i, j);
        }
        swap(nums, l, j);
        return j;
    }

    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public static void main(String[] args) {
        int[] input = new int[]{4, 5, 1, 6, 2, 7, 3, 8};
        int k = 4;

        ArrayList<Integer> list = getLeastNumbers(input, k);
        System.out.println();
    }
}
