package com.wk.swing.event.mouseEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEventDemo2 extends JFrame {

    public MouseEventDemo2(String title){
        super(title);

        JPanel root = new JPanel();
        root.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.setContentPane(root);

        root.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    System.out.println("左键");
                }else if (e.getButton() == MouseEvent.BUTTON2){
                    System.out.println("中键");
                }else if (e.getButton() ==  MouseEvent.BUTTON3){
                    System.out.println("右键");
                }
                int clickCount = e.getClickCount();
                if (clickCount == 1){
                    System.out.println("单击事件");
                }else if (clickCount == 2){
                    System.out.println("双击事件");
                }
            }
        });
    }
}
