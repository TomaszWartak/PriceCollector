package com.dev4lazy.pricecollector.view.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel;

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance( ) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment( );
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        AlertDialogFragmentViewModel alertDialogFragmentViewModel = new ViewModelProvider( getActivity() ).get( AlertDialogFragmentViewModel.class );
        return alertDialogFragmentViewModel.getAlertDialog();
    }

}
