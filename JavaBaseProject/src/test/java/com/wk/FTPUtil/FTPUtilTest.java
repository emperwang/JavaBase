package com.wk.FTPUtil;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class FTPUtilTest {

    /**
     *  列出目录中的文件
     * @throws IOException
     */
    @Test
    public void listAllFile() throws IOException {
        List<String> notepadPlus = FTPUtil.getFileNameByPath("notepadPlus");
        for (String plus : notepadPlus) {
            System.out.println(plus);
        }
    }

    /**
     *  下载目录中的所有文件到本地
     */
    @Test
    public void downLoadAllFileTest() throws IOException {
        Boolean flag = FTPUtil.downLoadAllFiles("notepadPlus", "H:\\FTPTest");
        System.out.println(flag);
    }

    /**
     *  下载指定的文件到本地
     */
    @Test
    public void downLoadFileByNameTest() throws IOException {
        Boolean flag = FTPUtil.downLoadfile("notepadPlus", "NppFTP-x64.zip", "H:\\FTPTest");
        System.out.println(flag);
    }

    /**
     *  上传文件
     */
    @Test
    public void uploadFile() throws IOException {
        Boolean flag = FTPUtil.uploadLocalFile("notepadPlus", "H:\\FTPTest\\1.txt", "1.txt");
        System.out.println(flag);
    }

    /**
     *  删除文件
     */
    @Test
    public void deleteFile() throws IOException {
        Boolean aBoolean = FTPUtil.deleteFile("notepadPlus/1.txt");
        System.out.println(aBoolean);
    }
}
