package com.dev4lazy.pricecollector.test_view.test_actions_screen;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_model.utils.RemoteDataInitializer;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.view.utils.PopupWindowWrapper;
import com.dev4lazy.pricecollector.view.utils.ProgressBarWrapper;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;
import com.dev4lazy.pricecollector.view.utils.ProgressPresentingManager;
import com.dev4lazy.pricecollector.view.utils.TextViewMessageWrapper;

import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DATA_SIZE_UNKNOWN;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DONT_HIDE_WHEN_FINISHED;

public class TestActionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test_actions_fragment, container, false);
        setTestButtons(view);
        return view;
    }

    private void setTestButtons(View parentView) {
        parentView.findViewById(R.id.button_clear_remote).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().clearRemoteDatabase();
        });
        parentView.findViewById(R.id.button_create_remote).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().initializeRemoteData(
                    getPopulatingDataProgressPresentingManager( parentView )
            );
        });
        parentView.findViewById(R.id.button_show_remote).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_remoteAnalysisRowJoinFragment);
        });
        parentView.findViewById(R.id.button_clear_local).setOnClickListener((View v) -> {
            LocalDataInitializer.getInstance().clearLocalDatabase( null );
        });
        parentView.findViewById(R.id.button_create_local).setOnClickListener((View v) -> {
            LocalDataInitializer.getInstance().initializeLocalDatabase();
        });
        parentView.findViewById(R.id.button_countries).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_countriesListFragment);
        });
        parentView.findViewById(R.id.button_companies).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_companiesListFragment);
        });
        parentView.findViewById(R.id.button_own_stores).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_ownStoresListFragment);
        });
        parentView.findViewById(R.id.button_other_stores).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_otherStoresListFragment);
        });
        parentView.findViewById(R.id.button_show_articles).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_articlesListFragment);
        });
        /* todo?
        view.findViewById(R.id.button_show_depts_in_secs).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_departmentInSectorsListFragment);
        });
          */
        parentView.findViewById(R.id.button_show_numbers_of_data).setOnClickListener((View v) -> {
            Navigation.findNavController(parentView).navigate(R.id.action_testActionsFragment_to_testNumbersOfDataFragment);
        });
    }

    private ProgressPresentingManager getPopulatingDataProgressPresentingManager(View parentView ) {
        PopupWindowWrapper populatingDataPopupWindowWrapper =
                new PopupWindowWrapper(
                        parentView,
                        R.layout.remote_populate_data_popup_window ).
                        setWidth( ViewGroup.LayoutParams.MATCH_PARENT ).
                        setHeight( ViewGroup.LayoutParams.WRAP_CONTENT ).
                        setGravity( Gravity.CENTER ).
                        setOutsideTouchable( false ).
                        setFocusable( false ).
                        show(0 , 0);
        View popupView = populatingDataPopupWindowWrapper.getPopupView();
        return new ProgressPresentingManager(
                new ProgressPresenter(
                        new ProgressBarWrapper(
                                popupView.findViewById(R.id.remote_populate_data__progressBar)
                        ),
                        DATA_SIZE_UNKNOWN,
                        DONT_HIDE_WHEN_FINISHED
                ),
                new TextViewMessageWrapper(
                        popupView.findViewById(R.id.remote_populate_data__progress_message)
                ),
                populatingDataPopupWindowWrapper
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if ((grantResults.length > 0) && (grantResults[0] ==
                PackageManager.PERMISSION_GRANTED)) {
            // Dostęp nadany
            switch (requestCode) {
                /*
                // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
                case RemoteDatabaseInitializer.MY_PERMISSIONS_REQUEST_STORAGE: {
                    new RemoteDatabaseInitializer(this).doConversion();
                    break;
                }
                 */
            }
        } else {
            // Dostęp nie udany. Wyświetlamy Toasta
            Toast.makeText(
                    AppHandle.getHandle().getApplicationContext(),
                    getString(R.string.access_denied),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
