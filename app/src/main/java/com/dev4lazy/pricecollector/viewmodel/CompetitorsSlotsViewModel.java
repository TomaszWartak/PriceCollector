package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.logic.AnalysisCompetitorSlotList;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class CompetitorsSlotsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<CompetitorSlotFullData>> competitorsSlotsLiveData;

    public CompetitorsSlotsViewModel(Application application) {
        super(application);
        competitorsSlotsLiveData = new MutableLiveData<>();
        askForSlots();
    }

    private void askForSlots() {
        MutableLiveData<List<AnalysisCompetitorSlot>> result = new MutableLiveData<>();
        Observer<List<AnalysisCompetitorSlot>> resultObserver = new Observer<List<AnalysisCompetitorSlot>>() {
            @Override
            public void onChanged(List<AnalysisCompetitorSlot> slots) {
                result.removeObserver(this); // this = observer...
                if (slots != null) {
                    AnalysisCompetitorSlotList analysisCompetitorSlotList = new AnalysisCompetitorSlotList(slots);
                    askForCompetitorStores( analysisCompetitorSlotList);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllAnalysisCompetitorSlotsSortedBySlotNr(result);
    }

    private void askForCompetitorStores( AnalysisCompetitorSlotList analysisCompetitorSlotList) {
        MutableLiveData<List<Store>> result = new MutableLiveData<>();
        Observer<List<Store>> resultObserver = new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> storesList) {
                result.removeObserver(this); // this = observer...
                for (CompetitorSlotFullData slotInfo : analysisCompetitorSlotList.getFullDataSlotList() ) {
                    ArrayList<Store> stores = (ArrayList<Store>)storesList;
                    HashMap<Integer,Store> storesMap = (HashMap<Integer,Store>)stores
                            .stream()
                            .filter(store -> store.getCompanyId()==slotInfo.getSlot().getCompanyId())
                            .collect( Collectors.toMap( Store::getId, store->store));
                    slotInfo.setCompetitorStoresMap( storesMap );
                    /* TODO XXX
                    slotInfo.setCompetitorStores1(
                            (ArrayList<Store>)storesList
                                    .stream()
                                    .filter(store -> store.getCompanyId()==slotInfo.getSlot().getCompanyId())
                                    .collect( Collectors.toList() )
                    );
                    */
                }
                competitorsSlotsLiveData.setValue( analysisCompetitorSlotList.getFullDataSlotList() );
                // competitorsSlotsListView.submitCompetitorsSlotsList(analysisCompetitorSlotList.getFullDataSlotList());
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllStores(result);
    }

    public LiveData<ArrayList<CompetitorSlotFullData>> getCompetitorsSlotsLiveData() {
        return competitorsSlotsLiveData;
    }
}
