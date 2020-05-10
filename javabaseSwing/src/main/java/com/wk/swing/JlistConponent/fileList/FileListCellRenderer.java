package com.wk.swing.JlistConponent.fileList;

import com.wk.swing.layout.AfRowLayout;
import com.wk.swing.panel.AfPanel;

import javax.swing.*;
import java.awt.*;

public class FileListCellRenderer implements ListCellRenderer {
    AfPanel row = new AfPanel();
    JLabel iconField = new JLabel();
    JLabel nameField = new JLabel();
    JLabel timeField = new JLabel();

    private ImageIcon icFolder, icFile;

    public FileListCellRenderer(){
        row.setLayout(new AfRowLayout());
        row.padding(2);
        row.preferredHeight(30);
        row.add(iconField, "20px");
        row.add(nameField, "1w");
        row.add(timeField, "150px");
        // 加载图标
        icFolder = new ImageIcon(getClass().getResource("/images/ic_folder.png"));
        icFile = new ImageIcon(getClass().getResource("/images/ic_file.png"));

        // 去掉粗体显示
        Font font = timeField.getFont().deriveFont(Font.PLAIN);
        nameField.setFont(font);
        timeField.setFont(font);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        FileListItem item = (FileListItem) value;

        // 图标列
        if (item.isDir){
            iconField.setIcon(icFolder);
        }else{
            iconField.setIcon(icFile);
        }

        // 名称和时间
        nameField.setText(item.name);
        timeField.setText(item.lastModified);
        row.setOpaque(true);
        if (isSelected){
            row.setBackground(list.getSelectionBackground());
            row.setForeground(list.getSelectionForeground());
        }else {
            row.setBackground(list.getBackground());
            row.setForeground(list.getForeground());
        }

        return row;
    }
}
