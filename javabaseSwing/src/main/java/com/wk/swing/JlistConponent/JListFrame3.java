package com.wk.swing.JlistConponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JListFrame3 extends JFrame{

    JList<String> nameList = new JList<>();
    DefaultListModel<String> listModel = new DefaultListModel<>();

    public JListFrame3(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        JComponent listPane = initListView();

        root.add(listPane, BorderLayout.CENTER);

    }
    // JComponent: 是所有的swing控件的父类

    private JComponent initListView(){
        listModel.addElement("东伯雪鹰");
        listModel.addElement("余清秋");
        listModel.addElement("池丘白");
        listModel.addElement("红尘圣主");
        listModel.addElement("血妊娠第");
        listModel.addElement("元初主人");
        listModel.addElement("剑主");
        listModel.addElement("罗城主");
        listModel.addElement("元");

        nameList.setModel(listModel);
        nameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // 列表项选中事件
        nameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // e.getValueIsAdjusting() 为true 表示变化尚未结束
                if (! e.getValueIsAdjusting()){
                    selectChanged();
                }
            }
        });

        // 鼠标右键事件
        nameList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showContentMenu(e);
                }
            }

        });

        JScrollPane listScrollpanel = new JScrollPane(nameList);
        return listScrollpanel;
    }

    private void selectChanged(){
        String selectedValue = nameList.getSelectedValue();
        System.out.println("选中: "+ selectedValue);
    }

    public void showContentMenu(MouseEvent e){
        // 先检查点中的时那一项
        // 根据鼠标点钟的位置 e.getPoint, 计算被点中的是那一项
        int index = nameList.locationToIndex(e.getPoint());
        if (index < 0) return;

        // 选中该项(默认的,右键不会选中,所偶一这里要手工选中一下)
        nameList.setSelectedIndex(index);

        // 弹出右键菜单
        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("查看详情");
        JMenuItem item2 = new JMenuItem("关注");
        menu.add(item);
        menu.add(item2);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

}
