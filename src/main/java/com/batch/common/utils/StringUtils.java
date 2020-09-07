package com.batch.common.utils;

public class StringUtils extends org.springframework.util.StringUtils {

    public static String NVL(String str, String defaultStr) {
        return str != null ? str : defaultStr;
    }
}
