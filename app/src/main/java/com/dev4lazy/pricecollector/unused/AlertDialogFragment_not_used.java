package com.dev4lazy.pricecollector.unused;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.dev4lazy.pricecollector.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertDialogFragment_not_used extends DialogFragment {

    public static AlertDialogFragment_not_used newInstance( ) {
        AlertDialogFragment_not_used alertDialogFragment = new AlertDialogFragment_not_used( );
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        AlertDialogFragmentViewModel_not_used alertDialogFragmentViewModel = new ViewModelProvider( getActivity() ).get( AlertDialogFragmentViewModel_not_used.class );
        return getAlertDialog(alertDialogFragmentViewModel);
    }

    @NonNull
    private AlertDialog getAlertDialog(AlertDialogFragmentViewModel_not_used alertDialogFragmentViewModel) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(
                    getContext(),
                    R.style.PC_AlertDialogStyle_Overlay )
                .setTitle( alertDialogFragmentViewModel.getTitle().toUpperCase() )
                .setMessage( alertDialogFragmentViewModel.getMessage() )
                .setCancelable( alertDialogFragmentViewModel.isCancelable() )
                .setPositiveButton(
                        alertDialogFragmentViewModel.getPositiveButtonLabel(),
                        alertDialogFragmentViewModel.getPostiveButtonOnClickListener() )
                .setNegativeButton(
                        alertDialogFragmentViewModel.getNegativeButtonLabel(),
                        alertDialogFragmentViewModel.getNegativeButtonOnClickListener() )
                .create();
        return alertDialog;
    }
}
