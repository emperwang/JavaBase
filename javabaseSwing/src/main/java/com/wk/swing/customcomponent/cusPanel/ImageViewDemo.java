package com.wk.swing.customcomponent.cusPanel;

import com.wk.swing.customcomponent.image.util.AfImageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ImageViewDemo extends JFrame {

    public ImageViewDemo(String title){
        super(title);
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        this.setContentPane(root);

        // 添加图片显示控件
        AfImageView view = new AfImageView();
        root.add(view, BorderLayout.CENTER);
        // 设置缩放类型
        view.setScaleType(AfImageView.FIT_CENTER);

        // 设置背景色
        view.setBgColor(new Color(255,255,255,0));
        // 加载图片
        try{
            Image image = imageFromResource("/images/im_j20.jpg");
            view.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image imageFromResource(String imagepath) throws IOException {
        URL url = this.getClass().getResource(imagepath);

        return ImageIO.read(url);
    }
}
