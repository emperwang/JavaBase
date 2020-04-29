package com.wk.swing.displayname;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private JButton button;
    private JTextField textField;
    private JLabel label;

    public MyFrame(String title){
        super(title);
        label = new JLabel("姓名:");
        button= new JButton("确定");
        // 此处的16不是限制字数, 而是表示宽度
        textField = new JTextField(16);

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        contentPane.add(label);
        contentPane.add(textField);
        contentPane.add(button);

        button.addActionListener((e) -> {
            showName();
        });
    }

    private void showName(){
        String text = this.textField.getText();
        JOptionPane.showMessageDialog(this, "输入的姓名为:"+text);
    }
}
