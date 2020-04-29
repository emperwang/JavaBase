package com.wk.swing.displaytime;

import javax.swing.*;

public class Starter {

    public static void creatUi(){
        MyFrame frame = new MyFrame("disTime");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                creatUi();
            }
        });
    }
}
