package com.wk.swing.JlistConponent.fileList;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileListFrame extends JFrame{

    DefaultListModel<FileListItem> listModel = new DefaultListModel<>();
    JList<FileListItem> nameList = new JList<>();

    public FileListFrame(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new BorderLayout());

        nameList.setModel(listModel);
        nameList.setCellRenderer(new FileListCellRenderer());

        JScrollPane listScollPanel = new JScrollPane(nameList);

        root.add(listScollPanel, BorderLayout.CENTER);


        loadDir(new File("C:/"));
    }

    private void loadDir(File dir){
        File[] files = dir.listFiles();
        if (files != null){
            for (File file : files) {
                FileListItem item = new FileListItem(file);
                listModel.addElement(item);
            }
        }
    }
}
