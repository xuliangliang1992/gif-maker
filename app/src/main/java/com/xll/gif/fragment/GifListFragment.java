package com.xll.gif.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            public void onImageDownload(int position, String url) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLoading();

                    }
                });
                //                saveImageToGallery(mActivity, position, id);
                PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        //                            download(url);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555243545061&di=26dfcd1e30fad29adc2fb2ba06a042c3&imgtype=0&src=http%3A%2F%2Fs7.sinaimg.cn%2Forignal%2F0063R5gqzy7maPm9Z4y46%26690";
                                    File file = Glide.with(mActivity).downloadOnly().load(url).submit().get();
                                    Log.d(TAG, "file: " + file);
                                    copy(file, FileUtil.createFile(getFileName(url), FileUtil.FILE_TYPE_GIF));
                                } catch (ExecutionException | InterruptedException ex) {
                                    Log.e(TAG, null, ex);
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
                    ShareUtil.shareImage(mActivity, Uri.parse(path), "Share");
                }
            }

        });
    }

    /**
     * 复制文件
     *
     * @param filename 文件名
     * @param bytes    数据
     */
    public void copy(String filename, byte[] bytes) {
        try {
            //如果手机已插入sd卡,且app具有读写sd卡的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream output = null;
                output = new FileOutputStream(filename);
                output.write(bytes);
                Log.i(TAG, "copy: success，" + filename);
                output.close();
            } else {
                Log.i(TAG, "copy:fail, " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        mAdapter.refresh(gifBeans);
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

    @Override
    public void setPresenter() {

    }

    public void saveImageToGallery(Context context, int position, int id) {
        InputStream inStream = context.getResources().openRawResource(id);
        // 首先保存图片
        String fileName = System.currentTimeMillis() + ".gif";
        File file = FileUtil.createFile(fileName, FileUtil.FILE_TYPE_GIF);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            //            Android10SaveFile.saveImage2Public(mActivity,fileName,bs,"");
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        //        try {
        //            MediaStore.Images.Media.insertImage(context.getContentResolver(),
        //                    file.getAbsolutePath(), fileName, fileName);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //        }
        //        // 最后通知图库更新
        //        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));

        updateAdapter(position, file.getAbsolutePath());
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });
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
    public  void copy(File source, File target) {
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
    }
}
