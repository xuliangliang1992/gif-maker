package com.xll.gif.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class GifAnimationDrawable extends AnimationDrawable {

    private volatile int duration;//its volatile because another thread will update its value
    private int currentFrame;
    onPlayListener mPlayListener;

    public void setPlayListener(onPlayListener playListener) {
        mPlayListener = playListener;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        selectDrawable(currentFrame);
    }

    public GifAnimationDrawable() {
        currentFrame = 0;
    }

    @Override
    public void run() {
        int n = getNumberOfFrames();
        currentFrame++;
        if (currentFrame >= n) {
            currentFrame = 0;
        }
        selectDrawable(currentFrame);
        if(mPlayListener!=null){
            mPlayListener.onPlay(currentFrame);
        }
        scheduleSelf(this, SystemClock.uptimeMillis() + duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
        //we have to do the following or the next frame will be displayed after the old duration
        unscheduleSelf(this);
        selectDrawable(currentFrame);
        scheduleSelf(this, SystemClock.uptimeMillis()+duration);
    }

   public interface onPlayListener{
        void onPlay(int currPosition);
    }
}
