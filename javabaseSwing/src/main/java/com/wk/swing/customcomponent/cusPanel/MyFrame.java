package com.wk.swing.customcomponent.cusPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame(String title){
        super(title);

        Container root = this.getContentPane();
        root.setLayout(new BorderLayout());

        //MyPanelFirst panel = new MyPanelFirst();
        //SinMyPanel panel = new SinMyPanel();
        //ImagePanel2 panel = new ImagePanel2();
        AfScalerUtilDemo panel = new AfScalerUtilDemo();
        //panel.setBackground(Color.BLUE);
        root.add(panel, BorderLayout.CENTER);
    }
}
