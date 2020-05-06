package com.wk.swing.border.border4;

import javax.swing.*;

public class Starter {

    public static void createUI(){
        AfUtilDemo utilDemo = new AfUtilDemo("border4");
        utilDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        utilDemo.setVisible(true);

        utilDemo.setSize(400,500);
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
