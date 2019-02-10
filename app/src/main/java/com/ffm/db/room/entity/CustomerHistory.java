package com.ffm.db.room.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerHistory {

    @SerializedName("issueID")
    @Expose
    private Integer issueID;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("closedDate")
    @Expose
    private String closedDate;
    @SerializedName("feedback")
    @Expose
    private String feedback;

    public Integer getIssueID() {
        return issueID;
    }

    public void setIssueID(Integer issueID) {
        this.issueID = issueID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
