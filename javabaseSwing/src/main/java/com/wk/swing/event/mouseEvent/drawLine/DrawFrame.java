package com.wk.swing.event.mouseEvent.drawLine;

import javax.swing.*;
import java.awt.*;

public class DrawFrame extends JFrame {

    public DrawFrame(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 将涂鸦板添加到界面里面
        HandlerDrawView draw = new HandlerDrawView();

        root.add(draw, BorderLayout.CENTER);

        draw.setBorder(BorderFactory.createLineBorder(Color.GREEN,1));
    }
}
