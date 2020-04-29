package com.wk.swing.layout;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutDemo extends JFrame{

    public BorderLayoutDemo(String title){
        super(title);
        Container contentPane = getContentPane();
        // 边界布局
        BorderLayout borderLayout = new BorderLayout(10,20);
        contentPane.setLayout(borderLayout);
        ColLabel a1 = new ColLabel("a", Color.GREEN);
        ColLabel a2 = new ColLabel("a", Color.RED);
        ColLabel a3 = new ColLabel("a", Color.BLUE);
        ColLabel a4 = new ColLabel("a", Color.YELLOW);
        ColLabel a5 = new ColLabel("a", Color.BLACK);

        contentPane.add(a1, BorderLayout.NORTH);
        contentPane.add(a2, BorderLayout.SOUTH);
        contentPane.add(a3, BorderLayout.WEST);
        contentPane.add(a4, BorderLayout.EAST);
        contentPane.add(a5, BorderLayout.CENTER);
        a1.setPreferredSize(new Dimension(200,100));
    }

    private class ColLabel extends JLabel {

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
