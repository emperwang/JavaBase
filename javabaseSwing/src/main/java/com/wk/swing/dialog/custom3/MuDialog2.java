package com.wk.swing.dialog.custom3;

import com.wk.swing.layout.AfColumnLayout;
import com.wk.swing.layout.AfRowLayout;
import com.wk.swing.panel.AfPanel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class MuDialog2 extends JDialog {

    public JTextField textField = new JTextField(20);
    public JButton button = new JButton("ok");

    // 记录用户是点击了 确定还是点击了x
    private boolean retValue = false;

    public MuDialog2(JFrame owner){
        super(owner,"cusdia", true);

        AfPanel root = new AfPanel();
        this.setContentPane(root);
        root.setLayout(new AfColumnLayout(10));
        root.padding(10);

        // 中间面板
        AfPanel mainPanel = new AfPanel();
        root.add(mainPanel, "1w");

        mainPanel.setLayout(new AfColumnLayout(10));
        mainPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        mainPanel.padding(10);

        mainPanel.add(new JLabel("请输入名字"), "20px");
        mainPanel.add(textField,"20px");

        // 底下
        AfPanel buttonPanel = new AfPanel();
        root.add(buttonPanel,"30px");
        buttonPanel.setLayout(new AfRowLayout(10));
        button.add(new JLabel(),"1w");
        buttonPanel.add(button,"20px");

        button.addActionListener( e -> {
            retValue = true;
            setVisible(false);
        });
    }

    public boolean exec(){
        Rectangle framRect = this.getOwner().getBounds();

        int width = this.getWidth();
        int height = this.getHeight();
        int x = framRect.x + (framRect.width - width)/2;
        int y = framRect.y + (framRect.height - height)/2;
        this.setBounds(x,y,width,height);
        this.setSize(300,200);
        this.setVisible(true);

        return  retValue;
    }

    public String getValue (){
        return textField.getText();
    }
}
