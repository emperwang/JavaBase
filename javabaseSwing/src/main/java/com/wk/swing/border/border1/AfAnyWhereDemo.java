package com.wk.swing.border.border1;

import com.wk.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AfAnyWhereDemo extends JFrame{
    public AfAnyWhereDemo(String title){
        super(title);

        // 创建一个box, 设置为顶层容器
        JPanel root = new JPanel();
        setContentPane(root);
        root.setLayout(new AfYLayout(4));

        ColorfulLabel a1 = new ColorfulLabel("1", new Color(0xC0FF3E));
        root.add(a1,"60px");
        ColorfulLabel a2 = new ColorfulLabel("2", new Color(0xEEEE00));
        root.add(a2, "60px");

        LineBorder lineBorder = new LineBorder(Color.BLUE, 4);
        a2.setBorder(lineBorder);

        ColorfulLabel a3 = new ColorfulLabel("3", new Color(0x98FB98));
        Border border = BorderFactory.createLineBorder(Color.GREEN, 4);
        a3.setBorder(border);
        root.add(a3, "60px");

        this.setSize(400,500);
    }


    public static class ColorfulLabel extends JLabel{
        public ColorfulLabel(String text, Color bg){
            super(text);
            setOpaque(true);
            setBackground(bg);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("宋体", Font.PLAIN, 16));
        }
    }
}
