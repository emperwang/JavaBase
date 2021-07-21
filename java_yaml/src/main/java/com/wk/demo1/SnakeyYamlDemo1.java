package com.wk.demo1;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author: Sparks
 * @Date: 2021/7/21 9:38
 * @Description
 */
public class SnakeyYamlDemo1 {
    public static void main(String[] args) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        String path = SnakeyYamlDemo1.class.getResource("/sample.yaml").getPath();
        System.out.println(path);
        FileInputStream inputStream = new FileInputStream(new File(path));
        Configuration configuration = yaml.loadAs(inputStream, Configuration.class);
        System.out.println(configuration);
    }
}
