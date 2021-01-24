package com.xll.gif.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.BitmapEntity;
import com.xll.gif.R;
import com.xll.gif.adapter.VideoGalleryAdapter;
import com.xll.gif.databinding.GalleryActivityBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class VideoListActivity extends BaseActivity {

    private GalleryActivityBinding mBinding;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.refresh(bit);
            hideLoading();//取消加载框
        }
    };

    private VideoGalleryAdapter mAdapter;
    private ArrayList<BitmapEntity> bit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.gallery_activity);
        mBinding.tvSave.setVisibility(View.GONE);
        mAdapter = new VideoGalleryAdapter();
        mBinding.rvList.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.rvList.setAdapter(mAdapter);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        bit = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getImages();
    }

    @Override
    protected void initListener() {
        mBinding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAdapter.setGalleryClickListener(new VideoGalleryAdapter.onGalleryClickListener() {
            @Override
            public void onImageSelect(String picPath) {
                startActivity(new Intent(VideoListActivity.this, VideoEditActivity.class).putExtra("videoPath", picPath));
            }

            @Override
            public void toCamera() {
                PermissionUtil.launchCamera(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        startActivity(new Intent(VideoListActivity.this, CameraActivity.class).putExtra("isImage", false));
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {

                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                    }
                }, new RxPermissions(VideoListActivity.this));

            }
        });
    }

    //获取图片的路径和父路径 及 图片size
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showToast(this, "检测到没有内存卡");
            return;
        }
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = {
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE
                };

                /**
                 * 根据视频文件格式 进行数据库查询
                 */
                String where = MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=? or "
                        + MediaStore.Video.Media.MIME_TYPE + "=?";

                String[] whereArgs = {"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
                        "video/mkv", "video/mov", "video/mpg"};
                Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = VideoListActivity.this.getContentResolver();
                Cursor cursor = mContentResolver.query(mImageUri, projection, where, whereArgs, MediaStore.Video.Media.DATE_ADDED + " DESC ");//获取图片的cursor，按照时间倒序（发现没卵用)

                try {
                    while (cursor.moveToNext()) {
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                        /**
                         * 过滤文件大小
                         */
                        if (size < 600 * 1024 * 1024) {
                            BitmapEntity vedioBean = new BitmapEntity();
                            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                            long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                            /**
                             * 设置视频实体属性
                             */
                            vedioBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                            vedioBean.setDuration(duration);
                            vedioBean.setPath(path);
                            vedioBean.setSize(size);
                            vedioBean.setUri_thumb(path);
                            bit.add(vedioBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

}

