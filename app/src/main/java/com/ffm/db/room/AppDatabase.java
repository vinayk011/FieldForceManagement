package com.ffm.db.room;


import android.content.Context;


import com.ffm.db.room.dao.ReportsDao;
import com.ffm.db.room.entity.ReportsInfo;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ReportsInfo.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getDatabase(Context context) {
        try {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "com_ffm")
                        .build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return instance;
    }

    public abstract ReportsDao reportsDao();
    
}
