package com.alonelyleaf.algorithm.sort;

/**
 * @author bijl
 * @date 2020/6/4
 */
public class Sort2 {

    public static void  main(String[] args){

        Sort2 sort = new Sort2();

        int[] nums = new int[]{5,2,6,9,3,7,12};
        sort.sort(nums, 0, nums.length - 1);
        for(int i=0; i< nums.length; i++){
            System.out.println(nums[i]);
        }
    }

    public void sort(int[] nums, int l, int h){

        if(l>=h){
            return;
        }

        int j = partition(nums, l, h);
        sort(nums, l, j-1);
        sort(nums, j+1, h);
    }

    private int partition(int[] nums, int l, int h){

        int i=l,j=h+1;
        int base = nums[l];
        while(true){

            while(nums[++i] < base && i!=h);
            while(nums[--j] > base && j!=l);
            if(i>=j){
                break;
            }
            swap(nums, i, j);
        }
        swap(nums, l, j);
        return j;
    }

    private void swap(int[] nums, int i, int j){

        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
