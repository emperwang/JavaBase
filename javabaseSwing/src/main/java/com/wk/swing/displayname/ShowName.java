package com.wk.swing.displayname;

import javax.swing.*;

public class ShowName {

    public static void createUi(){
        MyFrame frame = new MyFrame("信息");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);
        frame.setVisible(true);

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
