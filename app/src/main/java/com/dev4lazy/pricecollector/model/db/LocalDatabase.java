package com.dev4lazy.pricecollector.model.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.entities.Analysis;
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
    version = 1,
    entities = {
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

    public abstract AnalysisDao analysisDao();
    public abstract ArticleDao articleDao();
    public abstract CompanyDao companyDao();
    public abstract CountryDao2 countryDao(); // todo !!!! 22222
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

    public static LocalDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (LocalDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, DATABASE_NAME )
                            //.addCallback(roomDatabaseCallback)
                            //.addMigrations(MIGRATION_1_2)
                            .build();
                    instance.updateDatabaseCreated(context.getApplicationContext());
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

    public LiveData<Boolean> isDatabaseCreated() {
        return databaseCreated;
    }

}
