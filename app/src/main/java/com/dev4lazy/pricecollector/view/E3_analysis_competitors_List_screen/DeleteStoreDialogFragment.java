package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteStoreDialogFragment extends DialogFragment {

    private String validationMessage = "";

    public static DeleteStoreDialogFragment newInstance() {
        return new DeleteStoreDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.delete_store_fragment, null);

        EditText companyNameEditText = viewInflated.findViewById(R.id.delete_store_company_name_edit_text);
        EditText storeNameEditText = viewInflated.findViewById(R.id.delete_store_name_edit_text);
        EditText streetEditText = viewInflated.findViewById(R.id.delete_store_street_edit_text);
        EditText cityEditText = viewInflated.findViewById(R.id.delete_store_city_edit_text);
        EditText zipcodeEditText = viewInflated.findViewById(R.id.delete_store_zipcode_edit_text);

        StoreViewModel storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);
        Store store = storeViewModel.getStore();
        String dialogTitle = "";
        switch (storeViewModel.getActionToDo()) {
            case ADD: {
                dialogTitle = getString(R.string.add_competitor_store);
                break;
            }
            case MODIFY: {
                dialogTitle = getString(R.string.edit_competitor_store);
                storeNameEditText.setText( store.getName() );
                streetEditText.setText( store.getStreet() );
                cityEditText.setText( store.getCity() );
                zipcodeEditText.setText( store.getZipCode() );
                break;
            }
            case DELETE: {
                dialogTitle = getString(R.string.delete_competitor_store);
                storeNameEditText.setEnabled(false);
                storeNameEditText.setText( store.getName() );
                streetEditText.setEnabled(false);
                streetEditText.setText( store.getStreet() );
                cityEditText.setEnabled(false);
                cityEditText.setText( store.getCity() );
                zipcodeEditText.setEnabled(false);
                zipcodeEditText.setText( store.getZipCode() );
                break;
            }
            default:
        }

        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companies) {
                result.removeObserver(this); // this = observer...
                if ((companies!=null) && (!companies.isEmpty())) {
                    Company company = companies.get(0);
                    companyNameEditText.setText(company.getName());
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyById(store.getCompanyId(),result);

        return getDeleteStoreDialog(
                viewInflated,
                storeViewModel,
                store,
                dialogTitle,
                storeNameEditText,
                streetEditText,
                cityEditText,
                zipcodeEditText);
    }

    @NonNull
    private AlertDialog getDeleteStoreDialog(
            View viewInflated,
            StoreViewModel storeViewModel,
            Store store,
            String dialogTitle,
            EditText storeNameEditText,
            EditText streetEditText,
            EditText cityEditText,
            EditText zipcodeEditText) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle(dialogTitle)
                .setView(viewInflated) // jeśli dialog ma mieć niestandarodowy widok
                .setPositiveButton(R.string.caption_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // onClick zostaje pusta ze względu na walidację (zob. niżej onShow() )
                    }
                })
                .setNegativeButton(R.string.caption_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeViewModel.getData().removeObservers(AppUtils.getActivity(getContext()));
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Store storeToDelete = new Store();
                        storeToDelete.setId( store.getId() );
                        storeViewModel.setStore(storeToDelete);
                    }
                });
            }
        });
        return alertDialog;
    }
}
