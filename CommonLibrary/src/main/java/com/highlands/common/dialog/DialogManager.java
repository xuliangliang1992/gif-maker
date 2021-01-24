package com.highlands.common.dialog;

import android.content.Context;

import com.highlands.common.dialog.base.BaseDialog;
import com.highlands.common.view.progresshud.ProgressHUD;

import androidx.databinding.ObservableArrayList;

/**
 * @author xll
 * @date 2018/12/3
 */

public class DialogManager {
    protected static DialogManager dialogManager;

    private ProgressHUD hud;
    protected BaseDialog mDialog;

    public DialogManager() {
    }

    public static DialogManager getInstance() {
        if (null == dialogManager) {
            synchronized (DialogManager.class) {
                if (null == dialogManager) {
                    dialogManager = new DialogManager();
                }
            }
        }
        return dialogManager;
    }

    public void showBottomListDialog(Context context, ObservableArrayList<String> titles, ItemClickListener itemClickListener) {
        mDialog = new BottomListDialog(context, titles, itemClickListener);
        mDialog.show();
    }

    public void showAuthDialog(Context context,DialogClickListener dialogClickListener) {
        mDialog = new AuthDialog(context,dialogClickListener);

        mDialog.show();
    }
    public void showProgressDialog(Context context) {
        dismissProgressDialog();
        hud = ProgressHUD.create(context)
                .setStyle(ProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
    }

    public void dismissProgressDialog() {
        if (null != hud) {
            hud.dismiss();
            hud = null;
        }
    }

    public void dismissDialog() {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
