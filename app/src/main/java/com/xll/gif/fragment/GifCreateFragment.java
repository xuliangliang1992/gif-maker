package com.xll.gif.fragment;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.highlands.common.base.BaseApplication;
import com.highlands.common.base.fragment.BaseFragment;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ShapeUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.R;
import com.xll.gif.activity.CameraActivity;
import com.xll.gif.activity.GalleryActivity;
import com.xll.gif.activity.LaunchActivity;
import com.xll.gif.activity.VideoListActivity;
import com.xll.gif.databinding.CreateFragmentBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;

/**
 * @author xuliangliang
 * @date 2021/1/10
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifCreateFragment extends BaseFragment {

    private CreateFragmentBinding mBinding;

    public static GifCreateFragment newInstance() {
        return new GifCreateFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.create_fragment;
    }

    @Override
    public void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        ShapeUtil.setShape(mBinding.tvVip, mActivity, 25, R.color.blue_3974FF);

        MobileAds.initialize(mActivity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mBinding.adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.adView.setVisibility(BaseApplication.isAuth() ? View.GONE : View.VISIBLE);
        mBinding.tvVip.setVisibility(BaseApplication.isAuth() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initListener() {
        addClicks(mBinding.tvVip, unit -> {
            if (BaseApplication.isAuth()) {
                showToast("You are the vip");
                return;
            }
            startActivity(new Intent(mActivity, LaunchActivity.class));
        });
        addClicks(mBinding.tvCtg, unit -> {
            PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                                 @Override
                                                 public void onRequestPermissionSuccess() {
                                                     startActivity(new Intent(mActivity, CameraActivity.class));
                                                 }

                                                 @Override
                                                 public void onRequestPermissionFailure(List<String> permissions) {

                                                 }

                                                 @Override
                                                 public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                                                 }
                                             }, new RxPermissions(this),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA);
        });
        addClicks(mBinding.tvPtg, unit -> {
            PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    startActivity(new Intent(mActivity, GalleryActivity.class));
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {

                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                }
            }, new RxPermissions(this));
        });
        addClicks(mBinding.tvVtg, unit -> {
            PermissionUtil.launchCamera(new PermissionUtil.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    Log.d(TAG, "onRequestPermissionSuccess");
                    startActivity(new Intent(mActivity, VideoListActivity.class));

                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {

                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                }
            }, new RxPermissions(this));
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void setPresenter() {

    }

}
