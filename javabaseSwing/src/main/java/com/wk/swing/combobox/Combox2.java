package com.wk.swing.combobox;

import javax.swing.*;
import java.awt.*;

public class Combox2 extends JFrame {
    private JComboBox<ListOption> comboBox;
    private JLabel label;

    public Combox2(String title){
        super(title);
        comboBox = new JComboBox<>();
        label = new JLabel("选择要显示的颜色");

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(comboBox);
        contentPane.add(label);

        comboBox.addItem(new ListOption("黄色", Color.YELLOW));
        comboBox.addItem(new ListOption("蓝色", Color.BLUE));
        comboBox.addItem(new ListOption("绿色", Color.GREEN));

        comboBox.addActionListener(e -> {
            updateColor();
        });
    }

    public void updateColor(){
        ListOption col = (ListOption) comboBox.getSelectedItem();
        Color color = col.color;

        label.setForeground(color);
    }
}

class ListOption{
    public String text;
    public Color color;

    public ListOption(String text, Color color){
        this.text = text;
        this.color = color;
    }

    @Override
    public String toString() {
        return  text ;
    }
}
