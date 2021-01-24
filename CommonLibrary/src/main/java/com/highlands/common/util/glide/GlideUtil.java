package com.highlands.common.util.glide;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.highlands.common.R;
import com.highlands.common.util.StringUtil;
import com.highlands.common.util.SystemUtil;

import androidx.annotation.RawRes;

/**
 * glide加载图片
 * 抽出方法 为了后续链接修改方便
 *
 * @author xll
 * @date 2018/12/7
 */
public class GlideUtil {

    /**
     * 判断用于Glide的Context是否有效
     *
     * @param context 用于Glide的Context
     * @return 用于Glide的Context是否有效
     */
    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return !activity.isDestroyed() && !activity.isFinishing();
        }
        return true;
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (StringUtil.isStringNull(url)) {
            return;
        }
        GlideApp.with(context).load(new MyGlideUrl(url)).into(imageView);
    }

    public static void loadImage2(Context context, String path, ImageView imageView) {
        GlideApp.with(context).load(path).placeholder(R.drawable.loading_gif).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int roundingRadius) {
        if (StringUtil.isStringNull(url)) {
            return;
        }
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(SystemUtil.dip2px(context,roundingRadius));
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        GlideApp.with(context).load(new MyGlideUrl(url)).apply(options).placeholder(R.drawable.loading_gif).into(imageView);
    }

    public static void loadImage1(Context context, @RawRes Integer id, ImageView imageView) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(SystemUtil.dip2px(context,5));
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        //设置图片圆角角度
        GlideApp.with(context).load(id).apply(options).into(imageView);
    }

    public static void preLoad(Context context, String url) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .preload();
    }

    /**
     * 缓存
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void initImageWithFileCache(Context context, String url, ImageView imageView) {
        if (StringUtil.isStringNull(url)) {
            return;
        }
        GlideApp.with(context)
                .load(new MyGlideUrl(url))
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
        /*默认的策略是DiskCacheStrategy.AUTOMATIC
          DiskCacheStrategy有五个常量：
          DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
          DiskCacheStrategy.NONE 不使用磁盘缓存
          DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
          DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
          DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。*/

    }

    /**
     * 跳过缓存
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void initImageNoCache(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .into(imageView);
    }

    /**
     * 内存缓存清理(主线程)
     *
     * @param context
     */
    public static void clearMemoryCache(Context context) {
        GlideApp.get(context).clearMemory();
    }

    /**
     * 磁盘清理缓存(子线程)
     *
     * @param context
     */
    public static void clearFileCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlideApp.get(context).clearDiskCache();
            }
        }).start();
    }

}
