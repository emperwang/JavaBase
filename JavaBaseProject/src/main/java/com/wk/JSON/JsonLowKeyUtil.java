package com.wk.JSON;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 *  把json串中的key转换为小写
 */
@Slf4j
public class JsonLowKeyUtil {
    /**
     *  把javabean转换为json
     * @param object
     * @return
     */
    public static String beanToJson(Object object){
        try {
            return JSON.toJSONString(object);
        }catch (Exception e){
            log.error("beanToJson exception:{}",ExUtil.buildErrorMessage(e));
        }
        return "";
    }

    /**
     *  把json转换为bean对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String jsonStr,Class<T> clazz){
        if (jsonStr != null && jsonStr.length()>0){
            try {
                T t = JSON.parseObject(jsonStr, clazz);
                return t;
            }catch (Exception e){
                log.error("jsonToBean exception, msg:{}",ExUtil.buildErrorMessage(e));
            }
        }
        return null;
    }

    /**
     *  把json数组转换为对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToBeanList(String jsonStr, Class<T> clazz){
        if (jsonStr != null && jsonStr.length()>0) {
            try {
                List<T> list = JSON.parseArray(jsonStr, clazz);
                return list;
            }catch (Exception e){
                log.error("jsonToBeanList exception, msg:{}",ExUtil.buildErrorMessage(e));
            }
        }
        return null;
    }

    /**
     *  获取JSON中的字段
     * @param jsonStr
     * @param field
     * @return
     */
    public static String getJsonField(String jsonStr, String field){
        if (jsonStr != null && jsonStr.length()>0 && field!=null && field.length()>0){
            try {
                JSONObject object = JSON.parseObject(jsonStr);
                if (object.containsKey(field)) {
                    return object.get(field)==null?"":object.get(field).toString();
                } else {
                    log.debug("getJsonField but the str :" + jsonStr + "don't hava the field : " + field);
                }
            }catch (Exception e){
                log.debug("getJsonField Exception, msg :{}",ExUtil.buildErrorMessage(e));
            }
        }
        return null;
    }

    public static String getJsonStrField(String jsonStr, String field){
        if (jsonStr != null && jsonStr.length()>0 && field!=null && field.length()>0){
            JSONObject object = JSON.parseObject(jsonStr);
            if (object.containsKey(field)) {
                return object.get(field)==null?"":object.get(field).toString();
            }
        }
        return null;
    }

    public static Integer getJsonIntField(String jsonStr, String field){
        if (jsonStr != null && jsonStr.length()>0 && field!=null && field.length()>0){
            try {
                JSONObject object = JSON.parseObject(jsonStr);
                if (object.containsKey(field)) {
                    return object.getInteger(field);
                } else {
                    log.debug("getJsonIntField but the str don't hava the field : " + field);
                }
            }catch (Exception e){
                log.debug("getJsonIntField Exception, msg :{}",ExUtil.buildErrorMessage(e));
            }
        }
        return null;
    }

    public static Long getJsonLongField(String jsonStr, String field){
        if (jsonStr != null && jsonStr.length()>0 && field!=null && field.length()>0){
            try {
                JSONObject object = JSON.parseObject(jsonStr);
                if (object.containsKey(field)) {
                    return object.getLong(field);
                } else {
                    log.debug("getJsonLongField but the str don't hava the field : " + field);
                }
            }catch (Exception e){
                log.debug("getJsonLongField Exception, msg :{}",ExUtil.buildErrorMessage(e));
            }
        }
        return null;
    }

    public static boolean isJson(String str){
        boolean flag = true;
        try {
            JSON.parseObject(str);
            flag = true;
        }catch (JSONException e){
            flag = false;
        }
        return flag;
    }
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
