package com.ffm.db.room.viewmodels;

import android.app.Application;


import com.ffm.db.room.AppDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private MutableLiveData<Integer> value = new MutableLiveData<>();

    BaseViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public MutableLiveData<Integer> getValue() {
        return value;
    }

    public abstract LiveData<Integer> getData();
    public abstract void run(LifecycleOwner lifecycleOwner);
    public abstract void clear(LifecycleOwner lifecycleOwner);
}
