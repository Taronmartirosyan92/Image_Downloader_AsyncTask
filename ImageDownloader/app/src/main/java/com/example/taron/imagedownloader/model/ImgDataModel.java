package com.example.taron.imagedownloader.model;

public class ImgDataModel {
    private String imgNum;
    private String imgUrl;
    private boolean download;

    public ImgDataModel(String imgNum, String imgUrl, boolean download) {
        this.imgNum = imgNum;
        this.imgUrl = imgUrl;
        this.download = download;

    }
    public String getImgNum() {
        return imgNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }
}
