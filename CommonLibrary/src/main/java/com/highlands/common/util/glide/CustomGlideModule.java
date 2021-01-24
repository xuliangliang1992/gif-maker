package com.highlands.common.util.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.highlands.common.util.FileUtil;

/**
 * @author xll
 * @date 2018/12/10
 */
@GlideModule
public final class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //        设置缓存大小为20mb
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        //        设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //        根据SD卡是否可用选择是在内部缓存还是SD卡缓存
        if(FileUtil.sdCardExist()){
            builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "HYManagerImages", memoryCacheSizeBytes));
        }else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "HYManagerImages", memoryCacheSizeBytes));
        }
    }
    //    针对V4用户可以提升速度
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

