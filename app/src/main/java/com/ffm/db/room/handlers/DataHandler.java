package com.ffm.db.room.handlers;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.ffm.FieldForceApplication;
import com.ffm.db.room.AppDatabase;
import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.entity.Report;

import java.util.List;


public class DataHandler {
    private static DataHandler dataHandler;
    private final int REPORTS = 101;
    private final int COMPLAINTS = 103;
    private final int CLEAR = 105;
    private final int DEVICE_STATS = 102;
    private final String DATE = "date";
    private ReceiverThread receiverThread;

    private class ReceiverThread extends Thread {
        private Handler mHandler;

        Handler getmHandler() {
            return mHandler;
        }

        @Override
        public void run() {
            mHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg != null) {
                        if (REPORTS == msg.what) {
                            List<Report> reportList = (List<Report>) msg.obj;
                            new DevicesHandler().processReportsFromServer(reportList);
                        } else if (COMPLAINTS == msg.what) {
                            List<Complaint> complaints = (List<Complaint>) msg.obj;
                            new ComplaintsHandler().processComplaintsFromServer(complaints);
                        }
                    }
                }
            };
        }
    }

    private void clearDataFromDB() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase appDB = AppDatabase.getDatabase(FieldForceApplication.getInstance());
                appDB.reportsDao().emptyReports();
                return null;
            }
        }.execute();
    }

    private DataHandler() {
        receiverThread = new ReceiverThread();
        receiverThread.run();
    }

    public static void init() {
        if (dataHandler == null) {
            dataHandler = new DataHandler();
        }
    }

    public static DataHandler getInstance() {
        if (dataHandler == null) {
            throw new NullPointerException("Initialize before use.");
        }
        return dataHandler;
    }

    public void clearData() {
        Message message = new Message();
        message.what = CLEAR;
        receiverThread.getmHandler().sendMessage(message);
    }

    public void addReportsToDb(List<Report> reportsList) {
        Message message = new Message();
        message.what = REPORTS;
        message.obj = reportsList;
        receiverThread.getmHandler().sendMessage(message);
    }

    public void addComplaintsToDb(List<Complaint> complaints) {
        Message message = new Message();
        message.what = COMPLAINTS;
        message.obj = complaints;
        receiverThread.getmHandler().sendMessage(message);
    }

}
