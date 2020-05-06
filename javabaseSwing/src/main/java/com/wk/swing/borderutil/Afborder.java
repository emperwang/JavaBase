package com.wk.swing.borderutil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

// 用于快速设置边框和边距
public class Afborder {

    // 设置内边距
    public static void addPadding(JComponent c, int size){
        addPadding(c, size, size, size, size);
    }

    // 设置内间距
    public static void addPadding(JComponent c, int top, int left, int bottom, int right){
        Border border = BorderFactory.createEmptyBorder(top, left, bottom, right);
        addInnerBorder(c, border);
    }
    // 设置外间距
    public static void addMargin(JComponent c, int size){
        addMargin(c, size,size,size,size);
    }

    // 设置外间距
    public static void addMargin(JComponent c, int top, int left, int bottom, int right){
        Border border = BorderFactory.createEmptyBorder(top, left, bottom, right);
        addOuterBorder(c, border);
    }

    // 附加一个外边框
    public static void addOuterBorder(JComponent c, Border outerBorder){
        Border border = c.getBorder();
        if (border != null){
            border = BorderFactory.createCompoundBorder(outerBorder, border);
            c.setBorder(border);
        }else{
            c.setBorder(outerBorder);
        }
    }

    // 附件一个内边框
    public static void addInnerBorder(JComponent c, Border innerBorder){
        Border border = c.getBorder();
        if (border != null){ // 和原来border复合
            Border border1 = BorderFactory.createCompoundBorder(border, innerBorder);
            c.setBorder(border1);
        }else {
            c.setBorder(innerBorder);
        }
    }



}

