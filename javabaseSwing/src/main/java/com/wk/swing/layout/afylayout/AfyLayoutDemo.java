package com.wk.swing.layout.afylayout;

import com.wk.swing.layout.AfColumnLayout;

import javax.swing.*;
import java.awt.*;

public class AfyLayoutDemo extends JFrame {
    JLabel a1 = new Colorfillabel("helloworld", Color.YELLOW);
    JLabel a2 = new Colorfillabel("样例文本", Color.BLUE);
    JLabel a3 = new Colorfillabel("Good Boy", Color.CYAN);
    JLabel a4 = new Colorfillabel("占满剩余", Color.RED);

    public AfyLayoutDemo(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);

        root.setLayout(new AfColumnLayout(5));

        root.add(a1, "100px");
        root.add(a2, "30%");
        root.add(a3,"auto");
        root.add(a4, "1w");

        setSize(400,500);
    }

    private static class Colorfillabel extends JLabel{
        public Colorfillabel(String text, Color bgColor){
            super(text);
            setOpaque(true);
            setBackground(bgColor);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("宋体", Font.PLAIN, 16));
        }
    }
}
