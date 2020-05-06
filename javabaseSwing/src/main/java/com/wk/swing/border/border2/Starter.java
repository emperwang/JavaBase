package com.wk.swing.border.border2;

import javax.swing.*;

public class Starter {

    public static void createUI(){
        MyFrame border2 = new MyFrame("border2");
        border2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        border2.setVisible(true);
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
