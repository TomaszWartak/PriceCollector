package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

public interface DataFeeder {

    String[] COMPANIES_NAMES = {"BRIKO", "Leroy Merlin", "OBI", "Castorama", "Konkurent lokalny"};
    int BRIKO_INDEX = 0;
    int LEROY_MERLIN_INDEX = 1;
    int OBI_INDEX = 2;
    int CASTORAMA_INDEX = 3;
    int LOCAL_COMPETITOR_INDEX = 4;

    List<Country> getCountriesInitialList();

    List<Company> getCompaniesInitialList();

    List<OwnStore> getOwnStoresInitialList();

    List<Store> getObiStoresInitialList();

    List<Store> getLmStoresInitialList();

    List<Store> getCastoramaStoresInitialList();

    List<Store> getLocalCompetitorStoresInitialList();

}
