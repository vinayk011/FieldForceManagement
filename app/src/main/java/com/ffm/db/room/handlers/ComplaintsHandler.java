package com.ffm.db.room.handlers;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.ffm.FieldForceApplication;
import com.ffm.db.room.AppDatabase;
import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.entity.ReportsInfo;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;

import java.util.List;

public class ComplaintsHandler {
    @SuppressLint("StaticFieldLeak")
    void processComplaintsFromServer(List<Complaint> complaints) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase database = AppDatabase.getDatabase(FieldForceApplication.getInstance());
                String userId = AppPreference.getInstance().getString(AppPrefConstants.USER_ID);
                if (userId != null && complaints != null) {
                    for(Complaint complaint : complaints){
                        Complaint complaintFromDB = database.complaintsDao().getComplaintsByIssueIDAndEmpId(complaint.getIssueID(), userId);
                        if(complaintFromDB == null){
                            complaint.setEmployeeID(userId);
                            database.complaintsDao().insertAll(complaint);
                        }else{
                            complaintFromDB = complaint;
                            complaintFromDB.setEmployeeID(userId);
                            database.complaintsDao().update(complaintFromDB);
                        }
                    }
                }
                return null;
            }
        }.execute();

    }


}
