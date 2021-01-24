package com.xll.gif.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseRewardedAdActivity;
import com.highlands.common.dialog.DialogClickListener;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.util.DateUtil;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;
import com.xll.gif.service.GifMakeService;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.databinding.EditGifActivityBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import pl.droidsonroids.gif.GifDrawable;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/13
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class EditGifActivity extends BaseRewardedAdActivity {

    EditGifActivityBinding mBinding;
    String path;
    GifDrawable gifDrawable;
    ArrayList<Bitmap> list;
    int delay = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_gif_activity);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        path = getIntent().getStringExtra("filePath");
        list = new ArrayList<>();
        initGiv2();

    }

    @Override
    protected void initListener() {
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isAuth() || MainApplication.isAdAuth()) {
                    editGif();
                } else {
                    DialogManager.getInstance().showAuthDialog(EditGifActivity.this,
                            new DialogClickListener() {
                                @Override
                                public void leftClickListener() {
                                    playAd();
                                }

                                @Override
                                public void rightClickListener() {
                                    if (MainApplication.isAuth() || MainApplication.isAdAuth()) {
                                        editGif();
                                    } else {
                                        ToastUtil.showToast(EditGifActivity.this, "You can use this function after watching the advertisement");
                                    }
                                }
                            });
                }
            }
        });
        mBinding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gifDrawable.isPlaying()) {
                    mBinding.ivStart.setImageResource(R.mipmap.play);
                    gifDrawable.pause();
                } else {
                    mBinding.ivStart.setImageResource(R.mipmap.pause);
                    gifDrawable.start();
                }
            }
        });

        mBinding.seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                float tempDelay = (float) 6000 / seekParams.progress;
                Timber.tag(TAG).i("onProgressChanged i=" + seekParams.progress);
                Timber.tag(TAG).i("tempDelay i=" + tempDelay / delay);
                gifDrawable.setSpeed(tempDelay / delay);
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {

            }
        });

    }

    private void editGif() {
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                tryMaker();
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {

            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

            }
        }, new RxPermissions(this));
    }

    private void tryMaker() {
        showLoading();
        MainApplication.setBitmaps(list);
        final File file = FileUtil.createFile(this,DateUtil.getCurrentTimeYMDHMS() + ".gif");
        GifMakeService.startMaking(this, mBinding.seekBar.getProgress(), file.getAbsolutePath());
    }

    //初始化由MediaPlayerControl控制的播放的gif
    private void initGiv2() {
        try {
            gifDrawable = new GifDrawable(path);
            int totalCount = gifDrawable.getNumberOfFrames();
            for (int i = 0; i < totalCount; i++) {
                list.add(gifDrawable.seekToFrameAndGet(i));
            }
            mBinding.giv.setImageDrawable(gifDrawable);//这里是实际决定资源的地方，优先级高于xml文件的资源定义
            delay = gifDrawable.getDuration() / list.size();
            mBinding.seekBar.setProgress(100 / delay < 2 ? 2 : 100 / delay);
            Timber.tag(TAG).e(gifDrawable.getDuration() + "");
            Timber.tag(TAG).e(list.size() + "  ----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
