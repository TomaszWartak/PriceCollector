package com.dev4lazy.pricecollector.model.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.model.utils.StoreStructureTypeConverter;

@Database(
    version = 4,
    entities = {
        AnalysisCompetitorSlot.class,
        Analysis.class,
        Article.class,
        Company.class,
        Country.class,
        Department.class,
        EanCode.class,
        Family.class,
        Market.class,
        Module.class,
        OwnArticleInfo.class,
        OwnStore.class,
        Sector.class,
        Store.class,
        UOProject.class
    },
    exportSchema = false
)
@TypeConverters({
        DateConverter.class,
        StoreStructureTypeConverter.class
})
public abstract class LocalDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "price_collector_local_database";
    private static volatile LocalDatabase instance;

    // Dane pomocnicze
    public abstract AnalysisCompetitorSlotDao analysisCompetitorSlotDao();

    // Dane odpowiadające danym bazy zdalnej
    public abstract AnalysisDao analysisDao();
    public abstract ArticleDao articleDao();
    public abstract CompanyDao companyDao();
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE stores ADD COLUMN name TEXT");
            database.execSQL("ALTER TABLE own_stores ADD COLUMN name TEXT");
        }
    };
    public abstract DepartmentDao departmentDao();
    public abstract EanCodeDao eanCodeDao();
    public abstract FamilyDao familyDao();
    public abstract MarketDao marketDao();
    public abstract ModuleDao moduleDao();
    public abstract OwnArticleInfoDao ownArticleInfoDao();
    public abstract OwnStoreDao ownStoreDao();
    public abstract SectorDao sectorDao();
    public abstract StoreDao storeDao();
    public abstract UOProjectDao uoProjectDao();

    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>();
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS competitor_slots (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "slot_nr INTEGER, " +
                    "company_id INTEGER, " +
                    "other_store_id INTEGER  )" );
                    /*
                    "other_store_id INTEGER, " +
                    "PRIMARY KEY(id))");
                    */
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE competitor_slots ADD COLUMN company_name TEXT");
            database.execSQL("ALTER TABLE competitor_slots ADD COLUMN other_store_name TEXT");
        }
    };

    public static LocalDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (LocalDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, DATABASE_NAME )
                            //.addCallback(roomDatabaseCallback)
                            // !! Jeśli zamiast migracji chcesz wyczyścić bazę, to od komentuj .falback...
                            // i za komentuj .addMigrations
                            //.fallbackToDestructiveMigration() // tego nie rób, bo zpoamnisz i Ci wyczyści bazę...
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .build();
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public abstract CountryDao countryDao();

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        databaseCreated.postValue(true);
    }

    public LiveData<Boolean> isDatabaseCreated() {
        return databaseCreated;
    }

}
