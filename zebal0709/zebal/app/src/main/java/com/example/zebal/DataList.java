package com.example.zebal;

public class DataList {
    String id;
    String name;
    String number;
    String profileImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public DataList(String id, String name, String number, String profileImage) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.profileImage = profileImage;
    }
}