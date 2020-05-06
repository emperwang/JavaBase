package com.wk.swing.border.border2;

import com.wk.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new AfYLayout(4));

        //Border b1 = BorderFactory.createLineBorder(Color.GREEN, 4);
        //root.setBorder(b1);

        // 简单线条边框
        ColorfulLabel a1 = new ColorfulLabel("1", new Color(0xfcfcfc));
        root.add(a1, "60px");
        //a1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        a1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        // 特种边框
        ColorfulLabel a2 = new ColorfulLabel("1", new Color(0xfcfcfc));
        a2.setBorder(BorderFactory.createMatteBorder(1,5,1,1,Color.RED));
        root.add(a2,"60px");
        // 带标签边框
        ColorfulLabel a3 = new ColorfulLabel("3", new Color(0xfcfcfc));
        a3.setBorder(BorderFactory.createTitledBorder("title"));
        root.add(a3,"60px");

        // 复合边框
        ColorfulLabel a4 = new ColorfulLabel("4", new Color(0xfcfcfc));
        Border outer = BorderFactory.createLineBorder(Color.RED, 4);
        Border inter = BorderFactory.createLineBorder(Color.BLUE,4);
        a4.setBorder(BorderFactory.createCompoundBorder(outer, inter));
        root.add(a4,"60px");

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
