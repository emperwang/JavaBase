package com.wk.hdfs.demo;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Demo1 {

    private FileSystem fs = null;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        //可以直接读取classpath下的xxx-site.xml配置文件  并解析其内容
        //封装到conf对象中
        Configuration conf = new Configuration();
        //也可以在代码中对conf的配置进行手动设置,会覆盖配置文件的读取的值
        conf.set("fs.defaultFs","hdfs://192.168.72.11:9000");
        //分根据配置信息,去获取一个具体文件系统的客户端的操作实例对象
        fs = FileSystem.get(new URI("hdfs://192.168.72.11:9000"),conf,"root");
    }

    @Test
    public void listFiles() throws IOException {
        //获取远程根目录下的文件
        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
        //遍历获取到的文件
        while (files.hasNext()){
            //获取文件
            LocatedFileStatus file = files.next();
            Path path = file.getPath();
            System.out.println("path = " + path.toString());
            System.out.println("filename = " + path.getName());
        }
    }

    /**
     *  测试上传文件到hdfs
     *  比较底层的写法
     * @throws IOException
     */
    @Test
    public void upload() throws IOException {
        //要上传的目的目录
        Path dest = new Path("hdfs://192.168.72.11:9000/aa/testupload");
        //创建到目的目录的流
        FSDataOutputStream fsDataOutputStream = fs.create(dest);
        //要上传的本地文件
        FileInputStream ins = new FileInputStream("d:/test.xls");
        //拷贝文件
        IOUtils.copy(ins,fsDataOutputStream);
    }

    /**
     * 上传文件, 封装好的方法
     */
    @Test
    public void upload2() throws IOException {
        fs.copyFromLocalFile(new Path("d:/test.xls"),new Path("hdfs://192.168.72.11:9000/aa/testupload2"));
    }

    /**
     * 当使用函数 fs.copyToLocalFile(new Path("hdfs://192.168.72.11:9000/aa/testupload2"),new Path("d:/testtesttest.xls")); 报错如下:
     * 解决方法:加两个参数
     *copyToLocalFile(false,new Path("hdfs://192.168.72.11:9000/aa/testupload2"),new Path("d:/testtesttest.xls"),true)
     * 第一个false: 不删除源文件
     * 最后true:  使用本地文件系统
     * java.io.IOException: (null) entry in command string: null chmod 0644 D:\testtesttest.xls
     at org.apache.hadoop.util.Shell$ShellCommandExecutor.execute(Shell.java:773)
     at org.apache.hadoop.util.Shell.execCommand(Shell.java:869)
     at org.apache.hadoop.util.Shell.execCommand(Shell.java:852)
     at org.apache.hadoop.fs.RawLocalFileSystem.setPermission(RawLocalFileSystem.java:733)
     at org.apache.hadoop.fs.RawLocalFileSystem$LocalFSFileOutputStream.<init>(RawLocalFileSystem.java:225)
     at org.apache.hadoop.fs.RawLocalFileSystem$LocalFSFileOutputStream.<init>(RawLocalFileSystem.java:209)
     at org.apache.hadoop.fs.RawLocalFileSystem.createOutputStreamWithMode(RawLocalFileSystem.java:307)
     at org.apache.hadoop.fs.RawLocalFileSystem.create(RawLocalFileSystem.java:296)
     at org.apache.hadoop.fs.RawLocalFileSystem.create(RawLocalFileSystem.java:328)
     at org.apache.hadoop.fs.ChecksumFileSystem$ChecksumFSOutputSummer.<init>(ChecksumFileSystem.java:398)
     at org.apache.hadoop.fs.ChecksumFileSystem.create(ChecksumFileSystem.java:461)
     at org.apache.hadoop.fs.ChecksumFileSystem.create(ChecksumFileSystem.java:440)
     at org.apache.hadoop.fs.FileSystem.create(FileSystem.java:911)
     at org.apache.hadoop.fs.FileSystem.create(FileSystem.java:892)
     at org.apache.hadoop.fs.FileSystem.create(FileSystem.java:789)
     at org.apache.hadoop.fs.FileUtil.copy(FileUtil.java:365)
     at org.apache.hadoop.fs.FileUtil.copy(FileUtil.java:338)
     at org.apache.hadoop.fs.FileUtil.copy(FileUtil.java:289)
     at org.apache.hadoop.fs.FileSystem.copyToLocalFile(FileSystem.java:2034)
     at org.apache.hadoop.fs.FileSystem.copyToLocalFile(FileSystem.java:2003)
     at org.apache.hadoop.fs.FileSystem.copyToLocalFile(FileSystem.java:1979)
     at com.wk.hdfs.demo.Demo1.downLoadFile(Demo1.java:69)
     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     at java.lang.reflect.Method.invoke(Method.java:498)
     at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
     at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
     at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
     at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
     at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
     at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
     at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
     at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
     at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
     at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
     at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
     at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
     at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
     at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
     at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
     at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
     at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
     at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
     at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
     * @throws IOException
     */
    @Test
    public void downLoadFile() throws IOException {
        fs.copyToLocalFile(false,new Path("hdfs://192.168.72.11:9000/aa/testupload2"),new Path("d:/testtesttest.xls"),true);
    }

    /**
     * 删除文件
     * @throws IOException
     */
    @Test
    public void rmFile() throws IOException {
        fs.delete(new Path("/aa"),true);
    }

}
