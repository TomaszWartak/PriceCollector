package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompetitorSlotFullDataTest {

    CompetitorSlotFullData competitorSlotFullData;

    @Before
    public void setUp() throws Exception {
        AnalysisCompetitorSlot emptySlot = prepareEmptySlot();
        competitorSlotFullData = new CompetitorSlotFullData(emptySlot);
    }

    private AnalysisCompetitorSlot prepareEmptySlot() {
        AnalysisCompetitorSlot slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(1);
        slot.setOtherStoreId( AnalysisCompetitorSlot.NONE );
        slot.setCompanyId( 1 );
        slot.setCompanyName( "Castorama" );
        return slot;
    }

    public List<Store> prepareCopmpetitorStoreList() {
        List<Store> castoramaStores = new ArrayList<>();
        Store otherStore = new Store();
        otherStore.setName("Castorama Rybnik");
        otherStore.setCity("Rybnik");
        otherStore.setStreet("Obwiednia Północna 21");
        otherStore.setZipCode("44-200");
        castoramaStores.add(otherStore);
        otherStore = new Store();
        otherStore.setName("Castorama Żory");
        otherStore.setCity("Żory");
        otherStore.setStreet("Al. Zjednoczonej Europy 26");
        otherStore.setZipCode("44-240");
        castoramaStores.add(otherStore);
        return castoramaStores;
    }

    @Test
    public void _isStoresMapCorrect() {

        //assertEquals( false, userSupport.areLoginDataCorrect( null, null) );
    }

    @Test
    public void getSlot() {
    }

    @Test
    public void addStore() {
    }

    @Test
    public void getStore() {
    }

    @Test
    public void getCompetitorStores1() {
    }

    @Test
    public void setCompetitorStores1() {
    }

    @Test
    public void getCompetitorStoresMap() {
    }

    @Test
    public void setCompetitorStoresMap() {
    }

    @Test
    public void setChosenStore() {
    }

    @Test
    public void getChosenStore() {
    }

    @Test
    public void setNoChosenStore() {
    }

    @After
    public void tearDown() throws Exception {
    }

}