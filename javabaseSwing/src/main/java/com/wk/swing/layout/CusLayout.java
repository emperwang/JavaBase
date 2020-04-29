package com.wk.swing.layout;

import javax.swing.*;
import java.awt.*;

public class CusLayout extends JFrame {
    private JLabel lab1;
    private JLabel lab2;
    public CusLayout(String title){
        super(title);
        Container contentPane = getContentPane();
        contentPane.setLayout(new SimpleLayout());
        lab1 = new JLabel("hello world");
        lab2 = new JLabel("jlabel");
        contentPane.add(lab1);
        contentPane.add(lab2);
    }

    public class SimpleLayout implements LayoutManager{

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            if (lab1.isVisible()){
                Dimension size = lab1.getPreferredSize();
                int h = (int)(height - size.getHeight())/2;
                int w = (int)(width - size.getWidth())/2;
                lab1.setBounds(new Rectangle(h,w,(int) size.getWidth(),(int)size.getHeight()));
            }
            if (lab2.isVisible()){
                Dimension size = lab2.getPreferredSize();
                int x = (int)(width - size.getWidth());
                int y = 0;
                lab2.setBounds(new Rectangle(x,y,(int) size.getWidth(),(int)size.getHeight()));
            }
        }
    }
}
