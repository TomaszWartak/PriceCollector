package com.dev4lazy.pricecollector.view;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;

import java.util.ArrayList;
import java.util.List;

public class AnalysisCompetitorSlotList {

    private ArrayList<CompetitorSlotFullData> fullDataSlotList;

    AnalysisCompetitorSlotList(List<AnalysisCompetitorSlot> slots ) {
        fullDataSlotList = new ArrayList<>();
        for (AnalysisCompetitorSlot slot : slots) {
            fullDataSlotList.add(new CompetitorSlotFullData(slot));
        }
    }

    public ArrayList<CompetitorSlotFullData> getFullDataSlotList() {
        return fullDataSlotList;
    }

}
