package com.wk.swing.dialog.custom2;

import javax.swing.*;
import java.awt.*;

public class MyDialog extends JDialog {
    public JTextField textField = new JTextField(30);
    public JButton okBtn = new JButton("ok");
    public MyDialog(JFrame owner){
        // 第三个参数 设置dialog 显示时是否阻塞
        super(owner,"text", true);

        // 设置一个容器
        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());

        // 布局子控件
        root.add(textField);
        root.add(okBtn);

        //
        okBtn.addActionListener( e -> {
            setVisible(false);
        });
    }

    public String exec(){

        this.setVisible(true);

        String text = textField.getText();
        return text;
    }


}
