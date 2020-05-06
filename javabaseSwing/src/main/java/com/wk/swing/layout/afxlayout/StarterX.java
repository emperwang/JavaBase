package com.wk.swing.layout.afxlayout;

import javax.swing.*;

public class StarterX {

    private static void createUI(){
        AfxLayoutDemo layoutDemo = new AfxLayoutDemo("AfLayoutX");
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
