package com.ffm.db.room.entity;


import java.io.Serializable;

import androidx.room.Ignore;


public class Report implements Comparable, Serializable {
    private int complaintId;
    private String type;
    private String description;
    private String reportedBy;
    private String locationAddress;
    private long reportedTime;
    private String complaintStatus;
    private double lat;
    private double lng;

    @Ignore
    private boolean changed;



    public Report() {
    }

    public Report(int complaintId, String Type, String description, String reportedBy, String locationAddress, long reportedTime, String complaintStatus, double lat, double lng) {
        this.complaintId = complaintId;
        this.type = Type;
        this.description = description;
        this.reportedBy = reportedBy;
        this.locationAddress = locationAddress;
        this.reportedTime = reportedTime;
        this.complaintStatus = complaintStatus;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int compareTo(Object o) {
        Report compare = (Report) o;

        if (compare.description.equals(this.description) && compare.type.equals(this.type)) {
            return 0;
        }
        return 1;
    }


    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
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
