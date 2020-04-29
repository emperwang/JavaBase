package com.wk.swing.checkbox;

import javax.swing.*;

public class Starter {

    public static void createUi(){
        BoxDemo boxDemo = new BoxDemo("单选框");
        boxDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boxDemo.setSize(800, 600);
        boxDemo.setVisible(true);
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
