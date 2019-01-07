package com.ffm.db.room.entity;


import java.io.Serializable;

import androidx.room.Ignore;


public class Report implements Comparable, Serializable {
    private String report;
    private String name;
    @Ignore
    private boolean changed;

    public Report(String report, String name) {
        this.report = report;
        this.name = name;
    }

    public Report() {
    }

    @Override
    public int compareTo(Object o) {
        Report compare = (Report) o;

        if (compare.report.equals(this.report) && compare.name.equals(this.name)) {
            return 0;
        }
        return 1;
    }


    public String getReport() {
        return report;
    }

    public void setReport(String deviceId) {
        this.report = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
