package com.java.modules.sink;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Coverter<T> {
    protected String path;
    protected long lineCount;
    protected long size;

    /**
     *  get file path wihich stores the data
     * @param t
     * @return
     */
    public String getPath(T t){
        return  path;
    }

    /**
     *  get line count written
     * @return
     */
    public long getLineCount(){
        return lineCount;
    }

    /**
     * get size written
     * @return
     */
    public long getSize(){
        return size;
    }

    /**
     *  get butes to be written
     * @param t
     * @return
     */
    public abstract byte[] toBytes(T t);
}
