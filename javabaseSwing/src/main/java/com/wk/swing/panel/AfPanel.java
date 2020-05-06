package com.wk.swing.panel;

import com.wk.swing.borderutil.Afborder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// 用于快速布局的一个容器
public class AfPanel extends JPanel{

    private Color bgColor;

    public AfPanel(){
        this.setOpaque(false);  // 默认使用背景透明
    }
    // 内边距
    public AfPanel padding(int size){
        return padding(size, size,size,size);
    }

    public AfPanel padding(int top, int left, int bottom, int right){
        Afborder.addPadding(this, top,left,bottom,right);
        return this;
    }
    // 外边距
    public AfPanel margin(int size){
        return margin(size,size,size,size);
    }

    public AfPanel margin(int top, int left, int bottom, int right){
        Afborder.addMargin(this, top, left, bottom, right);
        return this;
    }

    // 附加一个外边框
    public void addOuterBorder(Border outerBorder){
        Afborder.addOuterBorder(this, outerBorder);
    }

    // 附件一个内边框
    public void addInnerBorder(Border innerBorder){
        Afborder.addInnerBorder(this, innerBorder);
    }

    // preferred size
    public AfPanel preferredSize(int w, int h){
        this.setPreferredSize(new Dimension(w,h));
        return this;
    }

    public AfPanel preferredWidth(int w){
        Dimension preferredSize = this.getPreferredSize();
        if (preferredSize == null){
            preferredSize = new Dimension(0,0);
        }
        preferredSize.width = w;
        this.setPreferredSize(preferredSize);
        return this;
    }

    public AfPanel preferredHeight(int h){
        Dimension size = this.getPreferredSize();
        if (size == null){
            size = new Dimension(0,0);
        }

        size.height = h;
        this.setPreferredSize(size);
        return this;
    }

    public void setBgColor(Color color){
        this.bgColor = color;
        this.repaint();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        // 绘制背景色
        if (bgColor != null){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setPaint(bgColor);
            g2d.fillRect(0,0,getWidth(),getHeight());
        }
    }
}
