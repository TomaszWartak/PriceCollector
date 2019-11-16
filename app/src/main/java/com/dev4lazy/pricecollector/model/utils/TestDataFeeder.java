package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.ArrayList;
import java.util.List;

public class TestDataFeeder implements DataFeeder {

    private static TestDataFeeder instance = null;

    public static TestDataFeeder getInstance() {
        if (instance == null) {
            synchronized (TestDataFeeder.class) {
                if (instance == null) {
                    instance = new TestDataFeeder();
                }
            }
        }
        return instance;
    }
    
    @Override
    public List<Country> getCountriesInitialList() {
        List<Country> countries = new ArrayList<>();
        /*
        Country country = new Country();
        country.setName("Polska");
        country.setEnglishName("Poland");
        countries.add(country);
         */
        return countries;
    }

    @Override
    public List<Company> getCompaniesInitialList() {
        return null;
    }

    @Override
    public List<OwnStore> getOwnStoresInitialList() {
        List<OwnStore> ownStores = new ArrayList<>();
        OwnStore ownStore = new OwnStore( );
        ownStore.setName( "Castorama Rybnik" );
        ownStore.setCity( "Rybnik" );
        ownStore.setStreet( "Obwiednia Północna 21" );
        ownStore.setZipCode( "44-200" );
        ownStore.setOwnNumber( "8033" );
        ownStore.setSapOwnNumber( "1563" );
        ownStore.setStructureType( StoreStructureType.BY_SECTORS );
        ownStores.add( ownStore);
        ownStore = new OwnStore();
        ownStore.setName( "Castorama Katowice" );
        ownStore.setCity( "Katowice" );
        ownStore.setStreet( "aleja Roździeńskiego 198" );
        ownStore.setZipCode( "40-315" );
        ownStore.setOwnNumber( "8007" );
        ownStore.setSapOwnNumber( "????" );
        ownStore.setStructureType( StoreStructureType.BY_DEPARTMENTS );
        ownStores.add( ownStore );
        ownStore = new OwnStore();
        ownStore.setName("Castorama Sosnowiec" );
        ownStore.setCity( "Sosnowiec" );
        ownStore.setStreet( "Jana Długosza 82" );
        ownStore.setZipCode( "41-219" );
        ownStore.setOwnNumber( "8015" );
        ownStore.setSapOwnNumber( "????" );
        ownStore.setStructureType( StoreStructureType.BY_DEPARTMENTS );
        ownStores.add( ownStore );
        return ownStores;
    }

    @Override
    public List<Store> getObiStoresInitialList() {
        List<Store> obiStores = new ArrayList<>();
        Store otherStore = new Store();
        otherStore.setName("OBI Rybnik");
        otherStore.setCity("Rybnik");
        otherStore.setStreet("Żorska 55");
        otherStore.setZipCode("44-203");
        obiStores.add(otherStore);
        // obi Dąbrowa Górnicza
        // obi Czeladź
        // obi Katowice
        return obiStores;
    }

    @Override
    public List<Store> getLmStoresInitialList() {
        List<Store> lmStores = new ArrayList<>();
        Store otherStore = new Store();
        // LM Sosnowiec
        // LM katowice 1
        return lmStores;
    }

    @Override
    public List<Store> getBricomanStoresInitialList() {
        List<Store> bricomanStores = new ArrayList<>();
        Store otherStore = new Store();
        // Bricoman Jaworzno
        return bricomanStores;
    }

    @Override
    public List<Store> getLocalCompetitorStoresInitialList() {
        List<Store> localCompetitorStores = new ArrayList<>();
        Store otherStore = new Store();
        otherStore.setName("PSB Mrówka Jastrzębie=Zdrój");
        otherStore.setCity("Jastrzębie-Zdrój");
        otherStore.setStreet("Rybnicka 11");
        otherStore.setZipCode("44-335");
        localCompetitorStores.add(otherStore);
        otherStore = new Store();
        otherStore.setName("Majster Radlin");
        otherStore.setCity("Radlin");
        otherStore.setStreet("Rybnicka 125");
        otherStore.setZipCode("44-310");
        localCompetitorStores.add(otherStore);
        return localCompetitorStores;
    }
}
