package com.wangzy.xspider;

/**
 * Created by wangzy on 15/11/12.
 */
public class DownLoadTask {

    public String url;

    public DownLoadTask() {
    }

    public DownLoadTask(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
