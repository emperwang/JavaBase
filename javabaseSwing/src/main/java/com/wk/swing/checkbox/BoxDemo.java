package com.wk.swing.checkbox;

import javax.swing.*;
import java.awt.*;

public class BoxDemo extends JFrame {
    private JCheckBox checkBox;
    private JTextField textField;

    public BoxDemo(String title){
        super(title);
        // 参数指定选项文本
        checkBox = new JCheckBox("如果有邮箱,请输入.");
        textField = new JTextField(20);

        // add
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        contentPane.add(checkBox);
        contentPane.add(textField);

        // 默认选中
        checkBox.setSelected(true);

        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()){
                textField.setEnabled(true);
            }else{
                textField.setEnabled(false);
            }
        });
    }
}
