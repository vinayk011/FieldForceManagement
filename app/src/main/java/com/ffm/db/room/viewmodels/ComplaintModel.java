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

public class ComplaintModel extends AndroidViewModel {
    private MutableLiveData<Complaint> complaint = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private LiveData<Complaint> complaintLiveData;

    public ComplaintModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }


    public LiveData<Complaint> getComplaint() {
        return complaint;
    }

    public void run(LifecycleOwner lifecycleOwner, int issueId) {
        clear(lifecycleOwner);
        complaintLiveData = appDatabase.complaintsDao().getComplaintsByIDAsLive(issueId);
        complaintLiveData.observe(lifecycleOwner, complaintObserver);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void clear(LifecycleOwner lifecycleOwner) {
        if (complaintLiveData != null) {
            complaintLiveData.removeObservers(lifecycleOwner);
        }
    }

    private Observer<Complaint> complaintObserver = info -> {
        if (info != null) {
            complaint.setValue(info);
        }
    };

}


