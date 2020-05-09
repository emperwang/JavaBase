package com.wk.swing.customcomponent.image.util;

import javax.swing.*;
import java.awt.*;

public class AfImageView  extends JPanel{

    // 缩放类型
    public static final int FIT_XY = 0;
    public static final int FIT_CENTER = 1;
    public static final int FIT_CENTER_INSIDE = 2;

    private Image image;
    private int scaleType = FIT_CENTER;
    private Color bgColor = Color.WHITE;

    public AfImageView(){
        this.setOpaque(false);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public int getScaleType() {
        return scaleType;
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
        repaint();
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();

        g.setColor(bgColor);
        g.fillRect(0,0,width, height);

        if (image != null){
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);
            AfImageScaler scaler = new AfImageScaler(imgW, imgH, width, height);

            Rectangle fitxy = scaler.fitxy();
            if (scaleType ==FIT_CENTER){
                fitxy = scaler.fitCenter();
            }
            if (scaleType == FIT_CENTER_INSIDE){
                fitxy = scaler.fitCenterInside();
            }

            g.drawImage(image,fitxy.x,fitxy.y, fitxy.width, fitxy.height, null);
        }

    }
}
