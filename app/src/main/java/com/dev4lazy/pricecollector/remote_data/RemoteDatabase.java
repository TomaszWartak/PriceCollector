package com.dev4lazy.pricecollector.remote_data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.model.utils.StoreStructureTypeConverter;
import com.dev4lazy.pricecollector.utils.AppHandle;

@Database(
        version = 6,
        entities = {
                RemoteAnalysis.class,
                RemoteAnalysisRow.class,
                RemoteDepartment.class,
                RemoteEanCode.class,
                RemoteFamily.class,
                RemoteMarket.class,
                RemoteModule.class,
                RemoteSector.class,
                RemoteUser.class,
                RemoteUOProject.class
        },
        exportSchema = false
)
@TypeConverters({
        DateConverter.class,
        StoreStructureTypeConverter.class
})
public abstract class RemoteDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "price_collector_remote_database";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "login TEXT, " +
                    "name TEXT, " +
                    "email TEXT, "+
                    "ownStoreNumber TEXT, "+
                    "departmentSymbol TEXT, "+
                    "sectorName TEXT, "+
                    "marketName TEXT )" );
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER NOT NULL, " +
                    "symbol TEXT, " +
                    "name TEXT )" );
             database.execSQL("CREATE TABLE IF NOT EXISTS sectors (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER NOT NULL, " +
                    "name TEXT )" );
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            /* todo?
            database.execSQL("CREATE TABLE IF NOT EXISTS articles??? (" +
            */
            database.execSQL("CREATE UNIQUE INDEX index_analysis_rows_articleCode " +
                    "ON analysis_rows (articleCode)" );
            database.execSQL("CREATE TABLE IF NOT EXISTS ean_codes (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER NOT NULL, " +
                    "value TEXT, " +
                    "article_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (article_id) REFERENCES analysis_rows(articleCode) )" );
             database.execSQL("CREATE INDEX index_ean_codes_article_id " +
                    "ON ean_codes (article_id)" );

        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS analyzes (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "creation_date INTEGER, " +
                    "due_date INTEGER, " +
                    "finish_date INTEGER, " +
                    "confirmation_date INTEGER, " +
                    "finished INTEGER )" );
            database.execSQL("ALTER TABLE analysis_rows ADD COLUMN analysisId INTEGER");
            database.execSQL("ALTER TABLE analysis_rows ADD COLUMN department TEXT");
            database.execSQL("ALTER TABLE analysis_rows ADD COLUMN sector TEXT");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS modules (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT )" );
            database.execSQL("CREATE TABLE IF NOT EXISTS families (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT )" );
            database.execSQL("CREATE TABLE IF NOT EXISTS markets (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT )" );
            database.execSQL("CREATE TABLE IF NOT EXISTS uo_projects (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT )" );
        }
    };

    private static volatile RemoteDatabase instance;

    public static RemoteDatabase getInstance() {
        if (instance == null) {
            Context context = AppHandle.getHandle().getApplicationContext();
            synchronized (RemoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            RemoteDatabase.class, DATABASE_NAME )
                            //.addCallback(roomDatabaseCallback)
                            // .fallbackToDestructiveMigration() // tego nie rób, bo zpoamnisz i Ci wyczyści bazę...
                            /**/
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            /**/
                            .build();
                    instance.updateDatabaseCreated(context);
                }
            }
        }
        return instance;
    }

    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>();

    public abstract RemoteAnalysisDao remoteAnalysisDao();

    public abstract RemoteAnalysisRowDao remoteAnalysisRowDao();

    public abstract RemoteEanCodeDao remoteEanCodeDao();

    public abstract RemoteFamilyDao remoteFamilyDao();

    public abstract RemoteMarketDao remoteMarketDao();

    public abstract RemoteModuleDao remoteModuleDao();

    public abstract RemoteUserDao remoteUserDao();

    public abstract RemoteDepartmentDao remoteDepartmentDao();

    public abstract RemoteSectorDao remoteSectorDao();

    public abstract RemoteUOProjectDao remoteUOProjectDao();

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


