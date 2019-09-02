package com.wk.JSON;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 *  把json串中的key转换为小写
 */
@Slf4j
public class JsonLowKeyUtil {

    public static final ValueFilter data_filter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (value != null){
                if (value instanceof Date){
                    return value;
                }
            }
            return value;
        }
    };

    /**
     *  字符串转换为jsonObjet对象
     * @param jsonStr
     * @return
     */
    public static JSONObject toJSONObject(String jsonStr){
        return JSON.parseObject(jsonStr);
    }

    public  static JSONObject toJSONObjectLower(String jsonStr){
        JSONObject json = toJSONObject(jsonStr);
        if (json == null){
            return null;
        }
        return JSONObjectKeyLower(json);
    }

    /**
     *  把json对象中的key转换为小写
     * @param jsonObject
     * @return
     */
    public static JSONObject JSONObjectKeyLower(JSONObject jsonObject){
        JSONObject json = new JSONObject();
        for (String key : jsonObject.keySet()){
            Object o = json.get(key);
            key = key.toLowerCase();
            if (o == null){
                json.put(key,o);
            }else if (o instanceof JSONObject){
                json.put(key,JSONObjectKeyLower((JSONObject)o));
            }else if (o instanceof JSONArray){
                json.put(key,JSONArrayKeyLower((JSONArray)o));
            }else {
                json.put(key,o);
            }
        }
        return json;
    }

    /**
     *
     * @param jarr
     * @return
     */
    public static JSONArray JSONArrayKeyLower(JSONArray jarr){
        JSONArray arrs = new JSONArray();
        for (Object o : arrs){
            if ( o == null){
                arrs.add(o);
            }else if (o instanceof JSONObject){
                arrs.add(JSONObjectKeyLower((JSONObject)o));
            }else if (o instanceof JSONArray){
                arrs.add(JSONArrayKeyLower((JSONArray)o));
            }else {
                arrs.add(o);
            }
        }
        return arrs;
    }
}
