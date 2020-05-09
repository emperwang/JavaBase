package com.wk.swing.JlistConponent;

import javax.swing.*;

public class JListStarter {

    public static void createUI(){
        //JListFrame1 list = new JListFrame1("jlist");
        //JListFrame2 list = new JListFrame2("jlist");
        //JListFrame3 list= new JListFrame3("listEvent");
        // JListFrame4 list = new JListFrame4("frame4");

        JListFileFrame list = new JListFileFrame("browserFile");

        list.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        list.setSize(800,600);
        list.setVisible(true);
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
