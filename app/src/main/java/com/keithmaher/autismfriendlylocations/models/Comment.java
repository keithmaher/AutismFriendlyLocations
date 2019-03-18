package com.keithmaher.autismfriendlylocations.models;

import java.util.UUID;

public class Comment extends Location {

    public String commentId;
    public String commentName;
    public String commentMain;
    public String commentDate;
    public String commentUserImageURL;

    public Comment() {
    }

    public Comment(String commentName, String commentMain, String commentDate, String commentUserImageURL) {
        this.commentId = UUID.randomUUID().toString();
        this.commentName = commentName;
        this.commentMain = commentMain;
        this.commentDate = commentDate;
        this.commentUserImageURL = commentUserImageURL;
    }

    public Comment(String commentName, String commentMain, String commentDate) {
        this.commentId = UUID.randomUUID().toString();
        this.commentName = commentName;
        this.commentMain = commentMain;
        this.commentDate = commentDate;
    }


    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentMain() {
        return commentMain;
    }

    public void setCommentMain(String commentMain) {
        this.commentMain = commentMain;
    }

    public String getCommentUserImageURL() {
        return commentUserImageURL;
    }

    public void setCommentUserImageURL(String commentUserImageURL) {
        this.commentUserImageURL = commentUserImageURL;
    }
}
