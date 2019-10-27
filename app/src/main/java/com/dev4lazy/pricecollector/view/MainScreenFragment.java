package com.dev4lazy.pricecollector.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.Remote2LocalConverter;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

public class MainScreenFragment extends Fragment {

    private MainViewModel mViewModel;

    private AnalysisDataUpdater analysisDataUpdater = getInstance();

    private View viewAnalysisiItem;


    // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
    // private RemoteDatabaseInitializer converter;

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

        ((TextView)view.findViewById(R.id.user_login)).setText(AppHandle.getHandle().getSettings().getUser().getLogin());

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
            // todo test
            testCopyEanCodes();
            // openTestAnalyzesList();
        });
    }

    // todo test
    private void testCopyEanCodes() {
        // pobrac wiersze do skopiowania
        // pobrac ilość wierszy do skopiowania z dlugosci listy
        // dopisac wiersze do lacal
        MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
        Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
            @Override
            public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    ArrayList<EanCode> eanCodeList = convertToEanCodes(remoteEanCodesList);
                    ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, eanCodeList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.insertEanCodes( eanCodeList, progressBarPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllEanCodes(result);

    }

    private ArrayList<EanCode> convertToEanCodes( List<RemoteEanCode> remoteEanCodesList ) {
        ArrayList<EanCode> eanCodeList = new ArrayList<>();
        Remote2LocalConverter converter = new Remote2LocalConverter();
        for ( RemoteEanCode remoteEanCode : remoteEanCodesList ) {
            // todo null się wywali... Powinien być Article
            // todo potrzeban list wszystkich artykułów, żeby z niej pobierać
            EanCode eanCode = converter.getEanCode(remoteEanCode, null);
            eanCodeList.add( eanCode );
        }
        return eanCodeList;
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
