package com.wk.swing.demo;

import javax.swing.*;
import java.awt.*;

public class swingdemo1 {

    public static void creatUI(){
        // JFrame之一个窗口,构造方法参数为窗口标题
        JFrame frame = new JFrame("Swing demo1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 内容面板
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());

        // 向面板中添加控件,如 JLabel  JButton
        pane.add(new JLabel("Hello Swing"));
        pane.add(new JButton("测试"));

        // 设置窗口的其他参数
        frame.setSize(400,300);

        // 显示窗口
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                creatUI();
            }
        });
    }
}
