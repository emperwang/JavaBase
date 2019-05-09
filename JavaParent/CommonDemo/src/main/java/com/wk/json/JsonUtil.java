package com.wk.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    public final  static Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    public static String bean2Json(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static String bean2PretyJson(Object object){
        return gsonPretty.toJson(object);
    }

    public static <T> T json2Bean(String json, Class<T> clazz){
        Gson gson = new Gson();
        return gson.fromJson(json,clazz);
    }

    public static <T> T json2Bean(String json, Type type){
        Gson gson = new Gson();
        return gson.<T>fromJson(json,type);
    }

    public static String extend(String json,String extend){
        JsonObject json1 = gsonPretty.fromJson(json, JsonObject.class);
        JsonObject extJson = gsonPretty.fromJson(extend, JsonObject.class);
        return gsonPretty.toJson(extend(json1,extJson));
    }

    public static JsonObject extend(JsonObject json,JsonObject ext){
        if(ext == null){
            return json == null? new JsonObject():json;
        }
        if (json == null){
            return ext;
        }
        for (Map.Entry<String,JsonElement> entry:ext.entrySet()){
            String key = entry.getKey();
            json.add(key,extendElement(json.get(key),entry.getValue()));
        }
        return json;
    }

    private static JsonElement extendElement(JsonElement dst, JsonElement src) {
        if(src == null){
            return dst == null? JsonNull.INSTANCE:dst;
        }

        if(dst == null){
            return src;
        }

        if(dst.getClass().equals(src.getClass())){
            if(dst.isJsonNull()){
                return dst;
            }else if(dst.isJsonPrimitive()){
                return dst.equals(src) ? dst:src;
            }else if(dst.isJsonArray()){
                return extendArray(dst.getAsJsonArray(),src.getAsJsonArray());
            }else if(dst.isJsonObject()){
                return extend(dst.getAsJsonObject(),src.getAsJsonObject());
            }else{
                throw new RuntimeException("Unknown JsonELement");
            }

        }
        return dst;
    }

    private static JsonElement extendArray(JsonArray a1, JsonArray a2) {
        int s1 = a1.size();
        int s2 = a2.size();

        for(int i=0;i<s2;i++){
            JsonElement e = a2.get(i);
            if(i < s1){
                JsonElement a1Tmp = a1.get(i);
                a1.set(i,extendElement(a1Tmp,e));
            }else {
                a1.add(e);
            }
        }
        return a1;
    }

}
