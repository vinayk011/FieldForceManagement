package com.ffm.db.room.dao;

import com.ffm.db.room.entity.Complaint;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ComplaintsDao {

    @Query("SELECT * FROM Complaint")
    List<Complaint> getAll();

    @Query("SELECT * FROM Complaint WHERE employeeID =:employeeId ")
    List<Complaint> getComplaintsByUserID(String employeeId);

    @Query("SELECT * FROM Complaint WHERE employeeID =:employeeId ")
    LiveData<List<Complaint>> getComplaintsByIDAsLive(String employeeId);

    @Query("SELECT * FROM Complaint WHERE issueID =:issueId AND employeeID =:employeeId Limit 1")
    Complaint getComplaintsByIssueID(int issueId, String employeeId);

    @Query("SELECT * FROM Complaint")
    LiveData<List<Complaint>> getAllAsLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Complaint... complaints);

    @Update
    void update(Complaint complaint);

    @Query("DELETE FROM Complaint")
    public void emptyReports();
}
