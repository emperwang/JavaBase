package com.wk.swing.dialog;

import com.wk.swing.dialog.custom.cusdialog1;
import com.wk.swing.dialog.custom2.MyDialogFrame;
import com.wk.swing.dialog.custom3.MyDialogFrame3;

import javax.swing.*;

public class DialogStarter {

    public static void createUI(){
        // Dialog1 frame = new Dialog1("dialog");
       // FileChooserDialog frame = new FileChooserDialog("dialog");
        // cusdialog1 frame = new cusdialog1("cus");
        //MyDialogFrame frame = new MyDialogFrame("myDialog");
        MyDialogFrame3 frame = new MyDialogFrame3("dialog3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800,600);
        frame.setVisible(true);
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
