package com.zhy.graph.bean;

import java.io.Serializable;

/**
 * Created by yuzhuo on 2017/9/5.
 */
public class TaskInfo implements Serializable{
    private String uri;
    private String description;
    private String title;
    private boolean done;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
