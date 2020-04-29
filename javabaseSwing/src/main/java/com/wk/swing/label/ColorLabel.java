package com.wk.swing.label;

import javax.swing.*;
import java.awt.*;

public class ColorLabel extends JFrame {

    public ColorLabel(String title){
        super(title);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        ColLabel a1 = new ColLabel("a", Color.RED);
        ColLabel a2 = new ColLabel("b", Color.BLUE);
        ColLabel a3 = new ColLabel("c", Color.GREEN);
        ColLabel a4 = new ColLabel("d", Color.GRAY);

        contentPane.add(a1);
        contentPane.add(a2);
        contentPane.add(a3);
        contentPane.add(a4);
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
