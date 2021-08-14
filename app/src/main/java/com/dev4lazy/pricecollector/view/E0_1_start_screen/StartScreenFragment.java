package com.dev4lazy.pricecollector.view.E0_1_start_screen;


import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.utils.AppPreferences;
import com.dev4lazy.pricecollector.utils.PermissionsUtils;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.util.ArrayList;

import static com.dev4lazy.pricecollector.utils.PermissionsUtils.ALL_PERMISSIONS_REQUEST;

public class StartScreenFragment extends Fragment {

    public StartScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.start_screen_fragment, container, false);
        if (checkAndRequestPermissions()) {
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

    private boolean checkAndRequestPermissions() {
        // Check which permissions are not granted
        PermissionsUtils permissionsUtils = new PermissionsUtils( this );
        ArrayList<String> listPermissionsDenied = permissionsUtils.getDeniedPermissions();
        // ask for non granted permissions
        if ( !listPermissionsDenied.isEmpty()) {
            permissionsUtils.callUserForPermissions(
                listPermissionsDenied.toArray( new String[ listPermissionsDenied.size() ]),
                ALL_PERMISSIONS_REQUEST
            );
            return false;
        }
        // App has all permissions
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        // TODO!!! sprawdź, jak będzie się zachowywać aplikacja, jeśli użytkownik zmieni uprawnienia już po uruchomieniu...
        if ( requestCode == ALL_PERMISSIONS_REQUEST ) {
            PermissionsUtils permissionsUtils = new PermissionsUtils(this);
            ArrayList<String> deniedPermissions = permissionsUtils.getDeniedPermissionsFromPermissionsArray( permissions, grantResults );
            View view = getView().findViewById(R.id.start_screen_layout);
            // Are all permissions granted?
            if (deniedPermissions.isEmpty()) {
                // Oh yes!
                Navigation.findNavController( view ).navigate( R.id.action_startScreenFragment_to_setUpPreferencesFragment );
            }
            // Oh no! Some of permission are denied...
            else {
                String deniedPermissionsMessage = permissionsUtils.getConcatenatedShortPermisionsNames( deniedPermissions );
                for ( String permissionName : deniedPermissions ) {
                    // So permission is denied (this is first time, when "never ask again" is not checked).
                    // So ask again explaining the usage of permission
                    // shouldShowRequestPermissionRationale() will return true.
                    if (shouldShowRequestPermissionRationale( permissionName )) {
                        // Show explantation info
                        showExplantationAndAskUserForPermissionDialog( view, deniedPermissionsMessage );
                        break;
                    }
                    // Permission is denied (an "never ask again" is checked )
                    // shouldShowRequestPermissionRationale() will return false.
                    else {
                        AppPreferences appPreferences = AppHandle.getHandle().getPrefs();
                        if (appPreferences.isFirstTimeAskingPermission( permissionName )) {
                            appPreferences.saveFirstTimeAskingPermission( permissionName, false );
                            permissionsUtils.callUserForPermission( permissionName, ALL_PERMISSIONS_REQUEST );
                            break;
                        } else {
                            // Ask user to go to Settings and manually allow permission
                            showExplantationAndAskUserForSystemSettings( view, deniedPermissionsMessage );
                            break;
                        }
                    }
                }
            }
        }
    }

    private void showExplantationAndAskUserForPermissionDialog(View view, String deniedPermissionsMessage ) {
        AlertDialogFragmentViewModel alertDialogFragmentViewModel = ViewModelProviders.of(getActivity()).get(AlertDialogFragmentViewModel.class);
        alertDialogFragmentViewModel.setTitle("");
        alertDialogFragmentViewModel.setMessage(
                getResources().getString(R.string.nedded_permissions_message_line1) + "\n" +
                getResources().getString(R.string.nedded_permissions_message_line2) + "\n" +
                deniedPermissionsMessage
        );
        alertDialogFragmentViewModel.setPositiveButtonLabel( getString(R.string.yes_i_want_to_allow_message) );
        alertDialogFragmentViewModel.setPostiveButtonOnClickListener(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkAndRequestPermissions();
                    }
                }
        );
        alertDialogFragmentViewModel.setNegativeButtonLabel(getString(R.string.no_i_want_to_exit_app_message));
        alertDialogFragmentViewModel.setNegativeButtonOnClickListener(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishApp();
                    }
                }
        );
        alertDialogFragmentViewModel.setCancelable(false);
        Navigation.findNavController( view ).navigate( R.id.action_startScreenFragment_to_alertDialogFragment );
    }

    private void showExplantationAndAskUserForSystemSettings(View view, String deniedPermissionsMessage ) {
        AlertDialogFragmentViewModel alertDialogFragmentViewModel = ViewModelProviders.of(getActivity()).get(AlertDialogFragmentViewModel.class);
        alertDialogFragmentViewModel.setTitle("");
        Resources resources = getResources();
        alertDialogFragmentViewModel.setMessage(
                resources.getString(R.string.nedded_permissions_message_line1) + "\n" +
                resources.getString(R.string.nedded_permissions_message_line2) + "\n" +
                deniedPermissionsMessage + "\n" +
                resources.getString(R.string.nedded_permissions_message_go_to_settings)
        );
        alertDialogFragmentViewModel.setPositiveButtonLabel(getString(R.string.yes_i_want_to_go_to_settings));
        alertDialogFragmentViewModel.setPostiveButtonOnClickListener(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Go to app settings
                        Intent intent = new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                // todo może zamiast null powinno być this?
                                Uri.fromParts( "package", getActivity().getPackageName(), null )
                        );
                        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity( intent );
                        finishApp();
                    }
                }
        );
        alertDialogFragmentViewModel.setNegativeButtonLabel(getString(R.string.no_i_want_to_exit_app_message));
        alertDialogFragmentViewModel.setNegativeButtonOnClickListener(
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishApp();
                    }
                }
        );
        alertDialogFragmentViewModel.setCancelable(false);
        Navigation.findNavController( view ).navigate( R.id.action_startScreenFragment_to_alertDialogFragment );
    }

    private void finishApp() {
        AppHandle.getHandle().shutdown();
        getActivity().finishAndRemoveTask();
        System.exit(0);
    }

}
