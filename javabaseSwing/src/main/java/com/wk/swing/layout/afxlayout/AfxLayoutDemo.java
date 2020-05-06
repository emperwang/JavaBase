package com.wk.swing.layout.afxlayout;

import com.wk.swing.layout.AfRowLayout;
import com.wk.swing.layout.afylayout.AfyLayoutDemo;

import javax.swing.*;
import java.awt.*;

public class AfxLayoutDemo extends JFrame {
    JLabel a1 = new ColorfullLabel("helloworld", Color.YELLOW);
    JLabel a2 = new ColorfullLabel("样例文本", Color.BLUE);
    JLabel a3 = new ColorfullLabel("Good Boy", Color.CYAN);
    JLabel a4 = new ColorfullLabel("占满剩余", Color.RED);

    public AfxLayoutDemo(String title){
        super(title);

        JPanel root = new JPanel();
        setContentPane(root);
        root.setLayout(new AfRowLayout());
        root.add(a1, "100px");
        root.add(a2, "30%");
        root.add(a3, "auto");
        root.add(a4, "1w");

        setSize(400, 500);
    }

    public static class ColorfullLabel extends JLabel{

        public ColorfullLabel(String text, Color bg){
            super(text);

            setOpaque(true);
            setBackground(bg);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("宋体",Font.PLAIN, 16));
        }
    }
}
