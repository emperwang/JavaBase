package com.wk.swing.menubar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class PopMenuBar3 extends JFrame {

    JPopupMenu popupMenu = new JPopupMenu();

    public PopMenuBar3(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 右键菜单
        popupMenu.add(createMenuItem("open","fileOpen","ic_open.png"));
        popupMenu.add(createMenuItem("save","fileSave","ic_save.png"));
        popupMenu.add(createMenuItem("saveAs","fileSaveAs","ic_saveas.png"));

        root.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public JMenuItem createMenuItem(String text, String action, String iconName){
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setActionCommand(action);
        menuItem.addActionListener(actionListener);
        if (iconName != null){
            String imagePath = "/images/"+iconName;
            URL url = getClass().getResource(imagePath);
            menuItem.setIcon(new ImageIcon(url));
        }
        return menuItem;
    }

    public ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println("执行命令: "+ actionCommand);
            if (actionCommand.equalsIgnoreCase("fileOpen")){
                JOptionPane.showMessageDialog(PopMenuBar3.this,actionCommand);
            }
        }
    };
}
