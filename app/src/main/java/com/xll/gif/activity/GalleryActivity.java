package com.xll.gif.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import com.highlands.common.base.BaseActivity;
import com.highlands.common.util.DateUtil;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.adapter.GalleryAdapter;
import com.xll.gif.service.GifMakeService;
import com.xll.gif.ImageFloder;
import com.xll.gif.R;
import com.xll.gif.databinding.GalleryActivityBinding;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author xuliangliang
 * @date 2021/1/12
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GalleryActivity extends BaseActivity {

    private GalleryActivityBinding mBinding;
    private int mPicsSize;
    private List<String> mDirPaths;
    private List<ImageFloder> mImageFloders;
    private File mImgDir;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setAdapterData();//设置图片的显示
            hideLoading();//取消加载框
            //            initListDirPopupWindw();//初始化小相册的popupWindow
            //            initChekcBox();//初始化checkbox集合，防止checkBox的错乱
        }
    };
    private GalleryAdapter mAdapter;
    private List<String> mImgs;
    String[] imgTypes;
    String[] currTypes;

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
        mImageFloders = new ArrayList<>();
        mDirPaths = new ArrayList<>();
        mImgs = new ArrayList<>();
        imgTypes = new String[]{"image/jpeg", "image/png", "image/jpg"};
        currTypes = imgTypes;
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
        mBinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getSelectedPhoto().size() < 3 || mAdapter.getSelectedPhoto().size() > 200) {
                    ToastUtil.showToast(GalleryActivity.this, "selected photo in 2 - 200!");
                    return;
                }
                showLoading();

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
                }, new RxPermissions(GalleryActivity.this));
            }
        });
    }

    private void tryMaker() {

        final File file = FileUtil.createFile(this,DateUtil.getCurrentTimeYMDHMS() + ".gif");
        GifMakeService.startMaking(this, mAdapter.getSelectedPhoto(), file.getAbsolutePath());

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
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;//不获取sd卡根目录下的图片
                    String parentPath = parentFile.getAbsolutePath();//2.获取图片的文件夹信息
                    String parentName = parentFile.getName();
                    ImageFloder imageFloder;//自定义一个model，来保存图片的信息

                    //这个操作，可以提高查询的效率，将查询的每一个图片的文件夹的路径保存到集合中，
                    //如果存在，就直接查询下一个，避免对每一个文件夹进行查询操作
                    if (mDirPaths.contains(parentPath)) {
                        continue;
                    } else {
                        mDirPaths.add(parentPath);//将父路径添加到集合中
                        imageFloder = new ImageFloder();
                        imageFloder.setFirstImagePath(path);
                        imageFloder.setDir(parentPath);
                        imageFloder.setName(parentName);
                    }
                    List<String> strings = null;
                    try {
                        strings = Arrays.asList(parentFile.list(getFilterImage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int picSize = strings.size();//获取每个文件夹下的图片个数
                    imageFloder.setCount(picSize);//传入每个相册的图片个数
                    mImageFloders.add(imageFloder);//添加每一个相册
                    //获取图片最多的文件夹信息（父目录对象和个数，使得刚开始显示的是最多图片的相册
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                mDirPaths = null;
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    //图片筛选器，过滤无效图片
    private FilenameFilter getFilterImage() {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        };
        return filenameFilter;
    }

    //设置适配器数据
    private void setAdapterData() {
        if (mImgDir == null) {
            ToastUtil.showToast(this, "没有查询到图片");
            return;
        }
        //        tv_pop_gallery.setText(mImgDir.getName());
        try {
            mImgs = Arrays.asList(mImgDir.list(getFilterImage()));//获取文件夹下的图片集合
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询出来的图片是正序的，为了让图片按照时间倒序显示，对其倒序操作
        Collections.sort(mImgs, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return -1;
            }
        });

        mAdapter.refresh(mImgs);
        mAdapter.setImgDir(mImgDir);
    }

}

