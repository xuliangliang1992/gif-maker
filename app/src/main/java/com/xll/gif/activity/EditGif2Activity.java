package com.xll.gif.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.BaseConstant;
import com.highlands.common.base.BaseRewardedAdActivity;
import com.highlands.common.dialog.DialogClickListener;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.util.DateUtil;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.ToastUtil;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.databinding.EditGif2ActivityBinding;
import com.xll.gif.service.GifMakeService;
import com.xll.gif.view.GifAnimationDrawable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import pl.droidsonroids.gif.GifDrawable;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class EditGif2Activity extends BaseRewardedAdActivity {

    EditGif2ActivityBinding mBinding;
    GifAnimationDrawable mAnimationDrawable;
    String path;
    GifDrawable gifDrawable;
    List<Bitmap> list;
    private int delay;
    int iconId;
    boolean fromGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_gif2_activity);


        initData();
        initListener();
    }

    @Override
    protected void initData() {
        iconId = -1;
        path = getIntent().getStringExtra("filePath");
        fromGallery = getIntent().getBooleanExtra("from", false);
        if (fromGallery) {
            list = MainApplication.getBitmaps();
        } else {
            list = new ArrayList<>();
            mAnimationDrawable = new GifAnimationDrawable();
            initGiv2();
        }

        setBitmapsToAnim();
    }

    private void setBitmapsToAnim() {
        mAnimationDrawable = new GifAnimationDrawable();
        for (int i = 0; i < list.size(); i++) {
            Drawable drawable = new BitmapDrawable(getResources(), list.get(i));
            mAnimationDrawable.addFrame(drawable, delay);
        }
        mAnimationDrawable.setOneShot(false);
        mBinding.iv.setImageDrawable(mAnimationDrawable);
        mBinding.seekBarGif.setMax(list.size() + 1);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAnimationDrawable.setPlayListener(new GifAnimationDrawable.onPlayListener() {
            @Override
            public void onPlay(int currPosition) {
                mBinding.seekBarGif.setProgress(currPosition + 1);
            }
        });
        mBinding.seekBarGif.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                mAnimationDrawable.setCurrentFrame(seekBar.getProgress());
            }
        });
        mBinding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnimationDrawable != null) {
                    if (mAnimationDrawable.isRunning()) {
                        mAnimationDrawable.stop();
                        mBinding.ivStart.setImageResource(R.mipmap.play);
                    } else {
                        mAnimationDrawable.start();
                        mBinding.ivStart.setImageResource(R.mipmap.pause);
                    }
                }
            }
        });

        mBinding.seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
                delay = seekBar.getProgress() * 10;
                mAnimationDrawable.setDuration(seekBar.getProgress() * 10);
            }
        });

        mBinding.tvAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditGif2Activity.this, AddTextActivity.class);
                MainApplication.setBitmaps(list);
                intent.putExtra("delay", delay);
                startActivityForResult(intent, BaseConstant.ADD_TEXT_REQUEST_CODE);
            }
        });
        mBinding.tvAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditGif2Activity.this, AddIconActivity.class);
                MainApplication.setBitmaps(list);
                intent.putExtra("delay", delay);
                startActivityForResult(intent, BaseConstant.ADD_ICON_REQUEST_CODE);
            }
        });

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
                    DialogManager.getInstance().showAuthDialog(EditGif2Activity.this,
                            new DialogClickListener() {
                                @Override
                                public void leftClickListener() {
                                    playAd();
                                }

                                @Override
                                public void rightClickListener() {
                                    ToastUtil.showToast(EditGif2Activity.this, "try for free");
                                    //                                    if (MainApplication.isAuth() || MainApplication.isAdAuth()) {
                                    //                                        editGif();
                                    //                                    } else {
                                    //                                        ToastUtil.showToast(EditGif2Activity.this, "You can use this function after watching the advertisement");
                                    //                                    }
                                }
                            });
                }
            }
        });
    }

    private void editGif() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mBinding.ivStart.setImageResource(R.mipmap.play);
        }
        tryMaker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mBinding.ivStart.setImageResource(R.mipmap.play);
        }
    }

    //初始化由MediaPlayerControl控制的播放的gif
    private void initGiv2() {
        try {
            gifDrawable = new GifDrawable(path);
            int totalCount = gifDrawable.getNumberOfFrames();
            for (int i = 0; i < totalCount; i++) {
                Bitmap bitmap = gifDrawable.seekToFrameAndGet(i);
                list.add(bitmap);
            }
            delay = gifDrawable.getDuration() / list.size();
            mBinding.seekBar.setProgress(100 / delay < 2 ? 2 : 100 / delay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == BaseConstant.ADD_TEXT_REQUEST_CODE) {
                list = MainApplication.getBitmaps();
                setBitmapsToAnim();
            }
            if (requestCode == BaseConstant.ADD_ICON_REQUEST_CODE) {
                list = MainApplication.getBitmaps();
                setBitmapsToAnim();
            }
        }
    }

    private void tryMaker() {
        showLoading();
        mAnimationDrawable.stop();
        MainApplication.setBitmaps(list);
        final File file = FileUtil.createFile(this, DateUtil.getCurrentTimeYMDHMS() + ".gif");
        GifMakeService.startMaking(this, delay, file.getAbsolutePath());
    }
}
