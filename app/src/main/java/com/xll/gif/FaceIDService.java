package com.xll.gif;

import com.xll.gif.fragment.GifListFragment;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 接口
 *
 * @author xll
 * @date 2018/12/4
 */
@SuppressWarnings("rawtypes")
public interface FaceIDService {


    @GET("listgif.php")
    Observable<List<GifListFragment.GifBean>> getGifs();

}

