package com.wk.swing.customcomponent.cusPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class BgImage extends JFrame {

    public BgImage(String title){
        super(title);
        BgPanel panel = new BgPanel();
        // panel.setLayout(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setContentPane(panel);

        panel.add(new JLabel("样例文本"));
        panel.add(new JButton("测试按钮"));

    }

    private class BgPanel extends JPanel{
        Image image = null;

        public BgPanel(){
            URL url = BgImage.class.getResource("/images/bg.jpg");
            try{
                image = ImageIO.read(url);
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

            // 背景
            g.drawImage(image,0,0,width,height,null);

            // 加上一层半透明的遮罩
            g.setColor(new Color(255,255,255,200));
            g.fillRect(0,0,width,height);
        }
    }
}
