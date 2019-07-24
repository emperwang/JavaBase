package com.wk.FTPUtil;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class SFTPUtilTest {

    @Test
    public void listFileTest() throws SftpException, JSchException {
        Vector vector = SftpUtil.listFiles("/");
        vector.forEach(System.out::println);
    }
    @Test
    public void downLoadFileTest() throws FileNotFoundException, SftpException, JSchException {
        SftpUtil.downLoad("/","powerDesign.zip","H:/FTPTest/powerDesign.zip");
    }
    @Test
    public void uploadFileTest() throws IOException, SftpException, JSchException {
        SftpUtil.upload("","/","powerDesign.zip","H:\\PowerDesign16.5.zip");
    }
    @Test
    public void deleteServerFileTest() throws JSchException, SftpException {
        SftpUtil.delete("/","powerDesign.zip");
    }
}
