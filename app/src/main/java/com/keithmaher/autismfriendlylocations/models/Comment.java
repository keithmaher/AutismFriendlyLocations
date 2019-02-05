package com.keithmaher.autismfriendlylocations.models;

public class Comment {

    public String commentId;
    public String commentName;
    public String commentMain;

    public Comment() {
    }

    public Comment(String commentId, String commentName, String commentMain) {
        this.commentId = commentId;
        this.commentName = commentName;
        this.commentMain = commentMain;
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
}
