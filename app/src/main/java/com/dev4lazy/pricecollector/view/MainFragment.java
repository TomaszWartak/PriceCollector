package com.dev4lazy.pricecollector.view;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private AnalysisDataUpdater analysisDataUpdater = getInstance();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo tutaj zainicjoawanie wykorzystywanych później obiektów
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        // todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
        getPreferncesInfo();
        // todo jeśli analiza jest w trakcie - możliwość kontynuacji
        // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - wyświetlenie
        getNewAnalysisInfo();
        // todo incializacja menu, czy szuflady?
        return view;
    }

    private void getPreferncesInfo() {
        //todo
    }

    private void getNewAnalysisInfo() {
        analysisDataUpdater.checkNewAnalysisToDownload();
        if (analysisDataUpdater.isNewAnalysisDataReadyToDownlad()) {
            analysisDataUpdater.downloadAnalysisBasicData();
        }
    }

//------------------------------------------------------------------------
// Obsługa Drawer menu
    private void menuOptionAnalyzes() {
    }

    private void menuOptionNewAnalysis() {
        // tego nie implementuję, bo w tej wersji nie będzie możliwości tworzenia własnych analiz
    }

    private void menuOptionCompetitors() {

    }

    private void menuOptionNewCompetitor() {

    }

    private void menuOptionPreferences() {

    }

    private void menuOptionLogOff() {

    }

    private void menuOptionMainScreen() {

    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}
