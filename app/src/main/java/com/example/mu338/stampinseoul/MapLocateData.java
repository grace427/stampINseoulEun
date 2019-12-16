package com.example.mu338.stampinseoul;

public class MapLocateData {

    private int imgProfile;

    private String txtName;
    private String txtContent;

    private Double latitude;
    private Double longitude;

    public MapLocateData(String txtName, String txtContent, Double latitude, Double longitude) {
        this.txtName = txtName;
        this.txtContent = txtContent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(int imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtContent() {
        return txtContent;
    }

    public void setTxtContent(String txtContent) {
        this.txtContent = txtContent;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
