package com.ffm.db.room.entity;


import java.io.Serializable;

import androidx.room.Ignore;


public class Report implements Comparable, Serializable {
    private String reportType;
    private String reportName;
    private String reportedBy;
    private String locationAddress;
    private long reportedTime;
    private String reportStatus;
    private double lat;
    private double lng;

    @Ignore
    private boolean changed;



    public Report() {
    }

    public Report(String reportType, String reportName, String reportedBy, String locationAddress, long reportedTime, String reportStatus, double lat, double lng) {
        this.reportType = reportType;
        this.reportName = reportName;
        this.reportedBy = reportedBy;
        this.locationAddress = locationAddress;
        this.reportedTime = reportedTime;
        this.reportStatus = reportStatus;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int compareTo(Object o) {
        Report compare = (Report) o;

        if (compare.reportName.equals(this.reportName) && compare.reportType.equals(this.reportType)) {
            return 0;
        }
        return 1;
    }


    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
