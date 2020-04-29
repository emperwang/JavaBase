package com.wk.swing.combobox;

import javax.swing.*;
import java.awt.*;

public class Combox1 extends JFrame {
    private JComboBox<String> colList;
    private JLabel label;

    public Combox1(String title){
        super(title);

        colList = new JComboBox<>();
        label = new JLabel("请选择要显示的颜色.");

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        contentPane.add(colList);
        contentPane.add(label);
        colList.addItem("红色");
        colList.addItem("蓝色");
        colList.addItem("绿色");

        colList.addActionListener(e -> {
            updateColor();
        });
    }

    private void updateColor(){
        String col = (String) colList.getSelectedItem();
        Color color = null;

        if (col.equalsIgnoreCase("红色")){
            color = Color.RED;
        }else if (col.equalsIgnoreCase("蓝色")){
            color = Color.BLUE;
        }else if (col.equalsIgnoreCase("绿色")){
            color = Color.GREEN;
        }
        label.setForeground(color);
    }
}
