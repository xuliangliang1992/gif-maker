package com.xll.gif.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.highlands.common.base.fragment.BaseFragment;
import com.highlands.common.util.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.R;
import com.xll.gif.activity.CameraActivity;
import com.xll.gif.activity.GalleryActivity;
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
    }

    @Override
    public void initListener() {
        addClicks(mBinding.tvCtg, unit -> {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video");
            mFirebaseAnalytics.logEvent("try_for_free", bundle);
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
            Bundle params = new Bundle();
            params.putString("image_name", "image_name");
            params.putString("full_text", "full_text");
            mFirebaseAnalytics.logEvent("share_image", params);
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
