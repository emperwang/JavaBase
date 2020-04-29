package com.wk.swing.displaytime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFrame extends JFrame {

    private JLabel label = new JLabel("00:00:00");
    private JButton button = new JButton("display time");

    public MyFrame(String title){
        super(title);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        contentPane.add(button);
        contentPane.add(label);
        // 第一种方法 常规实现
        /*MyButtonListener listener = new MyButtonListener();
        button.addActionListener(listener);*/

        // 第二种方法  内部类
        /*button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFrame.this.showTime();
            }
        });*/

        // 第三种方法  lambda
        button.addActionListener((e) -> {
            MyFrame.this.showTime();
        });
    }

    private void showTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(new Date());
        label.setText(time);
    }

    private class MyButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("command: " + e.getActionCommand());
            System.out.println("param str:" +e.paramString());
            System.out.println("push button");
            MyFrame.this.showTime();
        }
    }
}
