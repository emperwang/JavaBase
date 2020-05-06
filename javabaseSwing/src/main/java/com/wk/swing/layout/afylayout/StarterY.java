package com.wk.swing.layout.afylayout;

import javax.swing.*;

public class StarterY  {

    public static void createUI(){
        AfyLayoutDemo layoutDemo = new AfyLayoutDemo("LayoutY");

        layoutDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layoutDemo.setVisible(true);
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
