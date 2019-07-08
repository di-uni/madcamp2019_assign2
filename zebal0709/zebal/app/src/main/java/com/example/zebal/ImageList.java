package com.example.zebal;

public class ImageList {
    String id;
    String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String _img) {
        this.image = _img;
    }
    public ImageList() {
    }

    public ImageList(String _id, String _img) {
        this.id = _id;
        this.image = _img;
    }
}
