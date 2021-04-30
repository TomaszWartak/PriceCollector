package com.dev4lazy.pricecollector.test_view.outside_app_data_initializatiion_screen;


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
import com.dev4lazy.pricecollector.remote_data.RemoteDataInitializer;
import com.dev4lazy.pricecollector.utils.AppHandle;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestActionsFragment2 extends Fragment {


    public TestActionsFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.test_actions_fragment2, container, false);
        // todo test

        setTestButtons(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void setTestButtons(View view) {
        view.findViewById(R.id.button_clear_remote_users).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().clearRemoteUsers();
        });
        view.findViewById(R.id.button_create_remote_users).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().initializeRemoteUsersOnly();
        });
        view.findViewById(R.id.button_show_remote_users).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment2_to_remoteUsersListFragment);
        });

        view.findViewById(R.id.button_clear_remote2).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().clearRemoteDatabase();
        });
        view.findViewById(R.id.button_create_remote2).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().initializeRemoteDatabase();
        });
        view.findViewById(R.id.button_show_remote2).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment2_to_remoteAnalysisRowJoinFragment);
        });

        /* todo ?
        view.findViewById(R.id.button_clear_remote_sects_depts).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().clearRemoteSectors();
            RemoteDataInitializer.getInstance().clearRemoteDepartments();
        });
        view.findViewById(R.id.button_create_remote_sects_depts).setOnClickListener((View v) -> {
            RemoteDataInitializer.getInstance().prepareRemoteSectors();
            RemoteDataInitializer.getInstance().populateRemoteSectors();
            RemoteDataInitializer.getInstance().prepareRemoteDepartments();
            RemoteDataInitializer.getInstance().populateRemoteDepartments();
        });
        view.findViewById(R.id.button_show_remote_sectors).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment2_to_remoteSectorsListFragment);
        });
        view.findViewById(R.id.button_show_remote_departments).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment2_to_remoteDepartmentsListFragment);
        });

         */

        view.findViewById(R.id.button_clear_local2).setOnClickListener((View v) -> {
            LocalDataInitializer.getInstance().clearLocalDatabase();
        });
        view.findViewById(R.id.button_show_numbers_of_data2).setOnClickListener((View v) -> {
            Navigation.findNavController(view).navigate(R.id.action_testActionsFragment2_to_testNumbersOfDataFragment2);
        });
        view.findViewById(R.id.button_add_second_remote_analysis).setOnClickListener((View v) -> {
            RemoteDataInitializer remoteDataInitializer = RemoteDataInitializer.getInstance();
            remoteDataInitializer.prepareRemoteAnalyzes();
            remoteDataInitializer.populateRemoteAnalysis( 1 );
        });
        view.findViewById(R.id.button_add_third_remote_analysis).setOnClickListener((View v) -> {
            RemoteDataInitializer remoteDataInitializer = RemoteDataInitializer.getInstance();
            remoteDataInitializer.prepareRemoteAnalyzes();
            remoteDataInitializer.populateRemoteAnalysis( 2 );
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
