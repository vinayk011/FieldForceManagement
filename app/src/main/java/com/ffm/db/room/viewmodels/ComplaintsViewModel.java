package com.ffm.db.room.viewmodels;

import android.app.Application;

import com.ffm.db.room.AppDatabase;
import com.ffm.db.room.entity.Complaint;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ComplaintsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Complaint>> complaints = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private LiveData<List<Complaint>> complaintsLiveData;

    public ComplaintsViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }


    public LiveData<List<Complaint>> getComplaints() {
        return complaints;
    }

    public void run(LifecycleOwner lifecycleOwner) {
        clear(lifecycleOwner);
        if (AppPreference.getInstance().getString(AppPrefConstants.USER_ID) != null) {
            complaintsLiveData = appDatabase.complaintsDao().getComplaintsByEmpIDAsLive(AppPreference.getInstance().getString(AppPrefConstants.USER_ID));
            complaintsLiveData.observe(lifecycleOwner, complaintsObserver);
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
        if (complaintsLiveData != null) {
            complaintsLiveData.removeObservers(lifecycleOwner);
        }
    }

    private Observer<List<Complaint>> complaintsObserver = info -> {
        if (info != null) {
            complaints.setValue(info);
        }
    };

}

