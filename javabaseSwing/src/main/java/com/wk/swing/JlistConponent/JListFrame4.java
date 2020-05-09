package com.wk.swing.JlistConponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class JListFrame4 extends JFrame {

    JList<String> nameList = new JList<>();
    DefaultListModel<String> nameListModel = new DefaultListModel<>();

    public JListFrame4(String title){
        super(title);
        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        JComponent listPanel = initListView();
        root.add(listPanel, BorderLayout.CENTER);
    }

    private JComponent initListView() {
        nameListModel.addElement("东伯雪鹰");
        nameListModel.addElement("余靖秋");
        nameListModel.addElement("东伯烈");
        nameListModel.addElement("墨阳瑜");
        nameListModel.addElement("铜三");
        nameListModel.addElement("宗凌");
        nameListModel.addElement("东伯青");
        nameListModel.addElement("东伯玉");
        nameListModel.addElement("墨阳瑜");
        nameListModel.addElement("池丘白");
        nameListModel.addElement("贺山主");
        nameListModel.addElement("司空阳");
        nameListModel.addElement("晁青");
        nameListModel.addElement("陈宫主");
        nameListModel.addElement("红尘圣主");
        nameListModel.addElement("血刃神帝");
        nameListModel.addElement("元初主人");
        nameListModel.addElement("魔祖");
        nameListModel.addElement("剑主");
        nameListModel.addElement("天愚");
        nameListModel.addElement("应山氏");
        nameListModel.addElement("南云");
        nameListModel.addElement("夏家");
        nameListModel.addElement("樊家");
        nameListModel.addElement("罗城主");
        nameListModel.addElement("元");

        nameList.setModel(nameListModel);
        nameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);;

        nameList.setVisibleRowCount(-1);
        //nameList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        //nameList.setLayoutOrientation(JList.VERTICAL);
        nameList.setLayoutOrientation(JList.VERTICAL_WRAP);

        nameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (! e.getValueIsAdjusting()){
                    selectChanged();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(nameList);
        return scrollPane;
    }

    private void selectChanged(){
        String item = nameList.getSelectedValue();
        System.out.println("选中了 :" + item);
    }
}
