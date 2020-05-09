package com.wk.swing.customcomponent.cusPanel;

import javax.swing.*;

public class Starter {

    public static void createUI(){
        //MyFrame panel = new MyFrame("cusPanel");
        //ImageViewDemo panel = new ImageViewDemo("view");
        BgImage panel = new BgImage("bg");
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setSize(400,500);
        panel.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createUI();
            }
        });
    }
}

