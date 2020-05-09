package com.wk.swing.dialog.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cusdialog1 extends JFrame {

    public cusdialog1(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());

        JButton button = new JButton("test");
        root.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = getUserInput();
                System.out.println("用户输入: " + str);
            }
        });

    }

    // 创建对话框  显示， 并获取用户输入
    private String getUserInput(){
        // 创建JDialog
        // 参数(JFrame owner, String title, boolean modal)
        JDialog dialog = new JDialog(this, "test", true);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        dialog.setContentPane(panel);

        // 添加控件到对话框
        JTextField textField = new JTextField(20);
        JButton button = new JButton("OK");
        panel.add(textField);
        panel.add(button);

        // 点击按钮时, 关闭对话框
        button.addActionListener((e) -> {
            dialog.setVisible(false);
        });

        // 显示对话框(setVisible() 方法会阻塞, 直到对话框关闭)
        dialog.setSize(300,100);
        dialog.setVisible(true);
        // 对话框关闭后,取得用户输入,并返回
        String text = textField.getText();
        return text;
    }
}
