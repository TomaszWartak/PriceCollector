package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.logic.AnalysisCompetitorsSlotsFullDataList;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class CompetitorsSlotsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<CompetitorSlotFullData>> competitorsSlotsFullDataLiveData;

    public CompetitorsSlotsViewModel(Application application) {
        super(application);
        competitorsSlotsFullDataLiveData = new MutableLiveData<>();
        askForSlots();
    }

    private void askForSlots() {
        MutableLiveData<List<AnalysisCompetitorSlot>> result = new MutableLiveData<>();
        Observer<List<AnalysisCompetitorSlot>> resultObserver = new Observer<List<AnalysisCompetitorSlot>>() {
            @Override
            public void onChanged(List<AnalysisCompetitorSlot> slots) {
                result.removeObserver(this); // this = observer...
                if (slots != null) {
                    AnalysisCompetitorsSlotsFullDataList analysisCompetitorsSlotsFullDataList = new AnalysisCompetitorsSlotsFullDataList(slots);
                    askForCompetitorsStores(analysisCompetitorsSlotsFullDataList);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllAnalysisCompetitorSlotsSortedBySlotNr(result);
    }

    private void askForCompetitorsStores(AnalysisCompetitorsSlotsFullDataList analysisCompetitorsSlotsFullDataList) {
        MutableLiveData<List<Store>> result = new MutableLiveData<>();
        Observer<List<Store>> resultObserver = new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> storesList) {
                result.removeObserver(this); // this = observer...
                for (CompetitorSlotFullData competitorSlotFullData : analysisCompetitorsSlotsFullDataList.getFullDataSlotList() ) {
                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    ArrayList<Store> stores = (ArrayList<Store>)storesList;
                    HashMap<Integer,Store> storesMap = (HashMap<Integer,Store>)
                            stores
                            .stream()
                            .filter( store -> store.getCompanyId()==slot.getCompanyId())
                            .collect( Collectors.toMap( Store::getId, store->store));
                    competitorSlotFullData.setCompetitorStoresMap( storesMap );
                    Store chosenStore = competitorSlotFullData.getStore( slot.getOtherStoreId() );
                    competitorSlotFullData.setChosenStore( chosenStore );
                    if (chosenStore!=null) {
                        competitorSlotFullData.removeStore( chosenStore );
                    }
                }
                /* Dla lokalnych konkurentów (sloty 4 i 5) należy wukonac nastepującą operację.
                Jeśli lokalny konkurent ma wybrany sklep w slocie, to u drugiego konkurenta
                nalezy ten sklep usunąć z listy. I odwrotnie. */
                CompetitorSlotFullData competitorSlotFullData_No4 = analysisCompetitorsSlotsFullDataList.getFullDataSlotList().get(3);
                CompetitorSlotFullData competitorSlotFullData_No5 = analysisCompetitorsSlotsFullDataList.getFullDataSlotList().get(4);
                Store storeChosenForSlot_No4 = competitorSlotFullData_No4.getChosenStore();
                Store storeChosenForSlot_No5 = competitorSlotFullData_No5.getChosenStore();
                if (storeChosenForSlot_No4!=null) {
                    competitorSlotFullData_No5.removeStore( storeChosenForSlot_No4 );
                }
                if (storeChosenForSlot_No5!=null) {
                    competitorSlotFullData_No4.removeStore( storeChosenForSlot_No5 );
                }
                competitorsSlotsFullDataLiveData.setValue( analysisCompetitorsSlotsFullDataList.getFullDataSlotList() );
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllStores(result);
    }

    public LiveData<ArrayList<CompetitorSlotFullData>> getCompetitorsSlotsFullDataLiveData() {
        return competitorsSlotsFullDataLiveData;
    }

    public ArrayList<CompetitorSlotFullData> getCompetitorsSlotsFullData() {
        return getCompetitorsSlotsFullDataLiveData().getValue();
    }
}
