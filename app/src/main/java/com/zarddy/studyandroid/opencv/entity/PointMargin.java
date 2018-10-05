package com.zarddy.studyandroid.opencv.entity;

import com.alibaba.fastjson.JSON;

public class PointMargin {

    private int xMargin;
    private int yMargin;

    public int getxMargin() {
        return xMargin;
    }

    public void setxMargin(int xMargin) {
        this.xMargin = xMargin;
    }

    public int getyMargin() {
        return yMargin;
    }

    public void setyMargin(int yMargin) {
        this.yMargin = yMargin;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
