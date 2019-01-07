package com.ffm.db;


import java.io.Serializable;

import androidx.room.Ignore;


public class Device implements Comparable, Serializable {
    private String device;
    private String name;
    @Ignore
    private boolean changed;

    public Device(String device, String name) {
        this.device = device;
        this.name = name;
    }

    public Device() {
    }

    @Override
    public int compareTo(Object o) {
        Device compare = (Device) o;

        if (compare.device.equals(this.device) && compare.name.equals(this.name)) {
            return 0;
        }
        return 1;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String deviceId) {
        this.device = deviceId;
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
