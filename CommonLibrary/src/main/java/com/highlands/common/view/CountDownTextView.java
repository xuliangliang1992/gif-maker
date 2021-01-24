package com.highlands.common.view;


import android.content.Context;
import android.util.AttributeSet;

import com.highlands.common.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * 倒计时控件
 *
 * @author xll
 * @date 2019-06-18
 */
public class CountDownTextView extends AppCompatTextView {
    private int count, enableColor, disableColor;
    private CompositeDisposable mCompositeDisposable;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化控件
     *
     * @param count 倒计时
     */
    public void init(int count) {
        init(count, R.color.white, R.color.white);
    }

    /**
     * 初始化控件
     *
     * @param count        倒计时
     * @param enableColor  可点击时字体颜色
     * @param disableColor 不可点击时字体颜色
     */
    public void init(int count, int enableColor, int disableColor) {
        this.count = count;
        this.enableColor = enableColor;
        this.disableColor = disableColor;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 点击开启倒计时
     */
    public void onClick() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(aLong -> count - aLong)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    CountDownTextView.this.setEnabled(false);
                    CountDownTextView.this.setTextColor(ContextCompat.getColor(getContext(), disableColor));
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        CountDownTextView.this.setText(aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.tag("CountDownTextView").e(e);
                    }

                    @Override
                    public void onComplete() {
                        CountDownTextView.this.setText("重新发送");
                        CountDownTextView.this.setEnabled(true);
                        CountDownTextView.this.setTextColor(ContextCompat.getColor(CountDownTextView.this.getContext(), enableColor));
                    }
                });
    }

    /**
     * 取消倒计时
     */
    public void onDestroy() {
        mCompositeDisposable.clear();
    }
}
