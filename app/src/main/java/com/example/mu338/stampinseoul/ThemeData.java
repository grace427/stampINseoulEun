package com.example.mu338.stampinseoul;

import java.io.Serializable;

public class ThemeData implements Serializable {

    private String title;
    private String firstImage;
    private String addr;
    private String tel;
    private String overView;
    private double mapX;
    private double mapY;
    private boolean hart=false;

    public boolean isHart() {
        return hart;
    }

    public void setHart(boolean hart) {
        this.hart = hart;
    }

    private Integer contentsID;

    public Integer getContentsID() {
        return contentsID;
    }

    public void setContentsID(Integer contentsID) {
        this.contentsID = contentsID;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }
}
