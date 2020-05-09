package com.wk.swing.customcomponent.cusPanel;

import javax.swing.JPanel;
import java.awt.*;

public class MyPanelFirst extends JPanel {

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 此框架的宽度和高度
        int width = this.getWidth();
        int height = this.getHeight();
        System.out.println("width="+width+", height="+height);
        // 清除显示
        g.clearRect(0,0, width, height);
        // 设置背景颜色
        g.setColor(new Color(0xFF0000));

        // 填充一个矩形
        //g.fillRect(0,0,width,height);
        // 绘制
        g.drawRect(0,0,100,100);

        // 绘制线条
        g.drawLine(0,0,200,200);
    }

    /*@Override
    public void paintComponents(Graphics g) {
        // 此方法由swing框架调用执行
        // 当窗口被显示时, swing框架内部会调用每个控件的paintComponents来进行绘制
    }*/
}
