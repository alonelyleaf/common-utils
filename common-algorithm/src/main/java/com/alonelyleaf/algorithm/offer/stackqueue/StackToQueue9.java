package com.alonelyleaf.algorithm.offer.stackqueue;

import java.util.Stack;

/**
 * 用两个栈实现队列
 *
 * @author bijl
 * @date 6/29/21
 */
public class StackToQueue9 {

    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    /**
     * in 栈用来处理入栈（push）操作，out 栈用来处理出栈（pop）操作。一个元素进入 in 栈之后，出栈的顺序被反转。
     * 当元素要出栈时，需要先进入 out 栈，此时元素出栈顺序再一次被反转，因此出栈顺序就和最开始入栈顺序是相同的，先进入的元素先退出，这就是队列的顺序。
     *
     * @param node
     */
    public void push(int node) {
        stack1.push(node);
    }

    public int pop() throws Exception {

        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }

        if (stack2.isEmpty()) {

            throw new Exception("queue is empty");
        }

        return stack2.pop();
    }

}
