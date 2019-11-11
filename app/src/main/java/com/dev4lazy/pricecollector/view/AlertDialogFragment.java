package com.dev4lazy.pricecollector.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel;

public class AlertDialogFragment extends DialogFragment {

    // todo Czy tu jest potrzbeny VieeModel?

    public AlertDialogFragment( ) {
        // todo ??? Empty constructor required for DialogFragment
    }


    public static AlertDialogFragment newInstance( ) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment( );
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        alertDialogFragment.setArguments(args);
        return alertDialogFragment;
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_fragment, container, false);
        return view;
    }
    */

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_fragment, null);
        // EditText editText = viewInflated.findViewById(R.id.textView2);
        // editText.setText( "azssssssss");
        AlertDialogFragmentViewModel alertDialogFragmentViewModel = ViewModelProviders.of( getActivity() ).get( AlertDialogFragmentViewModel.class );
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getContext() );
        // alertDialogBuilder.setView( viewInflated );
        alertDialogBuilder.setTitle( alertDialogFragmentViewModel.getTitle() );
        alertDialogBuilder.setMessage( alertDialogFragmentViewModel.getMessage() );
        alertDialogBuilder.setCancelable( alertDialogFragmentViewModel.isCancelable() );
        alertDialogBuilder.setPositiveButton( alertDialogFragmentViewModel.getPositiveButtonLabel(), alertDialogFragmentViewModel.getPostiveButtonOnClickListener() );
        alertDialogBuilder.setNegativeButton( alertDialogFragmentViewModel.getNegativeButtonLabel(), alertDialogFragmentViewModel.getNegativeButtonOnClickListener() );
        AlertDialog alertDialog = alertDialogBuilder.create();
        /* Jeśli chcesz przechwycić flow po wybraniu OK, aby zapobiec zamknięciu, jeśli coś jest nie tak,
            to trzeba być jak niżej zrobić.
            Z tym, że wtedy przestanie działać positiveButtonOnClickListener z ViewModel...
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO tutaj np. przygotowanie danych do walidacji
                        // TODO poniżej validacja
                        if (isValid(store)) {
                            dialog.dismiss();
                            storeViewModel.getData().setValue(store);
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    validationMessage,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
           */
        return alertDialog;
    }
}
