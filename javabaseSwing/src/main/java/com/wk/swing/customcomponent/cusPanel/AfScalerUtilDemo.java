package com.wk.swing.customcomponent.cusPanel;

import com.wk.swing.customcomponent.image.util.AfImageScaler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AfScalerUtilDemo extends JPanel {

    private Image image;

    public AfScalerUtilDemo(){
        try{
            image = imageFromResource("/images/im_j20.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();

        g.clearRect(0,0,width,height);

        if (image != null){
            int imgW = image.getWidth(null);
            int imgH = image.getHeight(null);
            AfImageScaler scaler = new AfImageScaler(imgW, imgH, width, height);

            Rectangle center = scaler.fitCenter();
            g.drawImage(image, center.x, center.y, center.width, center.height,null);
        }
    }

    public Image imageFromResource(String imagePath) throws IOException {
        URL url = this.getClass().getResource(imagePath);
        return ImageIO.read(url);
    }

    public Image imageFromFile(File imageFile) throws IOException {
        return ImageIO.read(imageFile);
    }
}
