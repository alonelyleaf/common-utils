package com.alonelyleaf.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://www.cnblogs.com/10158wsj/p/6782124.html?utm_source=tuicool&utm_medium=referral
 *
 * @author bijl
 * @date 2019/5/29
 */
public class SortMethods {

    public static void main(String[] args) {
        int[] a = new int[10];
        for (int i = 0; i < a.length; i++) {
            //a[i]=(int)(new Random().nextInt(100));
            a[i] = (int) (Math.random() * 10);
        }
        System.out.println("排序前的数组为："+Arrays.toString(a));
        long startTime = System.currentTimeMillis();
        //排序方法测试
        //s.insertSort(a);
        //s.sheelSort(a);
        //s.selectSort(a);
        //s.heapSort(a);
        //s.bubbleSort(a);
        quickSort(a, 0, 9);
        //s.mergeSort(a, 3, 7);
        //baseSort(a);
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("排序后的数组为："+ Arrays.toString(a));
    }



    //--------------------------------------------------插入排序----------------------------------------------------

    /**
     * 直接插入排序（Straight Insertion Sorting）
     * <p>
     * 基本思想：在要排序的一组数中，假设前面(n-1) [n>=2] 个数已经是排好顺序的，
     * 现在要把第n个数插到前面的有序数中，使得这n个数也是排好顺序的。如此反复循环，直到全部排好顺序。
     * <p>
     * 时间复杂度O(n^2)
     *
     * @param a 排序数组
     */
    public static void insertSort(int[] a) {
        int len = a.length;//单独把数组长度拿出来，提高效率
        int insertNum;//要插入的数
        for (int i = 1; i < len; i++) {//因为第一次不用，所以从1开始
            insertNum = a[i];
            int j = i - 1;//序列元素个数
            while (j >= 0 && a[j] > insertNum) {//从后往前循环，将大于insertNum的数向后移动
                a[j + 1] = a[j];//元素向后移动
                j--;
            }
            a[j + 1] = insertNum;//找到位置，插入当前元素
        }
    }

    /**
     * 希尔排序
     * 也称递减增量排序算法，是插入排序的一种更高效的改进版本。希尔排序是非稳定排序算法。
     * <p>
     * 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
     * 插入排序在对几乎已经排好序的数据操作时， 效率高， 即可以达到线性排序的效率
     * 但插入排序一般来说是低效的， 因为插入排序每次只能将数据移动一位
     * <p>
     * 时间复杂度：O(n^2)~O(nlog2n), 算法稳定性：不稳定
     *
     * @param a
     */
    public static void shellSort(int[] a) {
        int len = a.length;//单独把数组长度拿出来，提高效率
        while (len != 0) {
            len = len / 2;
            for (int i = 0; i < len; i++) {//分组
                for (int j = i + len; j < a.length; j += len) {//元素从第二个开始
                    int k = j - len;//k为有序序列最后一位的位数
                    int temp = a[j];//要插入的元素
                    while (k >= 0 && temp < a[k]) {//从后往前遍历
                        a[k + len] = a[k];
                        k -= len;//向后移动len位
                    }
                    a[k + len] = temp;
                }
            }
        }
    }

    //----------------------------------------------------------选择排序------------------------------------------------

    /**
     * 简单选择排序
     * 常用于取序列中最大最小的几个数时。
     * <p>
     * (如果每次比较都交换，那么就是交换排序；如果每次比较完一个循环再交换，就是简单选择排序。)
     * 遍历整个序列，将最小的数放在最前面。
     * 遍历剩下的序列，将最小的数放在最前面。
     * 重复第二步，直到只剩下一个数。
     * <p>
     * 时间复杂度O(n^2)
     *
     * @param a
     */
    public static void selectSort(int[] a) {
        int len = a.length;
        for (int i = 0; i < len; i++) {//循环次数
            int value = a[i];
            int position = i;
            for (int j = i + 1; j < len; j++) {//找到最小的值和位置
                if (a[j] < value) {
                    value = a[j];
                    position = j;
                }
            }
            a[position] = a[i];//进行交换
            a[i] = value;
        }
    }

