package com.zjwh.sw.customize.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * create lyl on 2017/10/23
 * </p>
 * gson转化适配类
 */
public class DoubleTypeAdapter implements JsonSerializer<Double> {

    @Override
    public JsonElement serialize(Double d, Type type, JsonSerializationContext context) {
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        DecimalFormat format = new DecimalFormat("##0.000000");
        String temp = format.format(d);
        JsonPrimitive pri = new JsonPrimitive(temp);
        Locale.setDefault(locale);
        return pri;
    }

}
