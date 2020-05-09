package com.wk.swing.menubar;

import javax.swing.*;

public class MenuBarStarter {

    public static void createUI(){
        //MenuBar1 bar = new MenuBar1("menubar1");
        //menuBar2 bar = new menuBar2("menubar1");
        // PopMenuBar3 bar = new PopMenuBar3("popmenu");
        PopMenubar4 bar = new PopMenubar4("popmenu4");
        bar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        bar.setSize(400,500);
        bar.setVisible(true);
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
