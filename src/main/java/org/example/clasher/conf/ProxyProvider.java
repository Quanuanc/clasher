package org.example.clasher.conf;

public class ProxyProvider {

    private String url;
    private String path;
    private int interval;

    public ProxyProvider() {
    }

    public ProxyProvider(String url, String path, int interval) {
        this.url = url;
        this.path = path;
        this.interval = interval;
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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "ProxyProvider{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", interval=" + interval +
                '}';
    }
}
