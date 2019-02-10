package com.ffm.db.room.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Complaint implements Comparable, Serializable {
    @PrimaryKey
    @SerializedName("issueID")
    @Expose
    private int issueID;
    @SerializedName("employeeID")
    @Expose
    private String employeeID;
    @SerializedName("issueCategoryType")
    @Expose
    private String issueType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerMobile")
    @Expose
    private String customerMobile;
    @SerializedName("complaintDate")
    @Expose
    private String complaintDate;
    @SerializedName("severity")
    @Expose
    private String severity;
    @SerializedName("customerLocation")
    @Expose
    private LocationInfo customerLocation;
    @SerializedName("employeelocation")
    @Expose
    private LocationInfo employeeLocation;
    @SerializedName("issueStatus")
    @Expose
    private String issueStatus;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("reachedLocation")
    @Expose
    private boolean reachedLocation;

    @SerializedName("customerHistory")
    @Expose
    private List<CustomerHistory> customerHistory;


    /*@SerializedName("customerHistory")
    @Expose
    private List<String> customerHistory;*/

    private boolean changed;


    @Override
    public int compareTo(Object o) {
        Complaint compare = (Complaint) o;

        if (compare.getDescription().equals(this.description) && compare.getIssueType().equals(this.issueType)) {
            return 0;
        }
        return 1;
    }

    public Integer getIssueID() {
        return issueID;
    }

    public void setIssueID(Integer issueID) {
        this.issueID = issueID;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public LocationInfo getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(LocationInfo customerLocation) {
        this.customerLocation = customerLocation;
    }

    public LocationInfo getEmployeeLocation() {
        return employeeLocation;
    }

    public void setEmployeeLocation(LocationInfo customerLocation) {
        this.employeeLocation = customerLocation;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

   /* public List<String> getCustomerHistory() {
        return customerHistory;
    }

    public void setCustomerHistory(List<String> customerHistory) {
        this.customerHistory = customerHistory;
    }*/

    public boolean isReachedLocation() {
        return reachedLocation;
    }

    public void setReachedLocation(boolean reachedLocation) {
        this.reachedLocation = reachedLocation;
    }

    public List<CustomerHistory> getCustomerHistory() {
        return customerHistory;
    }

    public void setCustomerHistory(List<CustomerHistory> customerHistory) {
        this.customerHistory = customerHistory;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
