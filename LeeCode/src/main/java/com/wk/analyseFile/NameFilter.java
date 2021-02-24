package com.wk.analyseFile;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author: ekiawna
 * @Date: 2021/2/23 16:31
 * @Description
 */
public class NameFilter implements FilenameFilter  {
    private String pat;
    public NameFilter(String patter){
        this.pat = patter;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (name.contains(pat) && !name.endsWith("tmp")){
            return true;
        }
        return false;
    }
}
