package com.ffm.model;

import java.util.HashMap;
import java.util.Map;

public class IssueHistory {
    private Integer issueID;
    private String updatedDate;
    private String status;

    public Integer getIssueID() {
        return issueID;
    }

    public void setIssueID(Integer issueID) {
        this.issueID = issueID;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IssueHistory{" +
                "issueID=" + issueID +
                ", updatedDate='" + updatedDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
