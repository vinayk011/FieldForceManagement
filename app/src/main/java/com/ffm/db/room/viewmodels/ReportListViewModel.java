package com.ffm.db.room.viewmodels;

import android.app.Application;

import com.ffm.db.room.AppDatabase;
import com.ffm.db.room.entity.ReportsInfo;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ReportListViewModel extends AndroidViewModel {
    private MutableLiveData<ReportsInfo> reportsInfo = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private LiveData<ReportsInfo> reportLiveData;

    public ReportListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }


    public LiveData<ReportsInfo> getReports() {
        return reportsInfo;
    }

    public void run(LifecycleOwner lifecycleOwner) {
        clear(lifecycleOwner);
        if (AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE) != null) {
            reportLiveData = appDatabase.reportsDao().getReportsByIDAsLive(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
            reportLiveData.observe(lifecycleOwner, reportsObserver);
        }


       /* new AsyncTask<Void, Void, DevicesInfo>() {
            @Override
            protected void onPostExecute(DevicesInfo devicesInfo) {
                if (devicesInfo != null) {
                    Trace.i("Device List:" + devicesInfo.toString());
                    deviceInfo.setValue(devicesInfo);
                }
            }

            @Override
            protected DevicesInfo doInBackground(Void... voids) {
                if (AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE) != null)
                    stepsLiveData = appDatabase.devicesDao().getDevicesByIDAsLive(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
                    stepsLiveData.observe(lifecycleOwner, stepsObserver);
                    return appDatabase.devicesDao().getDeviceByID(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
                else return null;
            }
        }.execute();
*/
// do async operation to fetch devices
        /*Handler myHandler = new Handler();
        myHandler.postDelayed(() -> {
            devices.postValue(appDatabase.devicesDao().getAll());
        }, 5000);*/
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void clear(LifecycleOwner lifecycleOwner) {
        if (reportLiveData != null) {
            reportLiveData.removeObservers(lifecycleOwner);
        }
    }

    private Observer<ReportsInfo> reportsObserver = info -> {
        if (info != null) {
            reportsInfo.setValue(info);
        }
    };

}
