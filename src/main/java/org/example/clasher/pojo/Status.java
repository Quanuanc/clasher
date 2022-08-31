package org.example.clasher.pojo;

import java.util.Date;

public class Status {
    private String path;
    private Date date;

    public Status(String path, Date date) {
        this.path = path;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