    /**
     * 堆排序
     * <p>
     * 对简单选择排序的优化。
     * 将序列构建成大顶堆。
     * 将根节点与最后一个节点交换，然后断开最后一个节点。
     * 重复第一、二步，直到所有节点断开。
     * <p>
     * 时间复杂度O（nlogn）
     */
    public static void heapSort(int[] a) {
        int len = a.length;
        //循环建堆
        for (int i = 0; i < len - 1; i++) {
            //建堆
            buildMaxHeap(a, len - 1 - i);
            //交换堆顶和最后一个元素
            swap(a, 0, len - 1 - i);
        }
    }

    //交换方法
    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    //对data数组从0到lastIndex建大顶堆
    private static void buildMaxHeap(int[] data, int lastIndex) {
        //从lastIndex处节点（最后一个节点）的父节点开始
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            //k保存正在判断的节点
            int k = i;
            //如果当前k节点的子节点存在
            while (k * 2 + 1 <= lastIndex) {
                //k节点的左子节点的索引
                int biggerIndex = 2 * k + 1;
                //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
                if (biggerIndex < lastIndex) {
                    //若果右子节点的值较大
                    if (data[biggerIndex] < data[biggerIndex + 1]) {
                        //biggerIndex总是记录较大子节点的索引
                        biggerIndex++;
                    }
                }
                //如果k节点的值小于其较大的子节点的值
                if (data[k] < data[biggerIndex]) {
                    //交换他们
                    swap(data, k, biggerIndex);
                    //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

    //------------------------------------------------------交换排序----------------------------------------------

    /**
     * 冒泡排序算法：（从后往前）bubbleSort
     * <p>
     * 比较相邻的两个数，若前面的数大于后面的数，则交换两个数；
     * 这样对0到n-1个数据进行遍历，那么最大的数据就会被排到n-1处；
     * 重复步骤，直至再也不能交换
     * <p>
     * 时间复杂度O(n^2)
     */
    public static void bubbleSort1(int[] input) {

        int n = input.length;
        //外围循环n-1次，每次确定一个元素的位置，位于尾部
        for (int i = 0; i < n - 1; i++) {
            //内部循环，相邻元素进行比较，比较次数逐步减1
            for (int j = 0; j < n - 1 - i; j++) {
                //从小到大排序
                if (input[j] > input[j + 1]) {
                    int temp = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = temp;
                }
            }
        }

    }

    /**
     * 冒泡排序算法：（从后往前）bubbleSort
     * <p>
     * 对于一些数据，前面部分是乱序的，而后面部分是有序的。当前面的排序好后，后面的元素排序还需要重复前面的操作，这种做法
     * 是非常多余的。对与这种情况可以做出优化，比如加上一个标志（flag），初始值为false，有交换则置为true，如果某次排序没有
     * 交换元素，则表明后面的元素是有序的，跳出循环，排序结束。
     * <p>
     * * 时间复杂度O(n^2)
     */
    public static void bubbleSort2(int[] input) {

        int n = input.length;
        //外围循环n-1次，每次确定一个元素的位置，位于尾部
        for (int i = 0; i < n - 1; i++) {
            //标记位，如果这一趟发生了交换，则为true，否则为false。明显如果有一趟没有发生交换，说明排序已经完成。
            boolean flag = false;
            //内部循环，相邻元素进行比较，比较次数逐步减1
            for (int j = 0; j < n - 1 - i; j++) {
                //从小到大排序
                if (input[j] > input[j + 1]) {
                    int temp = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = temp;
                    //发生交换，置flag为true
                    flag = true;
                }
            }
            //没有发生交换，则表明已是有序，跳出循环
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 快速排序算法（快排）：quickSort
     * <p>
     * 它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
     * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     * 注意：第一遍快速排序不会直接得到最终结果，只能确定一个数的最终位置。为了得到最后结果，必须继续分解数组，直到数组
     * 不能再分解为止（只有一个数据），才能得到正确结果。
     * <p>
     * 先从数列中取出一个数作为基准数
     * 分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
     * 再对左右区间重复第二步，直到各区间只有一个数。
     * 快速排序算法是基于分治法的：
     * 分治法+挖坑（ 先从后向前找，再从前向后找）：
     * 3 4 6 8 9 10 5 2 16
     * 首先选定一个枢纽数3，pivotkey=a[0] ， a[0]被保存到pivotkey中，可以被认为，在a[0]上挖了一个坑，可以将其他数据填充到
     * 这里来。然后从后向前找到小于pivotkey的值2，a[0]=a[7]，a[0]上的坑被a[7]填上，结果又形成了一个新坑a[7].
     * <p>
     * 时间复杂度O（nlogn）, 算法稳定性：不稳定。
     */
    public static void quickSort(int[] a, int start, int end) {
        if (start < end) {
            int baseNum = a[start];//选基准值
            int i = start;
            int j = end;
            do {
                while ((a[i] < baseNum) && i < end) { //找到一个大于等于基准值的数
                    i++;
                }
                while ((a[j] > baseNum) && j > start) { //找到一个小于等于基准值的数
                    j--;
                }
                if (i <= j) { //交换两个数，以保证左右两个组内的数分别小于和大于基准值
                    swap(a, i, j);
                    i++;
                    j--;
                }
            } while (i <= j);  //处理到两个指针相遇
            if (start < j) {  //继续处理左边组
                quickSort(a, start, j);
            }
            if (end > i) { //继续处理左边组
                quickSort(a, i, end);
            }
        }
    }

    //----------------------------------------------------归并排序-------------------------------------------------

    /**
     * 归并排序
     * 速度仅次于快速排序，内存少的时候使用，可以进行并行计算的时候使用。
     *
     * 选择相邻两个数组成一个有序序列。
     *
     * 选择相邻的两个有序序列组成一个有序序列。
     *
     * 重复第二步，直到全部组成一个有序序列。
     *
     * 时间复杂度O（nlogn）
     *
     */
    public static void mergeSort(int[] a, int left, int right) {
        int t = 1;// 每组元素个数
        int size = right - left + 1;
        while (t < size) {
            int s = t;// 本次循环每组元素个数
            t = 2 * s;
            int i = left;
            while (i + (t - 1) < size) {
                merge(a, i, i + (s - 1), i + (t - 1));
                i += t;
            }
            if (i + (s - 1) < right){
                merge(a, i, i + (s - 1), right);
            }
        }
    }

    private static void merge(int[] data, int p, int q, int r) {
        int[] B = new int[data.length];
        int s = p;
        int t = q + 1;
        int k = p;
        while (s <= q && t <= r) {
            if (data[s] <= data[t]) {
                B[k] = data[s];
                s++;
            } else {
                B[k] = data[t];
                t++;
            }
            k++;
        }
        if (s == q + 1)
            B[k++] = data[t++];
        else
            B[k++] = data[s++];
        for (int i = p; i <= r; i++)
            data[i] = B[i];
    }

    //---------------------------------------------------基数排序-----------------------------------------------------

    /**
     * 基数排序
     * 用于大量数，很长的数进行排序时。
     *
     * 将所有的数的个位数取出，按照个位数进行排序，构成一个序列。
     *
     * 将新构成的所有的数的十位数取出，按照十位数进行排序，构成一个序列。
     *
     */
    public static void baseSort(int[] a) {
        //首先确定排序的趟数;
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        int time = 0;
        //判断位数;
        while (max > 0) {
            max /= 10;
            time++;
        }
        //建立10个队列;
        List<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 10; i++) {
            ArrayList<Integer> queue1 = new ArrayList<Integer>();
            queue.add(queue1);
        }
        //进行time次分配和收集;
        for (int i = 0; i < time; i++) {
            //分配数组元素;
            for (int j = 0; j < a.length; j++) {
                //得到数字的第time+1位数;
                int x = a[j] % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);
                ArrayList<Integer> queue2 = queue.get(x);
                queue2.add(a[j]);
                queue.set(x, queue2);
            }
            int count = 0;//元素计数器;
            //收集队列元素;
            for (int k = 0; k < 10; k++) {
                while (queue.get(k).size() > 0) {
                    ArrayList<Integer> queue3 = queue.get(k);
                    a[count] = queue3.get(0);
                    queue3.remove(0);
                    count++;
                }
            }
        }
    }
}
