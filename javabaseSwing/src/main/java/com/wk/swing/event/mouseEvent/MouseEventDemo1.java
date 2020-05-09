package com.wk.swing.event.mouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventDemo1 extends JFrame{

    public MouseEventDemo1(String title){
        super(title);

        //
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.setContentPane(panel);

        panel.addMouseListener(new MouseListener() {
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

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
}
