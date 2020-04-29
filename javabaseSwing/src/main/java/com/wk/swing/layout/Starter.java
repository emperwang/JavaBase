package com.wk.swing.layout;

import javax.swing.*;

public class Starter {
    public static void createUi(){
        //FlowLayoutDemo flow = new FlowLayoutDemo("flow");
        // BorderLayoutDemo flow = new BorderLayoutDemo("Border");
        // CardLayoutDemo flow = new CardLayoutDemo("cardLayout");
        // BoundsDemo flow = new BoundsDemo("bounds");
        CusLayout flow = new CusLayout("cus");
        flow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flow.setSize(500,400);
        flow.setVisible(true);
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
