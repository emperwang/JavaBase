package com.wk.swing.dialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserDialog extends JFrame{
    private JTextField textField = new JTextField(20);
    public FileChooserDialog(String title){
        super(title);

        JPanel root = new JPanel();
        this.setContentPane(root);
        root.setLayout(new FlowLayout());

        JButton select = new JButton("select");
        root.add(new JLabel("文件"));
        root.add(textField);
        root.add(select);

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (text.equalsIgnoreCase("test1")){
                    test1();
                }
                if (text.equalsIgnoreCase("test2")){
                    test2();
                }
                if (text.equalsIgnoreCase("test3")){
                    test3();
                }
            }
        });
    }

    private void test1() {
        JFileChooser choose = new JFileChooser();

        //FileNameExtensionFilter  文件名过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter("图片文件", "jpg", "png", "jpeg");
        choose.setFileFilter(filter);

        // 显示对话框
        int ret = choose.showOpenDialog(this);
        // 获取用户选择的结果
        if (ret == JFileChooser.APPROVE_OPTION){
            File file = choose.getSelectedFile();
            textField.setText(file.getAbsolutePath());
        }
    }

    // 选择保存文件
    public void test2(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("xml file", "xml");
        chooser.setFileFilter(extensionFilter);

        // 显示对话框
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION){
            // 结果为要保存文件的路径
            File file = chooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
        }
    }

    // 选择目录
    public void test3(){
        JFileChooser chooser = new JFileChooser();

        // 设置模式，仅选择目录
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // 显示对话框
        int ret = chooser.showOpenDialog(this);

        if (ret == JFileChooser.APPROVE_OPTION){
            // 结果为: 已经存在的一个目录
            File file = chooser.getSelectedFile();
            textField.setText(file.getAbsolutePath());
        }
    }

}
