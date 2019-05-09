package com.wk.interpolation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringCombiner {
    private Map<String,Object> bindings;
    private LinkedHashMap<String,VarPart> parts;;
    private final Multimap<String,String> nameKeys;

    private StringCombiner(LinkedHashMap<String,VarPart> parts){
        this.bindings = new HashMap<>();
        this.parts = parts;
        this.nameKeys = createNameKeysMap();
    }

    private ArrayListMultimap<String,String> createNameKeysMap(){
        ArrayListMultimap<String,String> map = ArrayListMultimap.create();
        for(VarPart v:parts.values()){
            map.put(v.getName(),v.getKey());
        }
        return map;
    }

    public String combine(){
        StringBuilder stringBuilder = new StringBuilder(1024);
        for(VarPart part:parts.values()){
            String v = part.getValue();
            if(StringUtils.isNotEmpty(v)){
                stringBuilder.append(v);
            }
        }
        return stringBuilder.toString();
    }

    public void bind(String name,Object value){
        bindings.put(name,value);
        for(String k:nameKeys.get(name)){
            parts.get(k).value(bindings);
        }
    }

    public void bindPairs(Object ... os){
        int l = os.length >> 1;
        for(int i=0;i<1;i++){
            int ii = i<<1;
            Object n = os[ii];
            if(n == null) continue;
            bind(n.toString(),os[ii+1]);
        }
    }

    public void clear(String name){
        bind(name,null);
    }

    public boolean isChanged(String name){
        for(String k:nameKeys.get(name)){
            if(parts.get(k).isChanged()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static StringCombiner create(LinkedHashMap<String,VarPart> parts){
        if(parts == null){
            throw new NullPointerException("parameter parts can't be null");
        }
        return new StringCombiner(parts);
    }
}
