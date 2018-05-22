package com.zjwh.sw.customize.utils;

/**
 * @author Abner
 * @time 2016/6/1 19:57
 */
public class Regexp {
    /**
     * 验证手机号
     */
    public static final String PHONE = "[1]\\d{10}";
    /**
     * 密码数字字符组合
     */
    public static final String PSW = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,12}$";
    /**
     * 数字或字符
     */
    public static final String NunOrStr = "^[0-9a-zA-Z]*$";
    /**
     * 中文或字符或空格
     */
    public static final String ChinaOrStrOrMark = "^[\\u4e00-\\u9fa5A-Za-z\\s]*$";
    /**
     * 中文或字符或空格
     */
    public static final String ChinaOrStrOrMarkOrNum = "^[\\u4e00-\\u9fa5A-Za-z0-9\\s]*$";

    /**
     * 验证是否手机号
     * @param mobile
     * @return
     */
    public static boolean isMobilePhone(String mobile) {
        return mobile.matches(PHONE);
    }

    /**
     * 验证密码是否数字与字母的组合
     * @param psw
     * @return
     */
    public static boolean isPswWithNoAndStr(String psw) {
        return psw.matches(PSW);
    }

    /**
     * 只允许输入数字或字符
     * @param str
     * @return
     */
    public static boolean isOnlyNumOrStr(String str) {
        return str.matches(NunOrStr);
    }

    /**
     * 只允许输入中文或字符或标点符号
     * @param str
     * @return
     */
    public static boolean isOnlyChinaOrStrOrMark(String str) {
        return str.matches(ChinaOrStrOrMark);
    }

    /**
     * 只允许输入中文或字符或标点符号或数字
     * @param str
     * @return
     */
    public static boolean isOnlyChinaOrStrOrMarkOrNum(String str) {
        return str.matches(ChinaOrStrOrMarkOrNum);
    }
}
