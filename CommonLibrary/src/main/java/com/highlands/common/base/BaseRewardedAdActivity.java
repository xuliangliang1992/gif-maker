package com.highlands.common.base;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.highlands.common.BaseConstant;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.util.ToastUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;

/**
 * google AdMob 激励广告
 *
 * @author xuliangliang
 * @Date 2021/1/20
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public abstract class BaseRewardedAdActivity extends BaseActivity {

    protected RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardedAd = createAndLoadRewardedAd();
    }

    protected RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                BaseConstant.AD_MOB_KEY);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Timber.tag(TAG).i("Ad successfully loaded.");
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Timber.tag(TAG).e(adError.toString());
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void playAd() {
        if(BaseApplication.isAuth()){
            return;
        }
        if (rewardedAd.isLoaded()) {
            Activity activityContext = this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    Timber.tag(TAG).i("onRewardedAdOpened");
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    Timber.tag(TAG).i("onRewardedAdClosed");
                    rewardedAd = createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Timber.tag(TAG).i("onUserEarnedReward " + reward.getType());
                    userEarnedReward(reward);
                    BaseApplication.setAdAuth(true);
                    DialogManager.getInstance().dismissDialog();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    Timber.tag(TAG).e("onRewardedAdFailedToShow " + adError.toString());
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            ToastUtil.showToast(this, "The rewarded ad wasn't loaded yet,please try again in a few seconds!");
        }
    }

    protected void userEarnedReward(@NonNull RewardItem reward) {

    }

}
