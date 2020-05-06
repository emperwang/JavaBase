package com.wk.swing.common;

import javax.swing.*;
import java.awt.*;

public class ColorfulLabel extends JLabel {

    public ColorfulLabel(String text, Color bg){
        super(text);
        setOpaque(true);
        setBackground(bg);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("宋体",Font.PLAIN,16));
    }
}
