package com.dev4lazy.pricecollector.view.E0_1_start_screen;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.AppHandle;

import com.dev4lazy.pricecollector.utils.AppSettings;
import com.dev4lazy.pricecollector.utils.PermissionsUtils;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel2;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import static com.dev4lazy.pricecollector.utils.PermissionsUtils.ALL_PERMISSIONS_REQUEST;

public class StartScreenFragment extends Fragment {

    public StartScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_screen_fragment, container, false);
        setOnBackPressedCallback();
        return view;
    }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    new LogoutQuestionDialog( getContext(), getActivity() ).get();
                    // TODO XXX getLogoutQuestionDialog();
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        /* TODO XXX
            private void getLogoutQuestionDialog() {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_yes), new LogoutDialogListener( getActivity() ) )
                        .setNegativeButton(getActivity().getString(R.string.caption_no),null)
                        .show();
            }

         */

            /* TODO XXX
                private class LogoutDialogListener implements DialogInterface.OnClickListener {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishApp();
                    }

                }

             */


    @Override
    public void onStart() {
        super.onStart();
        if (checkAndRequestPermissions()) {
            Navigation.findNavController(getView()).navigate(R.id.action_startScreenFragment_to_setUpPreferencesFragment);
        }
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
                            AppSettings appSettings = AppHandle.getHandle().getSettings();
                            if (appSettings.isFirstTimeAskingPermission( permissionName )) {
                                appSettings.saveFirstTimeAskingPermission( permissionName, false );
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
            /* TODO XXX
            To musisz przetestować - zmiana AlertDialogFragment na AlertDialogFragment2
            musisz zdjąć uprawnienia i asprawdzić, jak dialog się wyświetli

             */
            AlertDialogFragmentViewModel2 alertDialogFragmentViewModel =
                    new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel2.class);
            alertDialogFragmentViewModel.setAlertDialog(
                    getExplantationAndAskUserForPermissionDialog( deniedPermissionsMessage )
            );
            Navigation.findNavController( view ).navigate( R.id.action_startScreenFragment_to_alertDialogFragment2 );
        }

        private AlertDialog getExplantationAndAskUserForPermissionDialog( String deniedPermissionsMessage ) {
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(
                        getContext(),
                        R.style.PC_AlertDialogStyle_Overlay )
                    .setTitle( "")
                    .setMessage(
                            getString(R.string.nedded_permissions_message_line1) + "\n" +
                                    getString(R.string.nedded_permissions_message_line2) + "\n" +
                                    deniedPermissionsMessage )
                    .setPositiveButton(
                            getString(R.string.yes_i_want_to_allow_message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    checkAndRequestPermissions();
                                }
                            }
                    )
                    .setNegativeButton(
                            getString(R.string.no_i_want_to_exit_app_message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finishApp();
                                }
                            }
                    )
                    .setCancelable( false )
                    .create();
            return alertDialog;
        }

        private void showExplantationAndAskUserForSystemSettings(View view, String deniedPermissionsMessage ) {
            /* TODO usuń
            AlertDialogFragmentViewModel alertDialogFragmentViewModel = ViewModelProviders.of(getActivity()).get(AlertDialogFragmentViewModel.class);
            alertDialogFragmentViewModel.setTitle("");
            alertDialogFragmentViewModel.setMessage(
                    getString(R.string.nedded_permissions_message_line1) + "\n" +
                    getString(R.string.nedded_permissions_message_line2) + "\n" +
                    deniedPermissionsMessage + "\n" +
                    getString(R.string.nedded_permissions_message_go_to_settings)
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
             */

            AlertDialogFragmentViewModel2 alertDialogFragmentViewModel2 =
                    new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel2.class);
            alertDialogFragmentViewModel2.setAlertDialog(
                    getExplantationAndAskUserForSystemSettingsDialog(deniedPermissionsMessage)
            );

            Navigation.findNavController( view ).navigate( R.id.action_startScreenFragment_to_alertDialogFragment2 );
        }

        @NonNull
        private AlertDialog getExplantationAndAskUserForSystemSettingsDialog(String deniedPermissionsMessage) {
            AlertDialog alertDialog = new MaterialAlertDialogBuilder(
                        getContext(),
                        R.style.PC_AlertDialogStyle_Overlay )
                    .setTitle("")
                    .setMessage(
                            getString( R.string.nedded_permissions_message_line1) + "\n" +
                                    getString(R.string.nedded_permissions_message_line2) + "\n" +
                                    deniedPermissionsMessage + "\n" +
                                    getString(R.string.nedded_permissions_message_go_to_settings) )
                    .setPositiveButton(
                            getString(R.string.yes_i_want_to_go_to_settings),
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
                    )
                    .setNegativeButton(
                            getString(R.string.no_i_want_to_exit_app_message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finishApp();
                                }
                            }
                    )
                    .setCancelable(false)
                    .create();
            return alertDialog;
        }

        private void finishApp() {
            AppHandle.getHandle().shutdown();
            getActivity().finishAndRemoveTask();
            System.exit(0);
        }

}
