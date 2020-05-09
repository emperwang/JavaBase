package com.wk.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dialog1 extends JFrame {

    public Dialog1(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());

        JTextField field = new JTextField(20);
        JButton button = new JButton("text");

        root.add(field);
        root.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = field.getText();
                if (text.equalsIgnoreCase("test1")){
                    test1();
                }
                if (text.equalsIgnoreCase("test2")){
                    test2();
                }
                if (text.equalsIgnoreCase("test3")){
                    test3();
                }
                if (text.equalsIgnoreCase("test4")){
                    test4();
                }
            }
        });

    }
    // 简单消息提示框
    public void test1(){
        JOptionPane.showMessageDialog(this, "操作已完成");;

        System.out.println("text1 exit");
    }

    // 简单输入框
    public void test2(){
        int sel = JOptionPane.showConfirmDialog(this, "是否确认删除?");
        // sel为用户点的第几个按钮
        if (sel == 0){
            System.out.println("模拟执行删除数据操作.");
        }
        System.out.println("test2 exit.");
    }

    // 简单数据输入框

    public void test3(){
        String input = JOptionPane.showInputDialog(this,
                "请输入你的身份证号\n(字母以x代替)",
                "000");
        if (input != null){
            System.out.println("输入的号码为: "+input);
        }
        System.out.println("test3 exit");
    }

    // 简单选项对话框
    public void test4(){
        Object[] color = {"red","blue","green"};

        String col = (String) JOptionPane.showInputDialog(
                this,
                "你最喜欢的颜色",
                "请选择",
                JOptionPane.PLAIN_MESSAGE,
                null,
                color,
                "red"
        );
        if (col != null){
            System.out.println("select : "+col);
        }
        System.out.println("test4 exit");
    }

}
