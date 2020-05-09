package com.wk.swing.menubar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class menuBar2 extends JFrame {


    public menuBar2(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());


        // 创建工具栏
        JToolBar toolbar = new JToolBar();
        toolbar.setPreferredSize(new Dimension(40,40));
        toolbar.setFloatable(false);// 工具栏固定， 不允许浮动
        root.add(toolbar, BorderLayout.PAGE_START);

        // 向工具栏添加按钮
        toolbar.add(toolButton("ic_open.png", "fileOpen", "open"));
        toolbar.add(toolButton("ic_save.png", "fileSave", "save"));
        toolbar.add(toolButton("ic_saveas.png", "fileSaveAs", "saveAs"));
        toolbar.addSeparator();
        toolbar.add(toolButton("ic_help.png","fileHelp", "help"));
    }

    public JButton toolButton(String imagename, String action, String tooltip){
        String imagePath = "/images/"+imagename;
        URL url = getClass().getResource(imagePath);

        // create button
        JButton button = new JButton();
        button.setActionCommand(action);
        button.setToolTipText(tooltip);
        button.setIcon(new ImageIcon(url));
        button.setPreferredSize(new Dimension(40,40));
        button.setFocusPainted(false);

        button.addActionListener(actionListener);
        return button;
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println("指定命令: " + actionCommand);
        }
    };
}

