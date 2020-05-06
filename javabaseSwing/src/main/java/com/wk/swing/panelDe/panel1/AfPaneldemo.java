package com.wk.swing.panelDe.panel1;

import com.wk.swing.layout.AfXLayout;
import com.wk.swing.panel.AfPanel;

import javax.swing.*;
import java.awt.*;

public class AfPaneldemo extends JFrame{
    private JTextField text1 = new JTextField();
    private JTextArea area1 = new JTextArea();
    private JButton b1 = new JButton("发送");

    public AfPaneldemo(String title){
        super(title);
        AfPanel root = new AfPanel();
        this.setContentPane(root);
        root.padding(4);

        root.setLayout(new BorderLayout());

        root.add(area1, BorderLayout.CENTER);

        if (true){
            AfPanel p = new AfPanel();
            p.setLayout(new AfXLayout());
            p.padding(4);
            p.preferredHeight(30);

            p.add(text1, "1w");
            p.add(b1, "80px");

            root.add(p, BorderLayout.PAGE_START);
        }
    }
}
