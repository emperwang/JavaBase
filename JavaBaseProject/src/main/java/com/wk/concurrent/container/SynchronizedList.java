package com.wk.concurrent.container;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedList {
    public static void main(String[] args) {
        HashMap<Object, Object> map = new HashMap<>();
        Map<Object, Object> synchronizedMap = Collections.synchronizedMap(map);
    }
}
