package com.wk.swing.event.mouseEvent.drawLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class HandlerDrawView extends JPanel {

    // 一条自由曲线由若干个点连接而成
    List<Point> curve = new ArrayList<>();

    List<List<Point>> olds = new ArrayList<>();

    // 当鼠标按下时， drawing为true， 表示正在绘制，鼠标抬起时结束
    boolean drawing = false;

    public HandlerDrawView(){
        // 添加两个listener
        MyMouseListener listener = new MyMouseListener();
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = this.getWidth();
        int height = this.getHeight();

        g.clearRect(0,0,width,height);

        // 设置线条颜色
        g.setColor(Color.BLUE);
        paintOlds(g);
        // 至少有两个点才能绘制线条
        if(curve.size() < 2) return;

        // 绘制线条，把各个点连接起来
        Point point = curve.get(0);
        for (int i = 1; i < this.curve.size(); i++){
            Point p2 = curve.get(i);
            g.drawLine(point.x, point.y, p2.x, p2.y);
            point = p2;
        }
    }

    private void paintOlds(Graphics g){
        // 至少有两个点才能绘制线条
        if(olds.size() <= 0) return;
        for (int j = 0; j < olds.size(); j++) {
            List<Point> points = olds.get(j);
            // 绘制线条，把各个点连接起来
            Point point = points.get(0);
            for (int i = 1; i < points.size(); i++){
                Point p2 = points.get(i);
                g.drawLine(point.x, point.y, p2.x, p2.y);
                point = p2;
            }
        }

    }

    private class MyMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println();
            System.out.println("mouse pressed..");
            drawing = true;
            curve = new ArrayList<>();
            curve.add(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("Mouse released");
            drawing = false;
            olds.add(curve);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println("mouse Dragged~~");
            curve.add(e.getPoint());
            repaint();
        }
    }
}
