package com.dev4lazy.pricecollector.model;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

    private LocalDataRepository() {
        // todo incializajca baz
    }

    public static LocalDataRepository getInstance() {
        if (instance == null) {
            synchronized (LocalDataRepository.class) {
                if (instance == null) {
                    instance = new LocalDataRepository();
                }
            }
        }
        return instance;
    }

    public void initializeLocalDatabase() {
        initalizeCountries();
        initializeCompanies();
        initializeStores();
        initializeDepartments();
        initializeFamilies();
        initializeModules();
        initializeMarkets();
        initializeSectores();
        initializeUOProjects();
    }

    private void initalizeCountries() {
        Country country = new Country();
        country.setName("Polska");
        country.setEnglishName("Poland");
    }

    private void initializeCompanies(){
        Company comapny = new Company();
        //comapny.setCountryId();

    }

    private void initializeStores(){

    }

    private void initializeDepartments(){

    }

    private void initializeFamilies(){

    }

    private void initializeModules(){

    }

    private void initializeMarkets(){

    }

    private void initializeSectores(){

    }

    private void initializeUOProjects(){

    }

}
