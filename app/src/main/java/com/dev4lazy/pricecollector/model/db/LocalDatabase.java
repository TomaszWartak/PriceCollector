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
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;
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
import com.dev4lazy.pricecollector.AppHandle;

@Database(
    version = 11,
    entities = {
        AnalysisCompetitorSlot.class,
        Analysis.class,
        AnalysisArticle.class,
        Article.class,
        Company.class,
        CompetitorPrice.class,
        Country.class,
        Department.class,
        DepartmentInSector.class,
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
    private final MutableLiveData<Boolean> databaseCreated = new MutableLiveData<>();

    public abstract AnalysisArticleDao analysisArticleDao();
    public abstract AnalysisCompetitorSlotDao analysisCompetitorSlotDao();
    public abstract AnalysisDao analysisDao();
    public abstract ArticleDao articleDao();
    public abstract CompanyDao companyDao();
    public abstract CompetitorPriceDao competitorPriceDao();
    public abstract CountryDao countryDao();
    public abstract DepartmentDao departmentDao();
    public abstract DepartmentInSectorDao departmentInSectorDao();
    public abstract EanCodeDao eanCodeDao();
    public abstract FamilyDao familyDao();
    public abstract MarketDao marketDao();
    public abstract ModuleDao moduleDao();
    public abstract OwnArticleInfoDao ownArticleInfoDao();
    public abstract OwnStoreDao ownStoreDao();
    public abstract SectorDao sectorDao();
    public abstract StoreDao storeDao();
    public abstract UOProjectDao uoProjectDao();

    public static LocalDatabase getInstance() {
        if (instance == null) {
            Context context = AppHandle.getHandle().getApplicationContext();
            synchronized (LocalDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, DATABASE_NAME )
                            //.addCallback(roomDatabaseCallback)
                            // !! Jeśli zamiast migracji chcesz wyczyścić bazę, to odkomentuj .fallback...
                            // i za komentuj .addMigrations
                             .fallbackToDestructiveMigration() // tego nie rób, bo zpoamnisz i Ci wyczyści bazę...
                            /*/
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            .addMigrations(MIGRATION_8_9)
                            .addMigrations(MIGRATION_9_10)
                            .addMigrations(MIGRATION_10_11)
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
            database.execSQL("ALTER TABLE stores ADD COLUMN name TEXT");
            database.execSQL("ALTER TABLE own_stores ADD COLUMN name TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS competitor_slots (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "slot_nr INTEGER NOT NULL, " +
                    "company_id INTEGER NOT NULL, " +
                    "other_store_id INTEGER NOT NULL )" );
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

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS analysis_articles (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "analysis_id INTEGER NOT NULL, " +
                    "article_id INTEGER NOT NULL, " +
                    "competitor_store_id INTEGER NOT NULL, " +
                    "competitor_store_price REAL, " +
                    "article_store_price REAL, " +
                    "article_ref_price REAL, " +
                    "article_new_price REAL, " +
                    "reference_article_id INTEGER NOT NULL, " +
                    "comments TEXT  )" );
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS departments_in_sector (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "sector_id INTEGER NOT NULL, " +
                    "department_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (sector_id) REFERENCES sectors(id), " +
                    "FOREIGN KEY (department_id) REFERENCES departments(id) )" );
            database.execSQL("CREATE INDEX index_departments_in_sector_sector_id " +
                    "ON departments_in_sector (sector_id)" );
            database.execSQL("CREATE INDEX index_departments_in_sector_department_id " +
                    "ON departments_in_sector (department_id)" );
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS competitors_prices (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "analysis_id INTEGER NOT NULL, " +
                    "analysis_article_id INTEGER NOT NULL, " +
                    "own_article_info_id INTEGER NOT NULL, " +
                    "competitor_store_id INTEGER NOT NULL, " +
                    "competitor_store_price REAL NOT NULL, " +
                    "reference_article_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (analysis_id) REFERENCES analyzes(id), " +
                    "FOREIGN KEY (competitor_store_id) REFERENCES stores(id) )" );
            database.execSQL("CREATE INDEX index_competitors_prices_analysis_id " +
                    "ON competitors_prices (analysis_id)" );
            database.execSQL("CREATE INDEX index_competitors_prices_competitor_store_id " +
                    "ON competitors_prices (competitor_store_id)" );
            database.execSQL("ALTER TABLE analysis_articles ADD COLUMN own_article_info_id INTEGER NOT NULL DEFAULT -1");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE analyzes ADD COLUMN remote_id INTEGER NOT NULL DEFAULT -1");
            database.execSQL("CREATE UNIQUE INDEX index_analyzes__remote_id ON analyzes (remote_id)" );
            database.execSQL("CREATE UNIQUE INDEX index_articles__remote_id ON articles (remote_id)" );
            database.execSQL("CREATE UNIQUE INDEX index_ean_codes__remote_id ON ean_codes (remote_id)" );
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP INDEX index_own_articles_infos_article_id");
            database.execSQL("CREATE UNIQUE INDEX index_own_articles__infos_article_id ON own_articles_infos (article_id)" );
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE analyzes ADD COLUMN dataDownloaded INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE analysis_articles ADD COLUMN competitor_store_price_id INTEGER NOT NULL DEFAULT -1");
        }
    };

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
