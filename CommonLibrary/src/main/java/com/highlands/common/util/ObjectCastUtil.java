package com.highlands.common.util;

/**
 * @author xll
 * @date 2019-06-11
 */
public class ObjectCastUtil {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
