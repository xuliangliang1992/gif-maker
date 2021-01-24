package com.xll.gif.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.highlands.common.base.fragment.BaseFragment;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.R;
import com.xll.gif.activity.EditGif2Activity;
import com.xll.gif.adapter.GifGalleryAdapter;
import com.xll.gif.databinding.EditFragmentBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @author xuliangliang
 * @date 2021/1/10
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifEditFragment extends BaseFragment {

    private EditFragmentBinding mBinding;

    public static GifEditFragment newInstance() {
        return new GifEditFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.edit_fragment;
    }

    @Override
    public void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        mBinding.rvList.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mAdapter = new GifGalleryAdapter();
        mBinding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mAdapter.setGalleryClickListener(new GifGalleryAdapter.onGalleryClickListener() {
            @Override
            public void onImageSelect(String picPath) {
                Intent intent = new Intent(mActivity, EditGif2Activity.class);
                intent.putExtra("filePath", picPath);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

        imgTypes = new String[]{"image/gif"};

    }

    @Override
    public void setPresenter() {

    }

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
    private GifGalleryAdapter mAdapter;
    private ObservableArrayList<String> mImgs;
    String[] imgTypes;

    //获取图片的路径和父路径 及 图片size
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.showToast(mActivity, "检测到没有内存卡");
            return;
        }
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mActivity.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri,
                        null,
                        MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/gif"},
                        MediaStore.Images.Media.DATE_ADDED + " DESC");//获取图片的cursor，按照时间倒序（发现没卵用)

                while (mCursor.moveToNext()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));// 1.获取图片的路径
                    String title = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
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
        Collections.sort(mImgs, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return -1;
            }
        });
        Log.i(TAG, "setAdapterData: " + mImgs.size());
        mAdapter.refresh(mImgs);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImgs = new ObservableArrayList<>();
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                getImages();
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {

            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

            }
        }, new RxPermissions(this));
    }
}
