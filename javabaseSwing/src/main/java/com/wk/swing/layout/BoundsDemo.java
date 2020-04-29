package com.wk.swing.layout;

import javax.swing.*;
import java.awt.*;

public class BoundsDemo extends JFrame {

    public BoundsDemo(String title){
        super(title);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        ColLabel a1 = new ColLabel("a1", Color.BLUE);
        ColLabel a2 = new ColLabel("a1", Color.GREEN);

        contentPane.add(a1);
        contentPane.add(a2);

        // 自定义布局
        a1.setBounds(new Rectangle(0,0,100,100));
        a2.setBounds(new Rectangle(100,100, 50,50));

    }

    private class ColLabel extends JLabel{
        public ColLabel(String text, Color color){
            super(text);
            // 不透明
            setOpaque(true);
            // 合适的大小
            setPreferredSize(new Dimension(70,60));
            // 背景颜色
            setBackground(color);
            //setForeground(color);
            // 水平对齐的方式
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
}
