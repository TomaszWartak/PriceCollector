package com.dev4lazy.pricecollector.view;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddStoreDialogFragment extends DialogFragment {

    private String validationMessage = "";

    // todo ViewModel...
    public AddStoreDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_store_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Resources resources = getResources();
        builder.setTitle(resources.getString(R.string.add_competitor_store));

        StoreViewModel storeViewModel = ViewModelProviders.of(getActivity()).get(StoreViewModel.class);
        /* tod? storeViewModel.getData().observe(this, new Observer<Store>() {
            @Override
            public void onChanged(Store store) {

            }
        });
        */
        Store store = storeViewModel.getData().getValue();

        EditText companyNameEditText = viewInflated.findViewById(R.id.company_name_edit_text);
        EditText storeNameEditText = viewInflated.findViewById(R.id.store_name_edit_text);
        EditText streetEditText = viewInflated.findViewById(R.id.store_street_edit_text);
        EditText cityEditText = viewInflated.findViewById(R.id.store_city_edit_text);
        EditText zipcodeEditText = viewInflated.findViewById(R.id.store_zipcode_edit_text);

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

        builder.setView(viewInflated);
        builder.setPositiveButton(R.string.caption_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // onClick zostaje pusta ze względu na walidację (zob. niżej onShow() )
                // todo store.setCompanyId();
                // companyName = companyNameEditText.getText().toString();
                /*
                store.setName( storeNameEditText.getText().toString() );
                store.setStreet( streetEditText.getText().toString() );
                store.setCity( cityEditText.getText().toString() );
                store.setZipCode( zipcodeEditText.getText().toString() );
                if (isValid(store)) {
                    dialog.dismiss();
                } else {
                    Toast.makeText(
                        getContext(),
                        validationMessage,
                        Toast.LENGTH_LONG).show();
                }
                */
                /*
                Note note = new Note.NoteBuilder()
                        .priority(priority)
                        .creationDate(System.currentTimeMillis())
                        .title(title)
                        .content(content)
                        .kind(noteKind)
                        .build();
                mViewModel.insertNote(note);
                 */
            }
        });
        builder.setNegativeButton(R.string.caption_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //builder.show();
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        store.setName( storeNameEditText.getText().toString() );
                        store.setStreet( streetEditText.getText().toString() );
                        store.setCity( cityEditText.getText().toString() );
                        store.setZipCode( zipcodeEditText.getText().toString() );
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
        return alertDialog /* todo builder.create() */;
    }

    private boolean isValid(Store store) {
        validationMessage = "";
        if (store.getCompanyId()<0) {
            validationMessage = getString(R.string.choose_competitor);
            return false;
        }
        if (store.getName()==null) {
            validationMessage = getString(R.string.store_name_null);
            return false;
        }
        if (store.getName().isEmpty()) {
            validationMessage = getString(R.string.enter_store_name);
            return false;
        }
       if (store.getStreet()==null) {
            validationMessage = getString(R.string.street_null);
            return false;
        }
        if (store.getStreet().isEmpty()) {
            validationMessage = getString(R.string.enter_street_name);
            return false;
        }
        if (store.getCity()==null) {
            validationMessage = getString(R.string.city_null);
            return false;
        }
        if (store.getCity().isEmpty()) {
            validationMessage = getString(R.string.enter_city_name);
            return false;
        }
        if (store.getZipCode()==null) {
            validationMessage = getString(R.string.zipcode_null);
            return false;
        }
        if (store.getZipCode().isEmpty()) {
            validationMessage = getString(R.string.enter_zipcode);
            return false;
        }
        return true;
    }
}
