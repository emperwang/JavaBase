package com.wk.data;

import java.io.Closeable;

public interface DataWriter extends Closeable{

    void writeBytes(byte b[]);
    void write(byte b[], int offet, int length);
}
