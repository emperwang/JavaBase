package com.wk.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class ReadContent {
    private String filePath = "";

    public ReadContent(String path){
        this.filePath = path;
    }

    public List<String> getContent() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
        List<String> contents = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            // log.info(line);
            contents.add(line);
        }
        reader.close();
        return contents;
    }
}
