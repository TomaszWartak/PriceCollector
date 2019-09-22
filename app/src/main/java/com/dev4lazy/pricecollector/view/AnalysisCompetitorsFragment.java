package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;

import java.util.List;

public class AnalysisCompetitorsFragment extends Fragment {

    private List<AnalysisCompetitorSlot> analysisCompetitorSlots;

    public AnalysisCompetitorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.analysis_competitors_fragment, container, false);
    }

    private void fillAnalysisCompetitorSlots() {
        // Sprawdzenie w Settings, czy sloty są utworzone w lokalnej bazie danych
        // Pobranie z Settings ile jest slotów
        // Sprawdzenie czy w bazie danych jest tyle slotów, ile odczytano z preferencji
        // Jesli tak, to utworzenie listy slotów analysisCompetitorSlots i pobranie danych do slotów
        // Jeśli nie,

        // Uwaga: w wersji dla Analizy Strategicznej z 2019 ilość slotów jest stała = 5,
        // oraz sloty mają określoną kolejność: LM, OBI, Bricoman, lokalny konurent 1, lokalny konurent 2

        // Pamiętaj - pracujesz na nie na bazie dancyh, tylko liście
        // Jeśli zmienisz liczbę slotów musisz odwzorować to w preferencjach i bazie danuch
        // Jeśli zmienisz zawartość slotu, musisz to odwzorować w bazie danych

    }

}
