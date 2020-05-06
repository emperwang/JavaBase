package com.wk.swing.icon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class icondemo1 extends JFrame {

    public icondemo1(String title){
        super(title);
        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());
        String fileName = "/images/ic_test.png";
        URL img = getClass().getResource(fileName);
        System.out.println(img.getPath());

        Icon icon = new ImageIcon(img);

        JLabel a1 = new JLabel("a1");
        a1.setIcon(icon);

        root.add(a1);
    }
}
