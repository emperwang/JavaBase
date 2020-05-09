package com.wk.swing.customcomponent.cusPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImagePanel extends JPanel{

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();

        g.clearRect(0,0,width,height);

        try{
            Image image = imageFromResource("/images/im_j20.jpg");

            g.drawImage(image,0,0,width,height, null);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Image imageFromResource(String imagePath) throws IOException {
        URL url = this.getClass().getResource(imagePath);
        BufferedImage image = ImageIO.read(url);
        return image;
    }

    private Image imageFromFile(File imageFile) throws IOException {
        return ImageIO.read(imageFile);
    }
}
