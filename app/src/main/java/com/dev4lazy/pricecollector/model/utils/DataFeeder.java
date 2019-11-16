package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

public interface DataFeeder {

    List<Country> getCountriesInitialList();

    List<Company> getCompaniesInitialList();

    List<OwnStore> getOwnStoresInitialList();

    List<Store> getObiStoresInitialList();

    List<Store> getLmStoresInitialList();

    List<Store> getBricomanStoresInitialList();

    List<Store> getLocalCompetitorStoresInitialList();

}
