package com.xll.gif;

/**
 * @author xuliangliang
 * @date 2021/1/16
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class BitmapEntity {
    String title;
    String path;
    long size;
    String uri_thumb;
    long duration;

    public BitmapEntity() {

    }

    public BitmapEntity(String title, String path, long size, String uri_thumb, long duration) {
        this.title = title;
        this.path = path;
        this.size = size;
        this.uri_thumb = uri_thumb;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUri_thumb() {
        return uri_thumb;
    }

    public void setUri_thumb(String uri_thumb) {
        this.uri_thumb = uri_thumb;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
