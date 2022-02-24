package com.dev4lazy.pricecollector.remote_model.db;

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
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteFamily;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteMarket;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteModule;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUOProject;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.AppHandle;

@Database(
        version = 9,
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

    private static volatile RemoteDatabase instance;

    public static RemoteDatabase getInstance() {
        if (instance == null) {
            Context context = AppHandle.getHandle().getApplicationContext();
            synchronized (RemoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context,
                            RemoteDatabase.class, DATABASE_NAME )
                            // !! Jeśli zamiast migracji chcesz wyczyścić bazę, to od komentuj .fallback...
                            // i za komentuj .addMigrations
                            /*/.fallbackToDestructiveMigration() // tego nie rób, bo zpoamnisz i Ci wyczyści bazę...
                            /*/
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            .addMigrations(MIGRATION_8_9)
                            /**/
                            .build();
                    instance.updateDatabaseCreated(context);
                }
            }
        }
        return instance;
    }
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

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE UNIQUE INDEX index_ean_codes__value ON ean_codes (value)" );
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP INDEX index_ean_codes_value" );
            database.execSQL("DROP INDEX index_ean_codes_article_id" );
            database.execSQL("PRAGMA foreign_keys = OFF");
            database.execSQL("BEGIN TRANSACTION" );
            database.execSQL("CREATE TABLE IF NOT EXISTS ean_codes_new (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER NOT NULL, " +
                    "value TEXT, " +
                    "article_id INTEGER NOT NULL )"
            );
            database.execSQL("CREATE INDEX index_ean_codes_article_id ON ean_codes_new (article_id)" );
            database.execSQL("CREATE UNIQUE INDEX index_ean_codes_value ON ean_codes_new (value)" );
            database.execSQL("INSERT INTO ean_codes_new SELECT * FROM ean_codes" );
            database.execSQL("DROP TABLE ean_codes" );
            database.execSQL("ALTER TABLE ean_codes_new RENAME TO ean_codes" );
            database.execSQL("COMMIT" );
            database.execSQL("PRAGMA foreign_keys = ON");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP INDEX index_analysis_rows_articleCode");
            database.execSQL("BEGIN TRANSACTION");
            database.execSQL("CREATE TABLE IF NOT EXISTS analysis_rows_new (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "analysisId INTEGER NOT NULL, " +
                    "articleCode INTEGER NOT NULL, " +
                    "storeId INTEGER NOT NULL, " +
                    "articleName TEXT, " +
                    "articleStorePrice REAL, " +
                    "articleRefPrice REAL, " +
                    "articleNewPrice REAL, " +
                    "articleNewMarginPercent REAL, " +
                    "articleLmPrice REAL, " +
                    "articleObiPrice REAL, " +
                    "articleCastoramaPrice REAL, " +
                    "articleLocalCompetitor1Price REAL, " +
                    "articleLocalCompetitor2Price REAL, " +
                    "department TEXT, " +
                    "sector TEXT )"
            );
            database.execSQL("CREATE INDEX index_analysis_rows_articleCode ON analysis_rows_new (articleCode)");
            database.execSQL("INSERT INTO analysis_rows_new SELECT " +
                    "id, " +
                    "analysisId, " +
                    "articleCode, " +
                    "storeId, " +
                    "articleName, " +
                    "articleStorePrice, " +
                    "articleRefPrice, " +
                    "articleNewPrice, " +
                    "articleNewMarginPercent, " +
                    "articleLmPrice, " +
                    "articleObiPrice, " +
                    "articleBricomanPrice articleCastoramaPrice, " +
                    "articleLocalCompetitor1Price, " +
                    "articleLocalCompetitor2Price, " +
                    "department, " +
                    "sector "+
                "FROM analysis_rows"
            );
            database.execSQL("DROP TABLE analysis_rows");
            database.execSQL("ALTER TABLE analysis_rows_new RENAME TO analysis_rows");
            database.execSQL("COMMIT");
        }
    };

    /*
    private Integer analysisId;
    private Integer articleCode; // kod briko 6 cyfr
    private Integer storeId;
    private String articleName;
    private Double articleStorePrice;
    private Double articleRefPrice;
    private Double articleNewPrice;
    private Double articleNewMarginPercent;
    private Double articleLmPrice;
    private Double articleObiPrice;
    private Double articleCastoramaPrice;
    private Double articleLocalCompetitor1Price;
    private Double articleLocalCompetitor2Price;
    private String department;
    private String sector;
     */
    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>( false );

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


