package com.wk.swing.layout;

import javax.swing.*;
import java.awt.*;

public class FlowLayoutDemo extends JFrame{

    public FlowLayoutDemo(String title){
        super(title);
        Container contentPane = getContentPane();
        // 流布局  (align, 水平距离, 垂直距离)
        LayoutManager flowLayout = new FlowLayout(FlowLayout.RIGHT, 20,20);
        contentPane.setLayout(flowLayout);
        ColLabel a1 = new ColLabel("a", Color.GREEN);
        ColLabel a2 = new ColLabel("a", Color.RED);
        ColLabel a3 = new ColLabel("a", Color.BLUE);
        ColLabel a4 = new ColLabel("a", Color.YELLOW);

        contentPane.add(a1);
        contentPane.add(a2);
        contentPane.add(a3);
        contentPane.add(a4);
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
