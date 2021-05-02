package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisCompetitorSlotList;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisCompetitorsListFragment extends Fragment {


    public AnalysisCompetitorsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.analysis_competitors_fragment, container, false);
        setupListView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupListView(View view) {
        ArrayList<CompetitorSlotFullData> emptyList = new ArrayList<>();
        AnalysisCompetitorsAdapter adapter = new AnalysisCompetitorsAdapter( getContext(),this, emptyList );
        ListView listViewCompetitorsSlots = view.findViewById( R.id.analysis_competitors_listview );
        listViewCompetitorsSlots.setAdapter( adapter );
        fillAnalysisCompetitorSlots( adapter );
    }

    private void fillAnalysisCompetitorSlots(AnalysisCompetitorsAdapter adapter) {
        // TODO...
        // Sprawdzenie w Settings, czy sloty są utworzone w lokalnej bazie danych
        // Pobranie z Settings ile jest slotów
        // Sprawdzenie czy w bazie danych jest tyle slotów, ile odczytano z preferencji
        // Jesli tak, to utworzenie listy slotów analysisCompetitorSlots i pobranie danych do slotów
        // Jeśli nie, to co??
        // Po co to sprawdzać...

        // Uwaga: w wersji dla Analizy Strategicznej z 2019 ilość slotów jest stała = 5,
        // oraz sloty mają określoną kolejność: LM, OBI, Bricoman, lokalny konurent 1, lokalny konurent 2

        // Pamiętaj - pracujesz  nie na bazie dancyh, tylko liście
        // Jeśli zmienisz liczbę slotów musisz odwzorować to w preferencjach i bazie danuch
        // Jeśli zmienisz zawartość slotu, musisz to odwzorować w bazie danych
        MutableLiveData<List<AnalysisCompetitorSlot>> result = new MutableLiveData<>();
        Observer<List<AnalysisCompetitorSlot>> resultObserver = new Observer<List<AnalysisCompetitorSlot>>() {
            @Override
            public void onChanged(List<AnalysisCompetitorSlot> slots) {
                result.removeObserver(this); // this = observer...
                if (slots != null) {
                    AnalysisCompetitorSlotList analysisCompetitorSlotList = new AnalysisCompetitorSlotList(slots);
                    askForCompetitorStores( adapter, analysisCompetitorSlotList);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllAnalysisCompetitorSlotsSortedBySlotNr(result);
    }

    private void askForCompetitorStores(
            AnalysisCompetitorsAdapter adapter,
            AnalysisCompetitorSlotList analysisCompetitorSlotList) {
        MutableLiveData<List<Store>> result = new MutableLiveData<>();
        Observer<List<Store>> resultObserver = new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> storesList) {
                result.removeObserver(this); // this = observer...
                for (CompetitorSlotFullData slotInfo : analysisCompetitorSlotList.getFullDataSlotList() ) {
                    slotInfo.setCompetitorStores( (ArrayList<Store>)
                        storesList
                            .stream()
                            .filter(store -> store.getCompanyId()==slotInfo.getSlot().getCompanyId())
                            .collect(Collectors.toList())
                    );
                }
                adapter.addAll(analysisCompetitorSlotList.getFullDataSlotList());
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllStores(result);
    }

}
