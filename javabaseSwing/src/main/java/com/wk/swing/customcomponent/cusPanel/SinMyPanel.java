package com.wk.swing.customcomponent.cusPanel;

import javax.swing.*;
import java.awt.*;

public class SinMyPanel extends JPanel {

    public int grain = 3;   // 线条的精细度
    public int range = 50;  // 高度
    private int period = 100; // x轴, 每100像素一个周期

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();

        g.clearRect(0,0,width,height);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);

        // 绘制中线
        int center = height / 2;
        g.setColor(Color.BLUE);
        g.drawLine(0,center,width, center);

        // 正弦曲线
        int x1 = 0;
        int y1 = 0;

        for (int i =0; i < width; i+=grain){
            int x2 = i;
            double angle = 2 * Math.PI * x2/ period;

            int y2 = (int)(range * Math.sin(angle));

            g.drawLine(x1, center+y1, x2, center+y2);

            x1 = x2;
            y1 = y2;
        }
    }
}
