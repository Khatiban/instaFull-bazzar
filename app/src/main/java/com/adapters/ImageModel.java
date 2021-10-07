package com.adapters;

public class ImageModel {
    public String caption;
    public String commentsCount;
    public String date;
    public String fileName;
    public String filePath;
    public int height;
    public String imgId;
    public boolean isChecked;
    public String likesCount;
    public String thumbPath;
    public int type;
    public String userId;
    public String userName;
    public int width;

    public ImageModel(String fullPath, String thumbPAth) {
        this.filePath = fullPath;
        this.thumbPath = thumbPAth;
    }

    public ImageModel(String filePath, String thumbPAth, int type) {
        this.filePath = filePath;
        this.thumbPath = thumbPAth;
        this.type = type;
    }

    public ImageModel(String filePath, String thumbPAth, String fileName, int type) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.thumbPath = thumbPAth;
        this.type = type;
    }

    public ImageModel(String filePath, String fileName, String thumbPAth, String userName, String userID, int type, String caption, String date, String likesCount, String commentsCount, String imgId, int width, int height) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.thumbPath = thumbPAth;
        this.type = type;
        this.caption = caption;
        this.date = date;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.userName = userName;
        this.userId = userID;
        this.imgId = imgId;
        this.width = width;
        this.height = height;
    }
}
