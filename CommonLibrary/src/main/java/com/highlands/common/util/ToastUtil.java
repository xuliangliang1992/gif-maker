package com.highlands.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.highlands.common.R;

import androidx.annotation.StringRes;

/**
 * @author xll
 * @date 2018/1/1
 */
public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context context,@StringRes int resId) {
        String text = context.getString(resId);
        showToast(context, text);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Gravity.CENTER);
    }

    public static void showToastCenter(Context context, String text) {
        showToast(context, text, Gravity.CENTER);
    }

    public static void showToast(Context context, String text, int gravity) {
        if (StringUtil.isStringNull(text)) {
            return;
        }
        cancelToast();
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.toast_layout, null);
            ((TextView) layout.findViewById(R.id.tv_toast_text)).setText(text);
            mToast = new Toast(context);
            mToast.setView(layout);
            mToast.setGravity(gravity, 0, 20);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
