package com.wk.swing.label;

import javax.swing.*;

public class starter {
    public static void createUi(){
        ColorLabel label = new ColorLabel("label");
        label.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label.setSize(600,500);
        label.setVisible(true);
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createUi();
            }
        });
    }
}
