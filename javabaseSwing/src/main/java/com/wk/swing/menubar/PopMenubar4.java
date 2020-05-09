package com.wk.swing.menubar;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class PopMenubar4 extends JFrame {

    JPopupMenu popupMenu = new JPopupMenu();

    public PopMenubar4(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 右键菜单
        popupMenu.add(createMenuitem("open", "fileOpen", "ic_open.png"));
        popupMenu.add(createMenuitem("save", "fileSave", "ic_save.png"));
        popupMenu.add(createMenuitem("saveAs", "filesaveas", "ic_saveas.png"));

        // 定制右键菜单的边框
        Border bd1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border bd2 = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        popupMenu.setBorder(BorderFactory.createCompoundBorder(bd1, bd2));

        // 添加右键事件相应，点击右键时，弹出菜单
        root.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * @param text 菜单项文字
     * @param action 菜单项关联的命令
     * @param iconName 图标名字
     * @return
     */
    public JMenuItem createMenuitem(String text, String action, String iconName){
        JMenuItem item = new JMenuItem(text);
        item.setActionCommand(action);
        item.addActionListener(actionListener);

        if (iconName != null){
            String imagePath = "/images/"+ iconName;
            URL url = getClass().getResource(imagePath);
            item.setIcon(new ImageIcon(url));
        }
        // 菜单外观
        item.setUI(new MyMenuItemUI());
        item.setFont(new Font("宋体", Font.PLAIN, 12));
        item.setBorder(BorderFactory.createEmptyBorder(4,2,4,6));
        return item;
    }

    private static class MyMenuItemUI extends BasicMenuItemUI{
        public MyMenuItemUI(){
            this.selectionBackground = new Color(0x91c9F7);
            this.selectionForeground = new Color(0x666666);
        }
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println("执行命令: " + actionCommand);
            if (actionCommand.equalsIgnoreCase("fileOpen")){
                JOptionPane.showMessageDialog(PopMenubar4.this, actionCommand);
            }
        }
    };
}
