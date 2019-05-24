package com.alonelyleaf.concurrent.futuretask;

import java.util.concurrent.ExecutionException;

/**
 * @author bijl
 * @date 2019/5/24
 */
public class MyFutureTaskManager {

    public static Integer process(){

        MyFutureTask<Integer> myFutureTask = new MyFutureTask<Integer>(new MyCallable());

        //执行任务
        myFutureTask.run();

        try {
            //如果任务完成，则返回结果
            if (myFutureTask.isDone()){
                return myFutureTask.get();
            }
            //否则停止任务
            myFutureTask.cancel(true);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {

        System.out.println(process());
    }
}
