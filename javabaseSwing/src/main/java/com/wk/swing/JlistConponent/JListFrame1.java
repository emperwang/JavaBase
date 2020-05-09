package com.wk.swing.JlistConponent;

import javax.swing.*;
import java.awt.*;

public class JListFrame1 extends JFrame {

    public JListFrame1(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 存在list的panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(30,30));
        panel.setLayout(new BorderLayout());
        root.add(panel, BorderLayout.CENTER);

        // 准备要显示的数据 model
        DefaultListModel<String> listModel = new DefaultListModel<>();

        listModel.addElement("东伯雪鹰");
        listModel.addElement("余清秋");
        listModel.addElement("池丘白");
        listModel.addElement("红尘圣主");
        listModel.addElement("血妊娠第");
        listModel.addElement("元初主人");
        listModel.addElement("剑主");
        listModel.addElement("罗城主");
        listModel.addElement("元");

        // 创建列表
        JList<String> nameList = new JList<>();
        nameList.setModel(listModel);
        nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 把列表放在scoll pane里

        JScrollPane listScollpanel = new JScrollPane(nameList);

        // 添加到主界面
        panel.add(listScollpanel, BorderLayout.CENTER);
    }
}
