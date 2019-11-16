package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

public class AppDataFeeder implements DataFeeder {

    private static AppDataFeeder instance = null;

    private ProductionDataFeeder productionDataFeeder;
    private TestDataFeeder testDataFeeder;

    AppDataFeeder() {
        productionDataFeeder = ProductionDataFeeder.getInstance();
        testDataFeeder = TestDataFeeder.getInstance();
    }

    public static AppDataFeeder getInstance() {
        if (instance == null) {
            synchronized (AppDataFeeder.class) {
                if (instance == null) {
                    instance = new AppDataFeeder();
                }
            }
        }
        return instance;
    }

    public void setDataFeeders(ProductionDataFeeder productionDataFeeder, TestDataFeeder testDataFeeder) {
        this.productionDataFeeder = productionDataFeeder;
        this.testDataFeeder = testDataFeeder;
    }

    @Override
    public List<Country> getCountriesInitialList() {
        return productionDataFeeder.getCountriesInitialList();
    }

    @Override
    public List<Company> getCompaniesInitialList() {
        return productionDataFeeder.getCompaniesInitialList();
    }

    @Override
    public List<OwnStore> getOwnStoresInitialList() {
        if (BuildConfig.DEBUG) {
            return testDataFeeder.getOwnStoresInitialList();
        } else {
            return productionDataFeeder.getOwnStoresInitialList();
        }
    }

    @Override
    public List<Store> getObiStoresInitialList() {
        if (BuildConfig.DEBUG) {
            return testDataFeeder.getObiStoresInitialList();
        } else {
            return productionDataFeeder.getObiStoresInitialList();
        }
    }

    @Override
    public List<Store> getLmStoresInitialList() {
        if (BuildConfig.DEBUG) {
            return testDataFeeder.getLmStoresInitialList();
        } else {
            return productionDataFeeder.getLmStoresInitialList();
        }
    }

    @Override
    public List<Store> getBricomanStoresInitialList() {
        if (BuildConfig.DEBUG) {
            return testDataFeeder.getBricomanStoresInitialList();
        } else {
            return productionDataFeeder.getBricomanStoresInitialList();
        }
    }

    @Override
    public List<Store> getLocalCompetitorStoresInitialList() {
        if (BuildConfig.DEBUG) {
            return testDataFeeder.getLocalCompetitorStoresInitialList();
        } else {
            return productionDataFeeder.getLocalCompetitorStoresInitialList();
        }
    }
}
