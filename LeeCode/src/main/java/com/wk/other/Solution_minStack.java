package com.wk.other;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: wk
 * @Date: 2021/1/12 10:46
 * @Description
 */
public class Solution_minStack {
    /*
    设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
        push(x) —— 将元素 x 推入栈中。
        pop() —— 删除栈顶的元素。
        top() —— 获取栈顶元素。
        getMin() —— 检索栈中的最小元素。

        示例:
        输入：
        ["MinStack","push","push","push","getMin","pop","top","getMin"]
        [[],[-2],[0],[-3],[],[],[],[]]
        输出：
        [null,null,null,null,-3,null,0,-2]

        解释：
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        minStack.getMin();   --> 返回 -3.
        minStack.pop();
        minStack.top();      --> 返回 0.
        minStack.getMin();   --> 返回 -2.
    提示：
        pop、top 和 getMin 操作总是在 非空栈 上调用。
     */
    class MinStack {
        private LinkedBlockingDeque<Integer> stack;
        private LinkedBlockingDeque<Integer> minList;
        /**
         * initialize your data structure here.
         */
        public MinStack() {
            stack = new LinkedBlockingDeque<>();
            minList = new LinkedBlockingDeque<>();
            minList.push(Integer.MAX_VALUE);
        }
        /*
        使用两个栈,一个栈存储数据, 另一个栈存储每个数据对应的最小值
        出栈时, 对两个栈同时进行操作
         */
        public void push(int x) {
            stack.push(x);
            minList.push(Math.min(minList.peekFirst(),x));
        }

        public void pop() {
            stack.pop();
            minList.pop();
        }

        public int top() {
            return stack.peekFirst();
        }

        public int getMin() {
            return minList.peekFirst();
        }
    }
    /*
    上面使用的是 辅助栈的方式, 本例使用一个栈即 栈内同时存储元素和此元素对应的最小值
     */
    class MinStack2 {
        private LinkedBlockingDeque<int[]> stack;
        /**
         * initialize your data structure here.
         */
        public MinStack2() {
            stack = new LinkedBlockingDeque<>();
        }
        public void push(int x) {
            if (stack.isEmpty()){
                stack.push(new int[]{x,x});
            }else{
                stack.push(new int[]{x,Math.min(x, stack.peekFirst()[1])});
            }
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return stack.peekFirst()[0];
        }

        public int getMin() {
            return stack.peekFirst()[1];
        }
    }

    public static void main(String[] args) {

    }
}
