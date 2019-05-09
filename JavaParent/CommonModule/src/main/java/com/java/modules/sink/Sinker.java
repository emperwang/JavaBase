package com.java.modules.sink;

public interface Sinker<T> {
    boolean drain(T t);
}
