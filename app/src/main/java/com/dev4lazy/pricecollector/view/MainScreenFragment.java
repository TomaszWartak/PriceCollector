package com.dev4lazy.pricecollector.view;

import androidx.activity.OnBackPressedCallback;

import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

public class MainScreenFragment extends Fragment {

    private MainViewModel mViewModel;

    private AnalysisDataUpdater analysisDataUpdater = getInstance();

    private View viewAnalysisiItem;


    // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
    // private Converter converter;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo tutaj zainicjoawanie wykorzystywanych później obiektów tej klasy
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        setOnBackPressedCalback();

        setAnalysisItem(inflater, view);

        // todo test
        view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_testActionsFragment);
        });

        // todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
        getPreferencesInfo(); // todo ????

        // todo jeśli analiza jest w trakcie - możliwość kontynuacji
        // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - wyświetlenie
        getNewAnalysisInfo();

        // todo incializacja menu, czy szuflady?
        return view;
    }

    private void setAnalysisItem(@NonNull LayoutInflater inflater, View view) {
        viewAnalysisiItem = inflater.inflate(R.layout.test_analysis_item,null);
        ((ViewGroup) view).addView(viewAnalysisiItem);
        view.findViewById(R.id.card_view_constraint_layout).setOnClickListener((View v) -> {
            openTestAnalyzesList();
        });
    }

    //todo test
    private void openTestAnalyzesList() {
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_analysisCompetitorsFragment);
    }

    private void  setOnBackPressedCalback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                new MaterialAlertDialogBuilder(getContext())/*, R.style.AlertDialogStyle) */
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_ok), new LogOffListener() )
                        .setNegativeButton(getActivity().getString(R.string.caption_cancel),null)
                        .show();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private class LogOffListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            AppHandle.getHandle().shutDown();
            getActivity().finishAndRemoveTask();
            System.exit(0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }


    private void getPreferencesInfo() {
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

}
