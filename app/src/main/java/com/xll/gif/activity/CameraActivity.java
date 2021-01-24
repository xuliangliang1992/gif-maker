package com.xll.gif.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.util.DateUtil;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.ToastUtil;
import com.jakewharton.rxbinding3.view.RxView;
import com.xll.gif.R;
import com.xll.gif.databinding.ActivityCameraBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.VideoCapture;
import androidx.camera.view.CameraView;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/10
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class CameraActivity extends BaseActivity {
    String TAG = "CameraActivity";
    ActivityCameraBinding mBinding;
    Executor mExecutor;
    private CompositeDisposable mCompositeDisposable;
    File file;
    int currentIndex;
    int videoCount;
    long seconds;
    private boolean isImage;

    private Disposable mDisposable;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        initData();
        initListener();
        if (!checkCameraHardware(this)) {
            finish();
            return;
        }

        setSystemUIChangeListener();
    }

    @SuppressLint("MissingPermission")
    protected void initData() {
        mCompositeDisposable = new CompositeDisposable();
        isImage = getIntent().getBooleanExtra("isImage", false);
        mBinding.camera.bindToLifecycle(this);
        mExecutor = Executors.newSingleThreadExecutor();

        currentIndex = 0;

    }

    protected void initListener() {

        mCompositeDisposable.add(RxView.clicks(mBinding.btnStart)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (isImage) {
                        startTakePhoto();
                    } else {
                        startRecording();
                    }
                }));
        mCompositeDisposable.add(RxView.clicks(mBinding.btnStop)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (seconds <= 2) {
                        ToastUtil.showToast(this, "video can't less than 2 seconds");
                    } else {
                        showLoading();
                        stopRecording();
                    }
                }));

        mCompositeDisposable.add(RxView.clicks(mBinding.btnLight)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mBinding.camera.getFlash() == FlashMode.ON) {
                        mBinding.camera.setFlash(FlashMode.OFF);
                        mBinding.btnLight.setImageResource(R.drawable.light_off);
                    } else if (mBinding.camera.getFlash() == FlashMode.OFF) {
                        mBinding.camera.setFlash(FlashMode.ON);
                        mBinding.btnLight.setImageResource(R.drawable.light_on);
                    }
                }));
        mCompositeDisposable.add(RxView.clicks(mBinding.btnChangeShot)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mBinding.camera.getCameraLensFacing() == CameraX.LensFacing.FRONT) {
                        mBinding.camera.setCameraLensFacing(CameraX.LensFacing.BACK);
                    } else if (mBinding.camera.getCameraLensFacing() == CameraX.LensFacing.BACK) {
                        mBinding.camera.setCameraLensFacing(CameraX.LensFacing.FRONT);
                    }
                }));
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.camera.isRecording()) {
                    stopRecording();
                    file.delete();
                }
                finish();
            }
        });
    }

    private void startTakePhoto() {
        file = FileUtil.createFile(this, DateUtil.getCurrentTimeYMD() + ".jpg");
        mBinding.camera.setCaptureMode(CameraView.CaptureMode.IMAGE);
        mBinding.btnStart.setVisibility(View.VISIBLE);
        mBinding.btnStop.setVisibility(View.GONE);
        mBinding.tvTime.setVisibility(View.GONE);
        mBinding.camera.takePicture(file, mExecutor, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {
                refreshPicture(file);

                finish();
            }

            @Override
            public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {

            }
        });
    }

    private void refreshPicture(@NonNull File file) {
        //通知系统相册刷新
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), "", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(file);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(localIntent);
    }

    private void startRecording() {
        mBinding.btnChangeShot.setVisibility(View.INVISIBLE);
        file = FileUtil.createFile(this, DateUtil.getCurrentTimeYMD() + ".mp4");
        mBinding.camera.setFlash(FlashMode.ON);
        mBinding.camera.setCameraLensFacing(CameraX.LensFacing.BACK);
        mBinding.camera.setCaptureMode(CameraView.CaptureMode.VIDEO);
        mBinding.btnStart.setVisibility(View.GONE);
        mBinding.btnStop.setVisibility(View.VISIBLE);

        mBinding.camera.startRecording(file, mExecutor, new VideoCapture.OnVideoSavedListener() {
            @Override
            public void onVideoSaved(@NonNull File file) {
                Timber.tag(TAG).i("onVideoSaved: " + file.getPath());
                hideLoading();
                insertIntoMediaStore(CameraActivity.this, true, file, System.currentTimeMillis());
                startActivity(new Intent(CameraActivity.this, VideoEditActivity.class).putExtra("videoPath", file.getPath()));
            }

            @Override
            public void onError(@NonNull VideoCapture.VideoCaptureError videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                Timber.tag(TAG).e(message);
            }

        });

        mDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        seconds = aLong + 1;
                        mBinding.tvTime.setVisibility(View.VISIBLE);
                        mBinding.tvTime.setText(secondsToMin(seconds));
                        if (aLong >= 59) {
                            mBinding.tvDuration.setVisibility(View.VISIBLE);
                            mBinding.tvDuration.setText("video must less than 1 minute");
                            stopRecording();
                        } else {
                            mBinding.tvDuration.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void stopRecording() {
        mBinding.camera.stopRecording();
        mBinding.btnStart.setVisibility(View.VISIBLE);
        mBinding.btnStop.setVisibility(View.GONE);
        mBinding.tvTime.setVisibility(View.GONE);
        mBinding.tvDuration.setVisibility(View.GONE);
        seconds = 0;
        cancel();
        mBinding.btnChangeShot.setVisibility(View.VISIBLE);
    }

    private String secondsToMin(long seconds) {
        return "0" + seconds / 60 + ":" + (seconds % 60 >= 10 ? seconds % 60 : ("0" + seconds % 60));
    }

    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel();
    }

    @Override
    public void onBackPressed() {

        if (mBinding.camera.isRecording()) {
            stopRecording();
            file.delete();
        }
        super.onBackPressed();
    }

    private void setStickyStyle(Window window) {
        int flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        window.getDecorView().setSystemUiVisibility(flag);
    }

    /**
     * 监听系统UI的显示，进行特殊处理
     */
    private void setSystemUIChangeListener() {
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            //当系统UI显示的时候（例如输入法显示的时候），再次隐藏
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                setStickyStyle(getWindow());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //焦点改变的时候，当Home键退出，重新从新进入等情况的处理。
        setStickyStyle(getWindow());
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            ToastUtil.showToast(this, "no camera on this device");
            return false;
        }
    }

    //针对非系统影音资源文件夹
    public static void insertIntoMediaStore(Context context, boolean isVideo, File saveFile, long createTime) {
        ContentResolver mContentResolver = context.getContentResolver();
        if (createTime == 0)
            createTime = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        //值一样，但是还是用常量区分对待
        values.put(isVideo ? MediaStore.Video.VideoColumns.DATE_TAKEN
                : MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
        if (!isVideo)
            values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, isVideo ? getVideoMimeType(saveFile.getAbsolutePath())/*"video/3gp"*/ : "image/jpeg");
        //插入
        mContentResolver.insert(isVideo
                ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    // 获取video的mine_type,暂时只支持mp4,3gp
    private static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }
}
