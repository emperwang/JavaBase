package com.wk.swing.combobox;

import javax.swing.*;

public class Starter {

    private static void createUi(){
        // Combox1 color1 = new Combox1("Color1");
        Combox2 color1 = new Combox2("Color1");
        color1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        color1.setSize(800,600);
        color1.setVisible(true);
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
