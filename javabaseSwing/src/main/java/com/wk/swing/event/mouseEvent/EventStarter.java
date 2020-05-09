package com.wk.swing.event.mouseEvent;

import com.wk.swing.event.mouseEvent.drawLine.DrawFrame;
import com.wk.swing.event.mouseEvent.viewer.ImagerView;

import javax.swing.*;

public class EventStarter {

    public static void createUI(){
        //MouseEventDemo1 event = new MouseEventDemo1("event");
        // MouseEventDemo2 event = new MouseEventDemo2("event");
        //ImagerView event = new ImagerView("view");
        DrawFrame event = new DrawFrame("draw");
        event.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        event.setSize(800,900);
        event.setVisible(true);
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
