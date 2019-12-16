package com.example.mu338.stampinseoul;

public class GpsData {

    private String missionTxtName;
    private String missionTxtContent;
    private int missionImgProfile;

    public GpsData(String missionTxtName, String missionTxtContent, int missionImgProfile) {
        this.missionTxtName = missionTxtName;
        this.missionTxtContent = missionTxtContent;
        this.missionImgProfile = missionImgProfile;
    }

    public GpsData(String missionTxtName, String missionTxtContent) {
        this.missionTxtName = missionTxtName;
        this.missionTxtContent = missionTxtContent;
    }

    public String getMissionTxtName() {
        return missionTxtName;
    }

    public void setMissionTxtName(String missionTxtName) {
        this.missionTxtName = missionTxtName;
    }

    public String getMissionTxtContent() {
        return missionTxtContent;
    }

    public void setMissionTxtContent(String missionTxtContent) {
        this.missionTxtContent = missionTxtContent;
    }

    public int getMissionImgProfile() {
        return missionImgProfile;
    }

    public void setMissionImgProfile(int missionImgProfile) {
        this.missionImgProfile = missionImgProfile;
    }
}


