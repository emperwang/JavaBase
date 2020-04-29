package com.wk.swing.layout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CardLayoutDemo extends JFrame {
    private JComboBox<String> comboBox;
    private JPanel card;

    public CardLayoutDemo(String title){
        super(title);
        comboBox = new JComboBox<>();
        card = new JPanel();
        Container contentPane = getContentPane();
        CardLayout cardLayout = new CardLayout(5,5);
        contentPane.setLayout(new BorderLayout());

        contentPane.add(comboBox, BorderLayout.NORTH);
        contentPane.add(card, BorderLayout.CENTER);

        comboBox.addItem("第一个面板");
        comboBox.addItem("第二个面板");

        JPanel jPanel1 = new JPanel();
        jPanel1.add(new JButton("按钮1"));
        jPanel1.add(new JButton("按钮2"));
        jPanel1.add(new JButton("按钮3"));

        JPanel jPanel2 = new JPanel();
        jPanel2.add(new JTextField(16));

        card.setLayout(cardLayout);
        card.add(jPanel1,"button");
        card.add(jPanel2, "text");


        comboBox.addActionListener(e -> {
            selectCard();
        });
    }

    public void selectCard(){
        int index = comboBox.getSelectedIndex();
        CardLayout layout = (CardLayout) card.getLayout();
        if (index == 0) {
            layout.show(card, "button");
        }else {
            layout.show(card, "text");
        }
    }
}

