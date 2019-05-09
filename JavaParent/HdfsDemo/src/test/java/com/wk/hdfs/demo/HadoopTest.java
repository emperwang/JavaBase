package com.wk.hdfs.demo;

import com.wk.hdfs.hadoop.SimpleHdfsSingleton;
import org.apache.hadoop.fs.FileStatus;

public class HadoopTest {
    public static void main(String[] args) {
        SimpleHdfsSingleton hdfsSingleton = SimpleHdfsSingleton.hdfs();

        FileStatus[] fileStatuses = hdfsSingleton.listStatus("/");
        for(int i=0;i<fileStatuses.length;i++){
            System.out.println(fileStatuses[i].getPath());
        }
    }
}
