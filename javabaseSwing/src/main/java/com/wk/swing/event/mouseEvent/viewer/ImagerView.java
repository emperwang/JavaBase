package com.wk.swing.event.mouseEvent.viewer;

import com.wk.swing.customcomponent.image.util.AfImageView;
import com.wk.swing.layout.AfRowLayout;
import com.wk.swing.panel.AfPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagerView extends JFrame {

    // 中间区域, 显示大图
    AfImageView canvas = new AfImageView();

    List<ButtonImage> bts = new ArrayList<>();

    public ImagerView(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 中间大图的设置
        canvas.setBackground(Color.WHITE);
        canvas.setScaleType(AfImageView.FIT_CENTER_INSIDE);
        canvas.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.LIGHT_GRAY));
        root.add(canvas, BorderLayout.CENTER);

        // 上面一排显示预览区

        AfPanel thumBar = new AfPanel();
        thumBar.setLayout(new AfRowLayout());
        thumBar.padding(10).preferredHeight(80);
        root.add(thumBar, BorderLayout.PAGE_START);

        // 公用一个事件监听器
        ThumbClickListener clickListener = new ThumbClickListener();

        // 加载images文件夹下的文件
        File file = new File("E:\\code-workSpace\\project-javaBase\\JavaBase\\javabaseSwing\\src\\main\\java\\com\\wk\\swing\\images");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().contains("Screenshot")){
                ButtonImage bimage = new ButtonImage(file1);
                thumBar.add(bimage, "auto");
                bts.add(bimage);

                bimage.setPreferredSize(new Dimension(80,80));
                bimage.setBgColor(Color.WHITE);
                bimage.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));

                bimage.addMouseListener(clickListener);
            }
        }
    }

    private class ThumbClickListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            // 被点击的按钮
            ButtonImage source = (ButtonImage) e.getSource();
            // 点击的按钮: 显示蓝色高亮边框
            // 其他按钮,白色普通边框
            for (ButtonImage bt : bts) {
                if (bt == source){
                    bt.setBorder(BorderFactory.createLineBorder(new Color(0x1E9FFF),3));
                }else{
                    bt.setBorder(BorderFactory.createLineBorder(Color.WHITE,3));
                }
            }
            // 中间区域， 显示大图
            canvas.setImage(source.image);
        }
    }
}
