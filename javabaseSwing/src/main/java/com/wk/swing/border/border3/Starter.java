package com.wk.swing.border.border3;

import javax.swing.*;

public class Starter {

    public static void createUI(){
        Frame3 frame3 = new Frame3("border");
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setVisible(true);


        frame3.setSize(400,500);
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
