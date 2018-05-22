package com.zjwh.sw.customize.utils;//package com.bci.wx.base.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESUtils {
    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "smkldospdosldaaa";//key，可自行修改
    private static AESUtils instance = null;

    AESUtils() {
    }

    public static AESUtils getInstance() {
        if (instance == null)
            instance = new AESUtils();
        return instance;
    }

    /**
     * 加密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sKey.getBytes(), "AES"));
            return new BASE64Encoder().encode(cipher.doFinal(sSrc.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 加密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc, String sKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sKey.getBytes(), "AES"));
            return new BASE64Encoder().encode(cipher.doFinal(sSrc.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public String decrypt(String sSrc) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKey.getBytes(), "AES"));
            BASE64Decoder decoder = new BASE64Decoder();
            return new String(cipher.doFinal(decoder.decodeBuffer(sSrc)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public String decrypt(String sSrc, String sKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKey.getBytes(), "AES"));
            BASE64Decoder decoder = new BASE64Decoder();
            return new String(cipher.doFinal(decoder.decodeBuffer(sSrc)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}