package com.wk.swing.border.border1;

import javax.swing.*;

public class StarterAnyWhere {

    public static void createUI(){
        AfAnyWhereDemo border = new AfAnyWhereDemo("border");

        border.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        border.setVisible(true);
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
