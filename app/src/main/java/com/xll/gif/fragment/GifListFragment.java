package com.xll.gif.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.highlands.common.BaseConstant;
import com.highlands.common.base.fragment.BaseFragment;
import com.highlands.common.schedulers.SchedulerProvider;
import com.highlands.common.util.FileUtil;
import com.highlands.common.util.PermissionUtil;
import com.highlands.common.util.SharePreferenceUtil;
import com.highlands.common.util.ShareUtil;
import com.highlands.common.util.StringUtil;
import com.highlands.http.AppRetrofit;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xll.gif.FaceIDService;
import com.xll.gif.R;
import com.xll.gif.adapter.GifGhyAdapter;
import com.xll.gif.databinding.ListFragmentBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/10
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifListFragment extends BaseFragment {
    ListFragmentBinding mBinding;
    GifGhyAdapter mAdapter;
    List<GifBean> list;
    Gson mGson;

    public static GifListFragment newInstance() {
        return new GifListFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.list_fragment;
    }

    @Override
    public void initView(View view) {
        mBinding = DataBindingUtil.bind(view);
        mBinding.rvList.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mAdapter = new GifGhyAdapter();
        mBinding.rvList.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

        mAdapter.setGalleryClickListener(new GifGhyAdapter.onGalleryClickListener() {
            @Override
            public void onImageDownload(int position, String url, GifBean gifBean) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading();

                    }
                });
                PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    File file = Glide.with(mActivity).downloadOnly().load(url).submit().get();
                                    File target = FileUtil.createFile(mActivity, getFileName(url));
                                    copy(file, target);

                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            gifBean.setPath(target.getAbsolutePath());
                                            list.set(position, gifBean);
                                            mAdapter.refresh(list);
                                            SharePreferenceUtil.getInstance().put(mActivity,
                                                    BaseConstant.SHARED_PREFERENCE_FILE_NAME,
                                                    SharePreferenceUtil.RESOURCE,
                                                    mGson.toJson(list)
                                            );
                                        }
                                    });
                                } catch (ExecutionException | InterruptedException ex) {
                                    Timber.tag(TAG).e(ex);
                                }
                                hideLoading();
                            }
                        }.start();
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissions) {

                    }

                    @Override
                    public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                    }
                }, new RxPermissions(mActivity));

            }

            @Override
            public void onImageShare(int position, String path) {
                if (StringUtil.isStringNull(path)) {
                    showToast("this picture is no exist!");
                    updateAdapter(position, "");
                } else {
                    ShareUtil.shareImage(mActivity, getImageContentUri(mActivity, path), "Share");
                }
            }

        });
    }

    @Override
    public void initData() {
        mGson = new Gson();
        String resource = (String) SharePreferenceUtil.getInstance().get(mActivity,
                BaseConstant.SHARED_PREFERENCE_FILE_NAME, SharePreferenceUtil.RESOURCE, "");
        //        if (StringUtil.isStringNull(resource)) {
        //            list = new ArrayList<>();
        //            GifBean gifBean1 = new GifBean();
        //            gifBean1.setRawId(R.raw.gif4bd);
        //            list.add(gifBean1);
        //            GifBean gifBean2 = new GifBean();
        //            gifBean2.setRawId(R.raw.gif5bf);
        //            list.add(gifBean2);
        //            GifBean gifBean3 = new GifBean();
        //            gifBean3.setRawId(R.raw.gif5c4);
        //            list.add(gifBean3);
        //            GifBean gifBean4 = new GifBean();
        //            gifBean4.setRawId(R.raw.gif6c7);
        //            list.add(gifBean4);
        //            GifBean gifBean5 = new GifBean();
        //            gifBean5.setRawId(R.raw.gif6c9);
        //            list.add(gifBean5);
        //            GifBean gifBean6 = new GifBean();
        //            gifBean6.setRawId(R.raw.gif7cc);
        //            list.add(gifBean6);
        //            GifBean gifBean7 = new GifBean();
        //            gifBean7.setRawId(R.raw.gif7cf);
        //            list.add(gifBean7);
        //            GifBean gifBean8 = new GifBean();
        //            gifBean8.setRawId(R.raw.gif8d2);
        //            list.add(gifBean8);
        //            GifBean gifBean9 = new GifBean();
        //            gifBean9.setRawId(R.raw.gif8d5);
        //            list.add(gifBean9);
        //            GifBean gifBean10 = new GifBean();
        //            gifBean10.setRawId(R.raw.gif9d7);
        //            list.add(gifBean10);
        //            GifBean gifBean11 = new GifBean();
        //            gifBean11.setRawId(R.raw.gif9d9);
        //            list.add(gifBean11);
        //            GifBean gifBean12 = new GifBean();
        //            gifBean12.setRawId(R.raw.gifae2);
        //            list.add(gifBean12);
        //            GifBean gifBean13 = new GifBean();
        //            gifBean13.setRawId(R.raw.gifae4);
        //            list.add(gifBean13);
        //            GifBean gifBean14 = new GifBean();
        //            gifBean14.setRawId(R.raw.gifbe6);
        //            list.add(gifBean14);
        //
        //
        //            //        list.add(R.raw.gifbe8);
        //            //        list.add(R.raw.gifcea);
        //            //        list.add(R.raw.gifcee);
        //            //        list.add(R.raw.gifdf2);
        //            //        list.add(R.raw.gifdf4);
        //            //        list.add(R.raw.gifef7);
        //            //        list.add(R.raw.gifef9);
        //            //        list.add(R.raw.gifffb);
        //        } else {
        //            list = mGson.fromJson(resource, new TypeToken<List<GifBean>>() {
        //            }.getType());
        //
        //        }
        //
        //        mAdapter.refresh(list);
        if (!StringUtil.isStringNull(resource)) {
            list = mGson.fromJson(resource, new TypeToken<List<GifBean>>() {
            }.getType());
            mAdapter.refresh(list);
        } else {
            new AppRetrofit().getRetrofit().create(FaceIDService.class)
                    .getGifs()
                    .subscribeOn(SchedulerProvider.getInstance().io())
                    .observeOn(SchedulerProvider.getInstance().ui())
                    .subscribe(new Observer<List<GifBean>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            mCompositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(@NonNull List<GifBean> gifBeans) {
                            Timber.tag(TAG).i("gifBeans " + gifBeans.size());
                            list = gifBeans;
                            mAdapter.refresh(list);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.tag(TAG).e(e);

                        }

                        @Override
                        public void onComplete() {
                            Timber.tag(TAG).i("onComplete");
                        }
                    });
        }
    }

    @Override
    public void setPresenter() {

    }

    public class GifBean {
        String path;
        String id;
        String url;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getId() {
            return id;
        }

        public void setId(String rawId) {
            this.id = rawId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    private void updateAdapter(int position, String path) {
        GifBean gifBean = list.get(position);
        gifBean.setPath(path);
        list.set(position, gifBean);
        mAdapter.notifyItemChanged(position);
        SharePreferenceUtil.getInstance().put(mActivity, BaseConstant.SHARED_PREFERENCE_FILE_NAME,
                SharePreferenceUtil.RESOURCE, mGson.toJson(list));

    }

    /**
     * 从url中获取文件名
     *
     * @param url 路径
     * @return 名称
     */
    public String getFileName(String url) {
        if (StringUtil.isStringNull(url)) {
            return null;
        }
        int start = url.lastIndexOf("/");
        int end = url.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return url.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 复制文件
     *
     * @param source 输入文件
     * @param target 输出文件
     */
    public void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), target.getAbsolutePath(), "", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(target);
        Timber.tag("GifMakeService").i("imageUri------>" + uri);
        //imageUri------>file:///storage/emulated/0/MyAndroidBase/mybase/1574394370534.jpg
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        mActivity.sendBroadcast(localIntent);

    }

    public Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }
}
