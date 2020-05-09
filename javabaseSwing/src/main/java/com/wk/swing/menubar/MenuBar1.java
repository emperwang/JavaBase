package com.wk.swing.menubar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar1 extends JFrame {

    public MenuBar1(String title){
        super(title);
        // content panel
        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 添加菜单
        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);

        // 菜单文件
        JMenu file = new JMenu("file");
        menubar.add(file);

        JMenuItem open = new JMenuItem("open");
        JMenuItem save = new JMenuItem("save");
        JMenuItem save_as = new JMenuItem("save as");

        file.add(open);
        file.add(save);
        file.add(save_as);

        JMenuItem quit = new JMenuItem("quit");
        file.addSeparator();
        file.add(quit);

        // 帮助菜单
        JMenu help = new JMenu("help");
        menubar.add(help);

        JMenuItem about = new JMenuItem("about");
        JMenuItem open_help = new JMenuItem("open help");
        help.add(about);
        help.add(open_help);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }
}
