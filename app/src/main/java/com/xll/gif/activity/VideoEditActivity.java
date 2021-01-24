package com.xll.gif.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.util.DateUtil;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.service.GifMakeService;
import com.xll.gif.R;
import com.xll.gif.databinding.VideoEditActivityBinding;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/10
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class VideoEditActivity extends BaseActivity {

    String path;

    VideoEditActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.video_edit_activity);

        initData();
        initListener();

    }

    @Override
    protected void initData() {
        super.initData();
        path = getIntent().getStringExtra("videoPath");
        Timber.tag(TAG).d(path);
        setupVideo(path);
        //        mBinding.vcsb.setVideoUri(path);

        // ====== 视频剪辑View  ======
        // 进行重置
        mBinding.vsb.reset();
        // 是否需要绘制进度 - 白色进度动,以及走过的画面背景变暗 - 统一控制setProgressLine(isDrawProgress), setProgressBG(isDrawProgress)
        mBinding.vsb.setProgressDraw(true);
        //		 是否需要绘制进度 - 播放中,有个白色的线条在动
        mBinding.vsb.setProgressLine(true);
        // 是否需要绘制进度 - 播放过的画面背景变暗
        mBinding.vsb.setProgressBG(true);
        // 是否属于裁剪模式 - 是否绘制非裁剪模块变暗
        mBinding.vsb.setCutMode(true, true);
        // 视频关键帧间隔(毫秒,表示左右两个模块最低限度滑动时间,无法选择低于该关键帧的裁剪时间)
        float videoFrame = 3 * 1000f;
        // 设置本地视频路径 - 默认裁剪模式,则不绘制播放背景
        mBinding.vsb.setVideoUri(true, path, videoFrame);
        // 不设置关键帧时间,则默认最多是两个ImageView左右多出的宽度
        //		 mBinding.vsb.setVideoUri(isCutMode, videoUri);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinding.videoView.isPlaying()) {
                    mBinding.videoView.pause();
                    mBinding.ivStart.setImageResource(R.mipmap.play);
                } else {
                    mBinding.ivStart.setImageResource(R.mipmap.pause);
                    mBinding.videoView.start();
                }
            }
        });

        mBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        showLoading();
                        tryMaker();
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {

                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                    }
                }, new RxPermissions(VideoEditActivity.this));
            }
        });
    }

    private void tryMaker() {
        final File file = FileUtil.createFile(DateUtil.getCurrentTimeYMDHMS() + ".gif", FileUtil.FILE_TYPE_GIF);
        if(mBinding.vsb.getEndTime()-mBinding.vsb.getStartTime()<3000){
            ToastUtil.showToast(this,"video can't less than 2 seconds");
        }
        GifMakeService.startMaking(this, path, file.getAbsolutePath(), (int) mBinding.vsb.getStartTime(), (int) mBinding.vsb.getEndTime(), 200);
    }

    private void setupVideo(String path) {
        mBinding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mBinding.videoView.start();
            }
        });
        mBinding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBinding.videoView.start();
            }
        });
        mBinding.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {
            Uri uri = Uri.parse(path);
            mBinding.videoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mBinding.videoView.isPlaying()) {
            mBinding.videoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBinding.videoView.canPause()) {
            mBinding.videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            mBinding.videoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
