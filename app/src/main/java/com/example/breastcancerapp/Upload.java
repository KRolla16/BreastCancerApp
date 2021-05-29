package com.example.breastcancerapp;



public class Upload {
    private String mName;
    private String image;
    public Upload() {

    }
    public Upload( String imageUrl) {

        image = imageUrl;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        image = image;
    }
}