package com.example.e_commerce_1.activities;

public class FeedbackModel {

    // string variable for
    // storing employee name.
    private String feedbackName;

    // string variable for storing
    // employee contact number
    private String feedbackMail;

    // string variable for storing
    // employee address.
    private String feedbackDescription;

    // string variable for storing
    // employee address.
    private String feedbackRating;

    public FeedbackModel() {
    }

    public FeedbackModel(String feedbackName, String feedbackMail, String feedbackDescription, String feedbackRating) {
        this.feedbackName = feedbackName;
        this.feedbackMail = feedbackMail;
        this.feedbackDescription = feedbackDescription;
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackName() {
        return feedbackName;
    }

    public void setFeedbackName(String feedbackName) {
        this.feedbackName = feedbackName;
    }

    public String getFeedbackMail() {
        return feedbackMail;
    }

    public void setFeedbackMail(String feedbackMail) {
        this.feedbackMail = feedbackMail;
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public String getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(String feedbackRating) {
        this.feedbackRating = feedbackRating;
    }
}
