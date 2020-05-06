package com.wk.swing.panelDe.panel1;

import javax.swing.*;

public class Starter {
    public static void createUI(){
        AfPaneldemo panel = new AfPaneldemo("Afpanel");
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setVisible(true);

        panel.setSize(400,500);
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
