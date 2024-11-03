package com.wk.swing.JlistConponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JListFrame2 extends JFrame{
     JList<String> nameList = new JList<>();
     DefaultListModel<String> listModel = new DefaultListModel<>();

    public JListFrame2(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 初始化列表控件
        JComponent listPanel = initListView();

        // 工具栏
        JToolBar toolBar = new JToolBar();
        root.add(toolBar, BorderLayout.PAGE_START);
        toolBar.setFloatable(false);

        JButton showBtn = new JButton("查看选中");
        toolBar.add(showBtn);
        showBtn.addActionListener(e -> {
            showSelection();
        });

        JButton removeBtn = new JButton("删除选中");
        toolBar.add(removeBtn);
        removeBtn.addActionListener(e -> {
            removeSelection();
        });

        root.add(listPanel, BorderLayout.CENTER);
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

        JScrollPane listScrollpanel = new JScrollPane(nameList);
        return listScrollpanel;
    }

    // 查看选中项目
    private void showSelection(){
        // 选择选中的项的索引
        int[] indices = nameList.getSelectedIndices();

        System.out.println("共选中: "+indices.length +"  项");
        ListModel<String> model = nameList.getModel();
        for (int index : indices) {
            String item = model.getElementAt(index);
            System.out.println("选中了 :" + item);
        }
    }
    // 删除选中的项
    private void removeSelection(){
        List<String> values = nameList.getSelectedValuesList();
        for (String value : values) {
            listModel.removeElement(value);
        }
    }
}
