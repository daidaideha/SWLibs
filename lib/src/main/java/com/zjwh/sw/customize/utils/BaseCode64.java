package com.zjwh.sw.customize.utils;


import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public class BaseCode64 {
    public static String encode(String str) {
        try {
            return new String(Base64.encodeBase64(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
