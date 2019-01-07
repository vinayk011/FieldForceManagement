package com.ffm.db.room.entity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ReportsInfo {
    @NonNull
    @PrimaryKey
    private String userId;
    private List<Report> reports;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> devices) {
        this.reports = devices;
    }
}
