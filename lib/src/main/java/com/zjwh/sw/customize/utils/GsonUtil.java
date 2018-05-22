package com.zjwh.sw.customize.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * create lyl on 2017/10/19
 * </p>
 * gson工具类
 */
public class GsonUtil {
    private static Gson gson;

    private GsonUtil() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            gson = new GsonBuilder().registerTypeAdapter(Double.class, new DoubleTypeAdapter()).create();
        }
        return gson;
    }

    /**
     * 转成map的obj
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> ObjToMaps(Object obj) throws IllegalAccessException {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            //把序列化号去掉
            map.put(field.getName(), field.get(obj));
        }

        return map;

    }
}