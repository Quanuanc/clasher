package org.example.clasher.conf;

import java.util.Date;

public class MmdbProvider extends Provider {
    private String url;
    private String path;
    private Date updateTime;

    public MmdbProvider() {
    }

    public MmdbProvider(String url, String path, Date updateTime) {
        this.url = url;
        this.path = path;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MmdbProvider{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
