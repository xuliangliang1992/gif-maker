package com.highlands.common.util;

import java.util.Map;

/**
 * @author xll
 * @date 2018/1/1
 */
public class HandleMapUtil {

    public static int getInt(Map map, String key) {
        return getInt(map, key, -1);
    }

    public static int getInt(Map map, String key, int dft) {
        Object v = map.get(key);
        if (v instanceof Integer) {
            return (int) v;
        } else if (v instanceof Float) {
            return ((int) (float) v);
        } else if (v instanceof Long) {
            return ((int) (long) v);
        } else if (v instanceof Double) {
            return ((int) (double) v);
        } else if (v instanceof String) {
            String value = (String) v;
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return dft;
            }
        }
        return dft;
    }

    public static float getFloat(Map map, String key) {
        return getFloat(map, key, 0.0f);
    }

    public static float getFloat(Map map, String key, float dft) {
        Object v = map.get(key);
        if (v instanceof Integer) {
            return (float) (int) v;
        } else if (v instanceof Float) {
            return (float) v;
        } else if (v instanceof Long) {
            return ((float) (long) v);
        } else if (v instanceof Double) {
            return ((float) (double) v);
        } else if (v instanceof String) {
            String value = (String) v;
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                return dft;
            }
        }
        return dft;
    }

    public static double getDouble(Map map, String key) {
        return getDouble(map, key, 0.0D);
    }

    public static double getDouble(Map map, String key, double dft) {
        Object v = map.get(key);
        if (v instanceof Integer) {
            return (double) (int) v;
        } else if (v instanceof Float) {
            return (double) (float) v;
        } else if (v instanceof Long) {
            return ((double) (long) v);
        } else if (v instanceof Double) {
            return (double) v;
        } else if (v instanceof String) {
            String value = (String) v;
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return dft;
            }
        }
        return dft;
    }

    public static String getString(Map map, String key) {
        return getString(map, key, "");
    }

    public static String getString(Map map, String key, String dft) {
        if (map == null) {
            return dft;
        }
        Object v = map.get(key);
        if (v == null) {
            return dft;
        } else {
            return String.valueOf(v);
        }
    }
}
