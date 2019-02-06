package com.ffm.db.room;

import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.entity.Report;
import com.ffm.util.GsonUtil;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Created by Raviteja on 26-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date value) {
        if (value == null) {
            return null;
        } else {
            return value.getTime();
        }
    }

    @TypeConverter
    public static List<Report> stringToReportList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<List<Report>>() {
        }.getType());
    }

    @TypeConverter
    public static String ReportListToString(List<Report> data) {
        return GsonUtil.getGson().toJson(data);
    }

    @TypeConverter
    public static List<Complaint> stringToComplaintList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return GsonUtil.getGson().fromJson(data, new TypeToken<List<Complaint>>() {
        }.getType());
    }

    @TypeConverter
    public static String ComplaintListToString(List<Complaint> data) {
        return GsonUtil.getGson().toJson(data);
    }

}
