package com.wk.swing.JlistConponent.custom;

import javax.swing.*;
import java.awt.*;

public class ListCellRenderDemo1 extends JFrame{

    public ListCellRenderDemo1(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        // 准备要显示的数据

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("东伯雪鹰");
        listModel.addElement("余靖秋");
        listModel.addElement("池丘白");
        listModel.addElement("红尘圣主");
        listModel.addElement("血刃神帝");
        listModel.addElement("元初主人");
        listModel.addElement("剑主");
        listModel.addElement("罗城主");
        listModel.addElement("元");

        // 创建列表
        JList<String> nameList = new JList<>();
        nameList.setModel(listModel);
        JScrollPane listScollPanel = new JScrollPane(nameList);

        // 设置单元格绘制器
        /*MyListCellRenderer renderer = new MyListCellRenderer();
        nameList.setCellRenderer(renderer);*/
        // 添加到主界面
        root.add(listScollPanel, BorderLayout.CENTER);
    }

    private static class MyListCellRenderer implements ListCellRenderer{
        JLabel label = new JLabel();

        /**
         * @param list  列表控件
         * @param value 列表项的值
         * @param index 索引
         * @param isSelected 该项是否被选中
         * @param cellHasFocus 该项是否为焦点行(多选模式下,有的被选中,但不是焦点行)
         * @return
         */
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setHorizontalAlignment(SwingConstants.CENTER);
            list.setToolTipText(value.toString());
            label.setBorder(BorderFactory.createEmptyBorder(10,4,10,4));
            label.setOpaque(true);
            if (isSelected){
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }else{
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        }
    }
}
