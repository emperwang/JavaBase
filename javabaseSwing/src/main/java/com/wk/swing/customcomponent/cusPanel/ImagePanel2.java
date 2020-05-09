package com.wk.swing.customcomponent.cusPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImagePanel2 extends JPanel {

    private Image image = null;
    public ImagePanel2(){
        try{
            image = ImageFromSource("/images/im_j20.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("painting.");

        int width = this.getWidth();
        int height = this.getHeight();

        g.clearRect(0,0,width,height);

        if (image != null){
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);

            /**
             * (fitW, fitH): 要求保持长宽比，并且在目标矩形内
             * 要求1：图形不能超出控制范围
             * 要求2：图形比按原始比例显示
             */

            // 首先以窗口的宽度作为图片的宽度，按比例绘制图片
            int fitW = width;
            int fitH = width * imgH / imgW;
            // 若图片高度fitH 超过了窗口高度,就以窗口高度为图片高度,按比例绘制
            if (fitH > height){
                fitH = height;
                fitW = height * imgW / imgH;
            }

            // 绘制图片
            //int x = (width - fitW)/2;
            //int y = (height - fitH)/2;
            //g.drawImage(image,x,y,fitH,fitW,null);
            g.drawImage(image,0,0,fitH,fitW,null);
        }
    }

    public Image ImageFromSource(String imagePath) throws IOException {
        URL url = this.getClass().getResource(imagePath);
        BufferedImage image = ImageIO.read(url);
        return image;
    }

    public Image ImageFromFile(File imageFile) throws IOException {
        return ImageIO.read(imageFile);
    }
}
