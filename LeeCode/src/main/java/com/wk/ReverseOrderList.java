package com.wk;

import java.util.ArrayList;
import java.util.List;

/**
 * 反向遍历 单向链表
 */
public class ReverseOrderList {

    public class Node<T>{
        T value;
        Node<T> next;
        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    public Node buildList(){
        Node<Integer> n3 = new Node<>(13, null);
        Node<Integer> n2 = new Node<>(12, n3);
        Node<Integer> n1 = new Node<>(11, n2);
        Node<Integer> root = new Node<>(10, n1);
        return root;
    }
    // 利用递归来实现反向遍历
    public void reverseOrder(Node node){
        if (node == null){
            return;
        }
        reverseOrder(node.next);
        System.out.printf("%d \t ",node.value);
    }

    // 利用容器 或者 栈 来实现反向遍历
    public void reverseWithList(Node node){
        List<Node> list = new ArrayList<>();
        while (node != null){
            list.add(node);
            node = node.next;
        }
        for (int i = list.size()-1; i>=0;i--){
            System.out.printf("%d \t ", list.get(i).value);
        }
    }

    public static void main(String[] args) {
        ReverseOrderList orderList = new ReverseOrderList();
        Node node = orderList.buildList();
        orderList.reverseOrder(node);
        System.out.println("------------------");
        orderList.reverseWithList(node);
    }
}
