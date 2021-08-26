package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;

import java.util.ArrayList;
import java.util.List;

public class AnalysisCompetitorSlotList {

    private ArrayList<CompetitorSlotFullData> fullDataSlotList;

    public AnalysisCompetitorSlotList(List<AnalysisCompetitorSlot> competitorSlots ) {
        fullDataSlotList = new ArrayList<>();
        for (AnalysisCompetitorSlot slot : competitorSlots) {
            fullDataSlotList.add(new CompetitorSlotFullData(slot));
        }
    }

    public ArrayList<CompetitorSlotFullData> getFullDataSlotList() {
        return fullDataSlotList;
    }

}
