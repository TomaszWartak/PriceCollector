package com.dev4lazy.pricecollector.view;


import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.utils.PermissionsUtils;

import java.util.ArrayList;

import static com.dev4lazy.pricecollector.utils.PermissionsUtils.MY_PERMISSIONS_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartScreenFragment extends Fragment {

    String[] appPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    public StartScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.start_screen_fragment, container, false);
        if (areAllPermissionGranted()) {
            // todo lambda
            view.findViewById(R.id.start_screen_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_startScreenFragment_to_setUpPreferencesFragment);
                }
            });
        }
        return view;
    }

    private boolean areAllPermissionGranted() {
        // Check which permissions are granted
        PermissionsUtils permissionsUtils = new PermissionsUtils(this);
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();
        for ( String permission : appPermissions ) {
            if (permissionsUtils.isPermissionNotGranted( permission )) {
                listPermissionsNeeded.add( permission );
            }
        }
        // ask for non granted permissions
        if ( !listPermissionsNeeded.isEmpty()) {
            permissionsUtils.callUserForPermissions(
                listPermissionsNeeded.toArray( new String[ listPermissionsNeeded.size() ]),
                MY_PERMISSIONS_REQUEST
            );
            return false;
        }
        // App has all permissions
        return true;
    }

}
