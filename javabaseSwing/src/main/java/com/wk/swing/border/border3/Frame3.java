package com.wk.swing.border.border3;

import com.wk.swing.common.ColorfulLabel;
import com.wk.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Frame3 extends JFrame {

    public Frame3(String title){
        super(title);
        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new AfYLayout(4));

        Border em1 = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        Border blue = BorderFactory.createLineBorder(Color.BLUE, 4);
        Border border1 = BorderFactory.createCompoundBorder(em1, blue);
        Border em2 = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        Border border = BorderFactory.createCompoundBorder(border1, em2);
        root.setBorder(border);

        ColorfulLabel a1 = new ColorfulLabel("a1", Color.RED);
        a1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        root.add(a1,"60px");

        ColorfulLabel a2 = new ColorfulLabel("a2", Color.RED);
        a2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        root.add(a2,"60px");

        ColorfulLabel a3 = new ColorfulLabel("a3", Color.RED);
        a3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        root.add(a3,"60px");
    }
}
