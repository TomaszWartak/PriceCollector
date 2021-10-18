package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.ArrayList;
import java.util.List;

public class ProductionDataFeeder implements DataFeeder {

    private static ProductionDataFeeder instance = null;
    /* TEST XXX
    public final String[] COMPANIES_NAMES = {"BRIKO", "Leroy Merlin", "OBI", "Castorama", "Konkurent lokalny"};
    public final int BRIKO_INDEX = 0;
    public final int LEROY_MERLIN_INDEX = 1;
    public final int OBI_INDEX = 2;
    public final int CASTORAMA_INDEX = 3;
    public final int LOCAL_COMPETITOR_INDEX = 4;

     */

    public static ProductionDataFeeder getInstance() {
        if (instance == null) {
            synchronized (ProductionDataFeeder.class) {
                if (instance == null) {
                    instance = new ProductionDataFeeder();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Country> getCountriesInitialList() {
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setName("Polska");
        country.setEnglishName("Poland");
        countries.add(country);
        return countries;
    }

    @Override
    public List<Company> getCompaniesInitialList() {
        List<Company> companies = new ArrayList<>();
        // Nie zmieniaj kolejności, bo dalej ma to znaczenie :-)
        Company company = new Company();
        company.setName(COMPANIES_NAMES[BRIKO_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[LEROY_MERLIN_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[OBI_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[CASTORAMA_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[LOCAL_COMPETITOR_INDEX]);
        companies.add(company);
           /*
        company.setName("BRICO MARCHE");
        companies.add(company);
        company.setName("Mrówka");
        companies.add(company);
        company.setName("Majster");
        companies.add(company);
        */
       return companies;
    }

    @Override
    public List<OwnStore> getOwnStoresInitialList() {
        return null;
    }

    @Override
    public List<Store> getObiStoresInitialList() {
        return null;
    }

    @Override
    public List<Store> getLmStoresInitialList() {
        return null;
    }

    @Override
    public List<Store> getCastoramaStoresInitialList() {
        return null;
    }

    @Override
    public List<Store> getLocalCompetitorStoresInitialList() {
        return null;
    }
}
