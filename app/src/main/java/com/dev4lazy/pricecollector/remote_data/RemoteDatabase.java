package com.dev4lazy.pricecollector.remote_data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dev4lazy.pricecollector.model.RemoteDataRepository;
import com.dev4lazy.pricecollector.utils.AppHandle;

@Database(
        version = 4,
        entities = {
                RemoteAnalysisRow.class,
                RemoteDepartment.class,
                RemoteEanCode.class,
                RemoteSector.class,
                RemoteUser.class
        },
        exportSchema = false
)
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
                    /*
                    private int id;
                    private String login;
                    private String name;
                    private String email;
                    private String ownStoreNumber; // OwnStore.ownNumber
                    private String departmentSymbol; // Department.symbol
                    private String sectorName; // Sector.name
                    private String marketName;
                    */
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER, " +
                    "symbol TEXT, " +
                    "name TEXT )" );
                    /*
                    public int remote_id; // klucz głowny w bazie zdalnej
                    public String symbol;
                    public String name;
                    */
            database.execSQL("CREATE TABLE IF NOT EXISTS sectors (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "remote_id INTEGER, " +
                    "name TEXT )" );
                    /*
                    public int remote_id; // klucz głowny w bazie zdalnej
                    public String name;
                    */
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
                    "remote_id INTEGER, " +
                    "value TEXT, " +
                    "article_id INTEGER, " +
                    "FOREIGN KEY (article_id) REFERENCES analysis_rows(articleCode) )" );
                    /*
                        @PrimaryKey(autoGenerate = true)
                        public int id;
                        public int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
                        public String value;
                        @ColumnInfo(name = "article_id")
                        public int articleId;
                    */
            database.execSQL("CREATE INDEX article_id " +
                    "ON ean_codes (article_id)" );

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
                            /**/
                            .build();
                    instance.updateDatabaseCreated(context);
                }
            }
        }
        return instance;
    }

    public abstract RemoteAnalysisRowDao analysisRowDao();

    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>();

    public abstract RemoteEanCodeDao eanCodeDao();

    public abstract RemoteUserDao userDao();

    public abstract RemoteDepartmentDao departmentDao();

    public abstract RemoteSectorDao sectorDao();

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


