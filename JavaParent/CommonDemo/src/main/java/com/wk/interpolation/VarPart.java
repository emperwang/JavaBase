package com.wk.interpolation;

import java.util.Map;

public interface VarPart {
    String getKey();
    String getName();
    String getValue();
    boolean isChanged();
    String value(Map<String,Object> bindings);
}
