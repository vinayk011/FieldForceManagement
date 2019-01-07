package com.ffm.db.room.handlers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;


import com.ffm.FieldForceApplication;
import com.ffm.db.room.AppDatabase;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.entity.ReportsInfo;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;

import java.util.List;

public class DevicesHandler {
    @SuppressLint("StaticFieldLeak")
    void processReportsFromServer(List<Report> deviceList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase database = AppDatabase.getDatabase(FieldForceApplication.getInstance());
                String userId = AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE);
                if (userId != null && deviceList != null) {
                    ReportsInfo reportfromDb = database.reportsDao().getReportByID(userId);
                    if (reportfromDb == null) {
                        reportfromDb = new ReportsInfo();
                        reportfromDb.setUserId(userId);
                        reportfromDb.setReports(deviceList);
                        database.reportsDao().insertAll(reportfromDb);
                    } else {
                        reportfromDb.setReports(deviceList);
                        database.reportsDao().update(reportfromDb);
                    }
                }
                return null;
            }
        }.execute();

    }


}
