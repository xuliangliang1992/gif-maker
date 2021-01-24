package com.highlands.common.util;

import java.util.List;

/**
 * @author xll
 * @date 2019-05-16
 */
public class ListUtil {

    /**
     * 去除重复元素
     *
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
