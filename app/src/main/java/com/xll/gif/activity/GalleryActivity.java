package com.xll.gif.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import com.highlands.common.base.BaseRewardedAdActivity;
import com.highlands.common.dialog.DialogClickListener;
import com.highlands.common.dialog.DialogManager;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.MainApplication;
import com.xll.gif.R;
import com.xll.gif.adapter.GalleryAdapter;
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
public class GalleryActivity extends BaseRewardedAdActivity {

    private GalleryActivityBinding mBinding;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAdapterData();//设置图片的显示
            hideLoading();//取消加载框
        }
    };
    private GalleryAdapter mAdapter;
    private List<String> mImgs;
    private String[] imgTypes;
    private String[] currTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.gallery_activity);
        mAdapter = new GalleryAdapter();
        mBinding.rvList.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.rvList.setAdapter(mAdapter);
        initData();
        initListener();

    }

    @Override
    protected void initData() {
        mImgs = new ArrayList<>();
        imgTypes = new String[]{"image/jpeg", "image/png", "image/jpg"};
        currTypes = imgTypes;


    }

    @Override
    protected void onResume() {
        super.onResume();
        getImages();
    }

    @Override
    protected void initListener() {
        mAdapter.setGalleryClickListener(new GalleryAdapter.onGalleryClickListener() {
            @Override
            public void openBottomDialog() {
                PermissionUtil.launchCamera(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        startActivity(new Intent(GalleryActivity.this, CameraActivity.class).putExtra("isImage", true));
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {

                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                    }
                }, new RxPermissions(GalleryActivity.this));
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
                ArrayList<Bitmap> bitmaps = mAdapter.getSelectedPhoto();
                if (mAdapter.getSelectedPhoto().size() < 3 || bitmaps.size() > 200) {
                    ToastUtil.showToast(GalleryActivity.this, "selected photo in 2 - 200!");
                    return;
                }
                if (MainApplication.isAuth() || MainApplication.isAdAuth()) {
                    Intent intent = new Intent(GalleryActivity.this, EditGif2Activity.class);
                    MainApplication.setBitmaps(bitmaps);
                    intent.putExtra("from", true);
                    startActivity(intent);
                } else {
                    DialogManager.getInstance().showAuthDialog(GalleryActivity.this,
                            new DialogClickListener() {
                                @Override
                                public void leftClickListener() {
                                    playAd();
                                }

                                @Override
                                public void rightClickListener() {
                                    ToastUtil.showToast(GalleryActivity.this, "try for free");
                                    //                                                                          if (MainApplication.isAuth() || MainApplication.isAdAuth()) {
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
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = GalleryActivity.this.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        currTypes,
                        MediaStore.Images.Media.DATE_TAKEN + " DESC");//获取图片的cursor，按照时间倒序（发现没卵用)

                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));// 1.获取图片的路径
                    mImgs.add(path);
                }
                mCursor.close();
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    //设置适配器数据
    private void setAdapterData() {
        //查询出来的图片是正序的，为了让图片按照时间倒序显示，对其倒序操作
//        Collections.sort(mImgs, new Comparator<String>() {
//            @Override
//            public int compare(String lhs, String rhs) {
//                return -1;
//            }
//        });
        mImgs.add(0, "");
        mAdapter.refresh(mImgs);
    }

}

