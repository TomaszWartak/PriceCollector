package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.ArrayList;

public class CompetitorSlotFullData {

    private AnalysisCompetitorSlot slot;
    private ArrayList<Store> competitorStores;
    private int state; //todo?

    /*
    CompetitorSlotFullData() {
        // na potrzeby dodawania dummy
    }
    */

    CompetitorSlotFullData(AnalysisCompetitorSlot slot) {
        this.slot = slot;
    }

    public AnalysisCompetitorSlot getSlot() {
        return slot;
    }

    public ArrayList<Store> getCompetitorStores() {
        return competitorStores;
    }

    public void setCompetitorStores(ArrayList<Store> stores) {
        this.competitorStores = stores;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}