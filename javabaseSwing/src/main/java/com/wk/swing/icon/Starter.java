package com.wk.swing.icon;

import javax.swing.*;

public class Starter {
    public static void createUI(){
        //icondemo1 icon1 = new icondemo1("icon1");
        //icondemo2 icon1 = new icondemo2("icon1");
        icsondemo3 icon1 = new icsondemo3("icon1");
        icon1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon1.setVisible(true);
        icon1.setSize(400,500);
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
