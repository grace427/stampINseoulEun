package com.example.mu338.stampinseoul;

public class AlbumData {

    private String reviewTxtID;
    private String reviewTxtTitle;
    private String reviewTxtContent;
    private int reviewImgReview;

    public AlbumData(String reviewTxtID, String reviewTxtTitle, String reviewTxtContent, int reviewImgReview) {
        this.reviewTxtID = reviewTxtID;
        this.reviewTxtTitle = reviewTxtTitle;
        this.reviewTxtContent = reviewTxtContent;
        this.reviewImgReview = reviewImgReview;
    }

    public String getReviewTxtID() {
        return reviewTxtID;
    }

    public void setReviewTxtID(String reviewTxtID) {
        this.reviewTxtID = reviewTxtID;
    }

    public String getReviewTxtTitle() {
        return reviewTxtTitle;
    }

    public void setReviewTxtTitle(String reviewTxtTitle) {
        this.reviewTxtTitle = reviewTxtTitle;
    }

    public String getReviewTxtContent() {
        return reviewTxtContent;
    }

    public void setReviewTxtContent(String reviewTxtContent) {
        this.reviewTxtContent = reviewTxtContent;
    }

    public int getReviewImgReview() {
        return reviewImgReview;
    }

    public void setReviewImgReview(int reviewImgReview) {
        this.reviewImgReview = reviewImgReview;
    }
}


