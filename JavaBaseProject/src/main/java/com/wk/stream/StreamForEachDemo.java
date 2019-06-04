package com.wk.stream;

import java.util.stream.IntStream;

/**
 * foreach方式demo
 */
public class StreamForEachDemo {
    public static void main(String[] args) {
        IntStream.of(1,2,3,4,5).forEach(System.out::print);
    }
}
