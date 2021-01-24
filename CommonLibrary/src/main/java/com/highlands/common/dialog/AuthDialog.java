package com.highlands.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.R;
import com.highlands.common.databinding.AuthDialogBinding;
import com.highlands.common.dialog.base.BaseDialog;
import com.highlands.common.util.ShapeUtil;

import androidx.databinding.DataBindingUtil;


/**
 * 底部有取消按钮的弹出框 从底部弹出
 *
 * @author xll
 * @date 2018/1/1
 */
public class AuthDialog extends BaseDialog {
    private AuthDialogBinding mBinding;
    private DialogClickListener mDialogClickListener;

    public AuthDialog(Context context, DialogClickListener dialogClickListener) {
        super(context);
        this.mDialogClickListener = dialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView() {
        View view = View.inflate(context, R.layout.auth_dialog, null);
        mBinding = DataBindingUtil.bind(view);
        widthScale(0.8f);
        ShapeUtil.setShape(mBinding.llContent, context, 20, R.color.white);
        ShapeUtil.setShape(mBinding.tvTry, context, 10, R.color.red_DE0F04);
        ShapeUtil.setShape(mBinding.tvWatch, context, 10, R.color.blue_709AFF);

        if (mDialogClickListener != null) {
            mBinding.tvTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogClickListener.rightClickListener();
                    dismiss();
                }
            });
            mBinding.tvWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogClickListener.leftClickListener();
                }
            });
        }
        return view;
    }

    @Override
    public boolean setUiBeforeShow() {
        return true;
    }


}
