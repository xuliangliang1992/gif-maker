package com.highlands.common.base;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.highlands.common.BaseConstant;
import com.highlands.common.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/22
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class BaseInterstitialAdActivity extends BaseBillingActivity {
    protected InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInterstitialAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(BaseConstant.AD_MOB_KEY);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Timber.tag(TAG).i("onAdLoaded");
                startTimer();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Timber.tag(TAG).i("onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Timber.tag(TAG).i("onAdClicked");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Timber.tag(TAG).i("onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Timber.tag(TAG).e(loadAdError.toString());
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                Timber.tag(TAG).i("onAdImpression");
            }
        });
    }

    private void startTimer() {
        Observable.interval(5, TimeUnit.MINUTES)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        playAd();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void playAd() {
        if(BaseApplication.isAuth()){
            return;
        }
        if ( mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Timber.tag(TAG).d("The interstitial wasn't loaded yet.");
            //            ToastUtil.showToast(this,"The interstitial ad wasn't loaded yet,please try again in a few seconds!");
        }
    }

}
