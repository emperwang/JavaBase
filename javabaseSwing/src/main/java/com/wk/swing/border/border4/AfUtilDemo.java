package com.wk.swing.border.border4;

import com.wk.swing.borderutil.Afborder;
import com.wk.swing.common.ColorfulLabel;
import com.wk.swing.layout.AfYLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class AfUtilDemo extends JFrame {

    public AfUtilDemo(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new AfYLayout());

        // setting padding and margin
        //Afborder.addPadding(root, 20);
        //Afborder.addPadding(root, 16,8,0,8);
        //Afborder.addMargin(root, 20);

        // add 3 component
        ColorfulLabel a1 = new ColorfulLabel("a1", new Color(0xCDB5CD));
        root.add(a1, "60px");
        a1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        ColorfulLabel a2 = new ColorfulLabel("a2", new Color(0xCDC673));
        root.add(a2, "60px");
        a2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        ColorfulLabel a3 = new ColorfulLabel("a3", new Color(0xFFe4E1));
        root.add(a3, "60px");
        a3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }
}
