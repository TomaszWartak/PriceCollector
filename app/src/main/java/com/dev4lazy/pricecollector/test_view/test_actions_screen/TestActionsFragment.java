package com.dev4lazy.pricecollector.test_view.test_actions_screen;


import android.content.pm.PackageManager;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TestActionsFragment extends Fragment {


    public TestActionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test_actions_fragment, container, false);
        // todo test

        setTestButtons(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void setTestButtons(View view) {
        view.findViewById(R.id.button_clear_remote).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().clearRemoteDatabase();
        });
        view.findViewById(R.id.button_create_remote).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().initializeRemoteDatabase();
        });
        view.findViewById(R.id.button_show_remote).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_remoteAnalysisRowJoinFragment);
        });
        view.findViewById(R.id.button_clear_local).setOnClickListener((View v) -> {
            LocalDataInitializer.getInstance().clearLocalDatabase();
        });
        view.findViewById(R.id.button_create_local).setOnClickListener((View v) -> {
            LocalDataInitializer.getInstance().initializeLocalDatabase();
        });
        view.findViewById(R.id.button_countries).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_countriesListFragment);
        });
        view.findViewById(R.id.button_companies).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_companiesListFragment);
        });
        view.findViewById(R.id.button_own_stores).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_ownStoresListFragment);
        });
        view.findViewById(R.id.button_other_stores).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_otherStoresListFragment);
        });
        view.findViewById(R.id.button_show_articles).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_articlesListFragment);
        });
        /* todo?
        view.findViewById(R.id.button_show_depts_in_secs).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_departmentInSectorsListFragment);
        });
          */
        view.findViewById(R.id.button_show_numbers_of_data).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment_to_testNumbersOfDataFragment);
        });

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
