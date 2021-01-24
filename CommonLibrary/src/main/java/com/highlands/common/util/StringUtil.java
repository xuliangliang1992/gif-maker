package com.highlands.common.util;

import com.highlands.common.R;
import com.highlands.common.base.BaseApplication;

import java.text.DecimalFormat;

/**
 * @author xll
 * @date 2018/1/1
 */
public class StringUtil {

    public static final int BIRTH_YEAR = 1;
    public static final int BIRTH_MONTH = 2;
    public static final int BIRTH_DAY = 3;

    public static boolean isStringNull(String temp) {
        return temp == null || temp.isEmpty() || " ".equals(temp) || "undefined".equals(temp) || "null".equals(temp);
    }

    /**
     * 字符串为空时替换成默认字符串
     *
     * @param str
     * @param defaultStr
     * @return
     */
    public static String emptyIs(String str, String defaultStr) {
        return isStringNull(str) ? defaultStr : str;
    }

    /**
     * 字符串为空时替换成默认字符串
     *
     * @param str
     * @return ""
     */
    public static String emptyIs(String str) {
        return isStringNull(str) ? " " : str;
    }

    /**
     * 字符串为空时替换成未命名
     *
     * @param str
     * @return ""
     */
    public static String emptyIsUnnamed(String str) {
        return isStringNull(str) ? "未命名" : str;
    }

    /**
     * 获取身份证上生日信息
     *
     * @param data 生日信息 eg：19990909
     * @param type Constant.BIRTH_YEAR 年
     * @return 1999 09 09
     */
    public static String formatBirth(String data, int type) {
        String value = "";
        if (data != null && data.length() > 0) {
            switch (type) {
                case BIRTH_YEAR:
                    value = data.substring(0, 4);
                    break;
                case BIRTH_MONTH:
                    value = data.substring(4, 6);
                    break;
                case BIRTH_DAY:
                    value = data.substring(6, 8);
                    break;
                default:
                    return value;
            }
        }
        return value;
    }

    public static String formatPeriod(String var1, String var2) {
        String var3 = "";
        if (!isStringNull(var1)) {
            var3 = var1.substring(0, 4) + "." + var1.substring(4, 6) + "." + var1.substring(6, 8);
        }
        String var4 = "";
        if (!isStringNull(var2)) {
            if (var2.contains("长期")) {
                var4 = var2;
            } else {
                var4 = var2.substring(0, 4) + "." + var2.substring(4, 6) + "." + var2.substring(6, 8);
            }
        }

        return var3 + "-" + var4;
    }

    /**
     * 格式化手机号
     *
     * @param mobile
     * @return
     */
    public static String hideMobile(String mobile) {
        if (isStringNull(mobile)) {
            return "";
        }
        if (mobile.length() < 7) {
            return mobile;
        }
        StringBuilder sbShow = new StringBuilder(mobile.subSequence(0, 3));
        sbShow.append("****");
        sbShow.append(mobile.substring(mobile.length() - 4,
                mobile.length()));
        return sbShow.toString();
    }

    /**
     * 隐藏身份证号
     *
     * @param idNumber
     * @return
     */
    public static String hideIdNumber(String idNumber) {
        if (isStringNull(idNumber)) {
            return "";
        }
        if (idNumber.length() < 10) {
            return idNumber;
        }
        StringBuilder sbShow = new StringBuilder(idNumber.subSequence(0, 6));
        sbShow.append("****");
        sbShow.append(idNumber.substring(idNumber.length() - 4,
                idNumber.length()));
        return sbShow.toString();
    }

    /**
     * 隐藏姓名
     *
     * @param name
     * @return
     */
    public static String hideName(String name) {
        if (isStringNull(name)) {
            return "";
        }
        if (name.length() < 2) {
            return name;
        }
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (i == name.length() - 1) {
                newName.append(name.charAt(i));
            } else {
                newName.append("*");
            }
        }
        return newName.toString();
    }

    public static String formatDoubleDateWithLine(double time) {
        String strApplyTime = new DecimalFormat("########").format(time);
        if (!isStringNull(strApplyTime)) {
            String yTime = strApplyTime.substring(0, 4);
            String mTime = strApplyTime.substring(4, 6);
            String dTime = strApplyTime.substring(6, 8);
            return yTime + "-" + mTime + "-" + dTime;
        }
        return "";
    }

    public static boolean checkPhone(String phone) {
        if (StringUtil.isStringNull(phone)) {
            ToastUtil.showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.please_input_phone));
            return false;
        }
        if (phone.length() != 11) {
            ToastUtil.showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.error_phone));
            return false;
        }
        return true;
    }

    public static boolean checkCode(String code) {
        if (StringUtil.isStringNull(code)) {
            ToastUtil.showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.please_input_code));
            return false;
        }

        return true;
    }

    public static boolean checkPassword(String password) {
        if (StringUtil.isStringNull(password)) {
            ToastUtil.showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.please_input_password));
            return false;
        }

        return true;
    }
}
