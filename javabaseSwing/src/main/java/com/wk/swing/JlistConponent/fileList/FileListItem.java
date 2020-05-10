package com.wk.swing.JlistConponent.fileList;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileListItem {

    public String name;
    public File file;
    public boolean isDir;
    public String lastModified;

    public FileListItem(File f){
        this.file = f;
        this.name = f.getName();
        this.isDir = f.isDirectory();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.lastModified = format.format(f.lastModified());
    }
}
