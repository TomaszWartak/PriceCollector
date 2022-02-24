package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.ArrayList;
import java.util.HashMap;

public class CompetitorSlotFullData {

    private AnalysisCompetitorSlot slot;
    private HashMap<Integer, Store> competitorStoresMap;
    private Store chosenStore;

    CompetitorSlotFullData(AnalysisCompetitorSlot slot) {
        this.slot = slot;
    }

    public AnalysisCompetitorSlot getSlot() {
        return slot;
    }

    public void addStore( Store store ) {
        competitorStoresMap.put( store.getId(), store);
    }

    public Store getStore( Integer storeId ) {
        return competitorStoresMap.get( storeId );
    }

    public void replaceStore( Store store ) {
        competitorStoresMap.replace( store.getId(), store);
    }

    public void removeStore( Store store ) {
        competitorStoresMap.remove( store.getId() );
    }

    public HashMap<Integer, Store> getCompetitorStoresMap() {
        return competitorStoresMap;
    }

    public void setCompetitorStoresMap(HashMap<Integer, Store> stores) {
        this.competitorStoresMap = stores;
    }

    public void setChosenStore(Store chosenStore) {
        this.chosenStore = chosenStore;
    }

    public Store getChosenStore() {
        return chosenStore;
    }

    public void setNoChosenStore() {
        setChosenStore( null );
    }

}