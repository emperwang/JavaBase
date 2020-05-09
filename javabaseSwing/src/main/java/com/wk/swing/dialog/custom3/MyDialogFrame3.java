package com.wk.swing.dialog.custom3;

import javax.swing.*;
import java.awt.*;

public class MyDialogFrame3 extends JFrame {

    public MyDialogFrame3(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());

        JButton testBtn = new JButton("test");
        root.add(testBtn);

        testBtn.addActionListener(e -> {
            String input = getUserInput();
            System.out.println("用户输入: " + input);
        });
    }

    private String getUserInput(){
        //MyDialog dialog = new MyDialog(this);
        MuDialog2 dialog = new MuDialog2(this);
        dialog.setSize(300,100);
        if(dialog.exec()){
            return dialog.getValue();
        }
        return null;
    }
}
