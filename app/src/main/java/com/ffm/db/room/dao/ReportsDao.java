package com.ffm.db.room.dao;


import com.ffm.db.room.entity.ReportsInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReportsDao {

    @Query("SELECT * FROM ReportsInfo")
    List<ReportsInfo> getAll();

    @Query("SELECT * FROM ReportsInfo WHERE userId =:userId ")
    ReportsInfo getReportByID(String userId);

    @Query("SELECT * FROM ReportsInfo WHERE userId =:userId ")
    LiveData<ReportsInfo> getReportsByIDAsLive(String userId);

    @Query("SELECT * FROM ReportsInfo")
    LiveData<List<ReportsInfo>> getAllAsLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ReportsInfo... ReportsInfos);

    @Update
    void update(ReportsInfo ReportsInfo);

    @Query("DELETE FROM ReportsInfo")
    public void emptyReports();
}
