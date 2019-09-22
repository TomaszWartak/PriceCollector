package com.dev4lazy.pricecollector.csv2pojo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dev4lazy.pricecollector.utils.AppHandle;

@Database(
        version = 1,
        entities = {
                AnalysisRow.class
        },
        exportSchema = false
)
public abstract class AnalyzesDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "price_collector_remote_database";
    private static volatile AnalyzesDatabase instance;

    public abstract AnalysisRowDao analysisRowDao();

    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>();

    public static AnalyzesDatabase getInstance() {
        if (instance == null) {
            Context context = AppHandle.getHandle().getApplicationContext();
            synchronized (AnalyzesDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            AnalyzesDatabase.class, DATABASE_NAME )
                            //.addCallback(roomDatabaseCallback)
                            //.addMigrations(MIGRATION_1_2)
                            .build();
                    instance.updateDatabaseCreated(context);
                }
            }
        }
        return instance;
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        databaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return databaseCreated;
    }
}


