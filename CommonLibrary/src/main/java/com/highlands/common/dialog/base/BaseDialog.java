package com.highlands.common.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import timber.log.Timber;

/**
 * @author xll
 * @date 2018/1/1
 */
public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {
    /**
     * TAG(日志)
     */
    protected String TAG;
    /**
     * context(上下文)
     */
    protected Context context;
    /**
     * (DisplayMetrics)设备密度
     */
    protected DisplayMetrics dm;
    /**
     * enable dismiss outside dialog(设置点击对话框以外区域,是否dismiss)
     */
    protected boolean cancel = true;
    /**
     * dialog width scale(宽度比例)
     */
    protected float widthScale = 1;
    /**
     * dialog height scale(高度比例)
     */
    protected float heightScale;
    /**
     * top container(最上层容器)
     */
    protected LinearLayout ll_top;
    /**
     * container to control dialog height(用于控制对话框高度)
     */
    protected LinearLayout ll_control_height;
    /**
     * is showAnim running(显示动画是否正在执行)
     */
    private boolean isShowAnim;
    /**
     * is DismissAnim running(消失动画是否正在执行)
     */
    private boolean isDismissAnim;
    /**
     * max height(最大高度)
     */
    protected float maxHeight;

    /**
     * method execute order:
     * show:constructor---show---onCreate---onStart---onAttachToWindow
     * dismiss:dismiss---onDetachedFromWindow---onStop
     *
     * @param context
     */
    public BaseDialog(Context context) {
        super(context);
        setDialogTheme();
        this.context = context;
        this.TAG = this.getClass().getSimpleName();
        Log.d(TAG, "constructor");
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.TAG = this.getClass().getSimpleName();
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
            //        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
            //                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
        }
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * inflate layout for dialog ui and return (填充对话框所需要的布局并返回)
     * <pre>
     *
     * public View onCreateView() {
     * View inflate = View.inflate(context, R.layout.dialog_share, null);
     * return inflate;
     * }
     * </pre>
     */
    public abstract View onCreateView();

    /**
     * set Ui data or logic opreation before attatched window(在对话框显示之前,设置界面数据或者逻辑)
     *
     * @return true dialog show,fasle dialog not show
     */
    public abstract boolean setUiBeforeShow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        Window window = this.getWindow();
        // 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置宽度
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        // 给 DecorView 设置背景颜色，很重要，不然导致 Dialog 内容显示不全，有一部分内容会充当 padding，上面例子有举出
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        dm = context.getResources().getDisplayMetrics();
        ll_top = new LinearLayout(context);
        ll_top.setGravity(Gravity.CENTER);

        ll_top.setBackgroundColor(Color.parseColor("#00000000"));
        ll_control_height = new LinearLayout(context);
        ll_control_height.setOrientation(LinearLayout.VERTICAL);
        ll_control_height.setBackgroundColor(Color.parseColor("#00000000"));
        ll_control_height.addView(onCreateView());
        ll_top.addView(ll_control_height);

        //        maxHeight = dm.heightPixels - StatusBarUtils.getHeight(context);
        maxHeight = dm.heightPixels;
        // maxHeight = dm.heightPixels;
        setContentView(ll_top, new ViewGroup.LayoutParams(dm.widthPixels, (int) maxHeight));
        setCanceledOnTouchOutside(cancel);

        setCancelable(cancel);
        ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel) {
                    dismiss();
                }
            }
        });
    }

    /**
     * when dailog attached to window,set dialog width and height and show anim
     * (当dailog依附在window上,设置对话框宽高以及显示动画)
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Timber.tag(TAG).d("onAttachedToWindow");

        setUiBeforeShow();

        int width;
        if (widthScale == 0) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (widthScale == 1) {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            width = (int) (dm.widthPixels * widthScale);

        }

        int height;
        if (heightScale == 0) {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (heightScale == 1) {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            height = (int) (maxHeight * heightScale);
        }

        ll_control_height.setLayoutParams(new LinearLayout.LayoutParams(width, height));

    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        this.cancel = cancel;
        super.setCanceledOnTouchOutside(cancel);
        super.setCancelable(cancel);
    }

    @Override
    public void show() {
        Timber.tag(TAG).d("show");
        super.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.tag(TAG).d("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.tag(TAG).d("onStop");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Timber.tag(TAG).d("onDetachedFromWindow");
    }

    @Override
    public void dismiss() {
        Timber.tag(TAG).d("dismiss");
        superDismiss();
    }

    /**
     * dismiss without anim(无动画dismiss)
     */
    public void superDismiss() {
        super.dismiss();
    }

    /**
     * dialog anim by styles(动画弹出对话框,style动画资源)
     *
     * @param animStyle
     */
    public void show(int animStyle) {
        Window window = getWindow();
        window.setWindowAnimations(animStyle);
        show();
    }

    /**
     * set window dim or not(设置背景是否昏暗)
     *
     * @param isDimEnabled
     * @return BaseDialog
     */
    public BaseDialog dimEnabled(boolean isDimEnabled) {
        if (isDimEnabled) {
            getWindow().addFlags(LayoutParams.FLAG_DIM_BEHIND);
        } else {
            getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        }
        return this;
    }

    /**
     * set dialog width scale:0-1(设置对话框宽度,占屏幕宽的比例0-1)
     *
     * @param widthScale
     * @return BaseDialog
     */
    public BaseDialog widthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * set dialog height scale:0-1(设置对话框高度,占屏幕高的比例0-1)
     *
     * @param heightScale
     * @return BaseDialog
     */
    public BaseDialog heightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDismissAnim || isShowAnim) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (isDismissAnim || isShowAnim) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * dp to px
     *
     * @param dp
     * @return
     */
    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
