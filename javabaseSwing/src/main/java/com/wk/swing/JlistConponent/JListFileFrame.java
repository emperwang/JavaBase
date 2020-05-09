package com.wk.swing.JlistConponent;

import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
import com.wk.swing.layout.AfRowLayout;
import com.wk.swing.panel.AfPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class JListFileFrame extends JFrame {
    private JList<ListItem> fileList = new JList<>();
    DefaultListModel<ListItem>  listModel = new DefaultListModel<>();
    JTextField dirField = new JTextField(20);

    public JListFileFrame(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);;
        root.setLayout(new BorderLayout());

        //  初始化列表控件
        JComponent listPanel = initListView();
        root.add(listPanel, BorderLayout.CENTER);

        AfPanel localtionBar = new AfPanel();
        root.add(localtionBar, BorderLayout.PAGE_START);

        localtionBar.setLayout(new AfRowLayout(10));
        localtionBar.padding(5).preferredHeight(36);
        localtionBar.add(dirField, "1w");

        JButton browseBtn = new JButton("浏览");
        localtionBar.add(browseBtn, "auto");
        browseBtn.addActionListener(e -> {
            selectFolderDialog();
        });
        loadDir(new File("C:/"));
    }

    private void selectFolderDialog(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("请选择文件夹");
        int ret = chooser.showOpenDialog(this);
        if (ret == chooser.APPROVE_OPTION){
            File dir = chooser.getSelectedFile();
            loadDir(dir);
        }
    }

    private JComponent initListView(){
        fileList.setModel(listModel);
        fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);;
        fileList.setVisibleRowCount(-1);
        fileList.setLayoutOrientation(JList.VERTICAL_WRAP);

        // 右键支持
        fileList.addMouseListener(new FileListMouseListener());

        JScrollPane scrollPane = new JScrollPane(fileList);
        return scrollPane;
    }

    // 显示目录下的文件
    private void loadDir(File dir){
        System.out.println("loadDir..");
        // 显示当路径
        dirField.setText(dir.getAbsolutePath());
        listModel.clear();

        // 添加一个 返回上级目录
        File parent = dir.getParentFile();
        // 到达顶层目录后，getParent返回null
        if (parent != null){
            ListItem item = new ListItem(parent);
            item.name = "返回上级目录..";
            item.isDir = true;
            listModel.addElement(item);
        }

        // 列出目录的子文件或文件夹
        // 当目录不为空时, 返回值files为File[0], 即长度为0的数据
        // 当目录不可访问时, 返回值files为null
        File[] files = dir.listFiles();
        if (files != null){
            for (File file : files) {
                ListItem item = new ListItem(file);
                listModel.addElement(item);
            }
        }
    }

    private void showContentMenu(MouseEvent e){
        // 右键显示
    }

    // 双击目录则打开， 双击文件则无反应
    private void fileDoubleClicked(MouseEvent e){
        int idx = fileList.locationToIndex(e.getPoint());
        if (idx < 0) return;

        ListItem item = fileList.getModel().getElementAt(idx);
        if (item.isDir){
            loadDir(item.file);
        }
    }
    private class FileListMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3){
                showContentMenu(e);
            }
            if (e.getButton() == MouseEvent.BUTTON1 &&
                    e.getClickCount() == 2){
                fileDoubleClicked(e);
            }
        }
    }

    private static class ListItem{
        public String name;
        public File file;
        public boolean isDir;

        public ListItem(File f){
            this.file = f;
            this.name = f.getName();
            this.isDir = f.isDirectory();
        }

        @Override
        public String toString() {
            if (isDir){
                return "+ "+name;
            }else{
                return " "+ name;
            }
        }
    }
}
