package com.wk.swing.icon;

import com.wk.swing.borderutil.Afborder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class icsondemo3 extends JFrame {

    public icsondemo3(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        //工具栏  添加三个按钮
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout());
        root.add(toolbar, BorderLayout.PAGE_START);

        JButton b1 = createButton("/images/ic_open.png");
        JButton b2 = createButton("/images/ic_save.png");
        JButton b3 = createButton("/images/ic_print.png");
        toolbar.add(b1);
        toolbar.add(b2);
        toolbar.add(b3);

        // 中间区域, 添加一个文本输入控件
        JTextArea content = new JTextArea();
        root.add(content, BorderLayout.CENTER);

        content.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 4));;
    }

    public JButton createButton(String iconPath){
        // 加载资源
        URL url = getClass().getResource(iconPath);
        Icon icon = new ImageIcon(url);

        // 设置图标
        JButton b1 = new JButton();
        b1.setIcon(icon);

        // 其他设置
        b1.setContentAreaFilled(false);
        b1.setFocusPainted(false);

        // 设置border
        b1.setBorder(BorderFactory.createLineBorder(Color.gray, 4));
        Afborder.addPadding(b1,4);

        // 创建鼠标事件监听: 鼠标移入时, 更换border为蓝色
        b1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
               b1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
               Afborder.addPadding(b1, 4);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b1.setBorder(BorderFactory.createLineBorder(Color.gray, 4));
                Afborder.addPadding(b1, 4);
            }
        });

        return b1;
    }
}
