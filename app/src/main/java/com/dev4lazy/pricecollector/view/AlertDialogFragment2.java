package com.dev4lazy.pricecollector.view;

import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.CountryDao;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel2;

public class AlertDialogFragment2 extends DialogFragment {

    // todo Czy tu jest potrzbeny VieeModel?

    public AlertDialogFragment2( ) {
        // todo ??? Empty constructor required for DialogFragment
    }

    // todo: znów jest to bezsensowne newInstance, gdzie jest po prostu konstruktor wołany...
    public static AlertDialogFragment2 newInstance( ) {
        AlertDialogFragment2 alertDialogFragment = new AlertDialogFragment2( );
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        AlertDialogFragmentViewModel2 alertDialogFragmentViewModel = ViewModelProviders.of( getActivity() ).get( AlertDialogFragmentViewModel2.class );
        /* todo?
        AlertDialog alertDialog = new AlertDialog.Builder( getContext() )
                .setTitle( alertDialogFragmentViewModel.getTitle() )
                .setMessage( alertDialogFragmentViewModel.getMessage() )
                .setCancelable( alertDialogFragmentViewModel.isCancelable() )
                .setPositiveButton( alertDialogFragmentViewModel.getPositiveButtonLabel(), alertDialogFragmentViewModel.getPostiveButtonOnClickListener() )
                .setNegativeButton( alertDialogFragmentViewModel.getNegativeButtonLabel(), alertDialogFragmentViewModel.getNegativeButtonOnClickListener() )
                .create();

         */

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
        return alertDialogFragmentViewModel.getAlertDialog();
    }

    public static class CountryListViewModel extends AndroidViewModel {

        private LiveData<PagedList<Country>> countriesLiveData;

        public CountryListViewModel(Application application) {
            super(application);
            CountryDao countryDao = AppHandle.getHandle().getLocalDatabase().countryDao();
            DataSource.Factory<Integer, Country>  factory = countryDao.getAllPaged();
            LivePagedListBuilder<Integer, Country> pagedListBuilder = new LivePagedListBuilder<Integer, Country>(factory, 50);
            countriesLiveData = pagedListBuilder.build();
        }

        public LiveData<PagedList<Country>> getCountriesLiveData() {
            return countriesLiveData;
        }
    }
}
