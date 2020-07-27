package com.imooc.o2o.dto;

import java.io.InputStream;

public class ImageHolder {

    private String fileName;

    private InputStream filePath;

    public ImageHolder(String fileName, InputStream filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public ImageHolder(){

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getFilePath() {
        return filePath;
    }

    public void setFilePath(InputStream filePath) {
        this.filePath = filePath;
    }
}
