package com.dev4lazy.pricecollector.view.E0_2_setup_preferences_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class SetUpPreferencesFragment extends Fragment {

    public SetUpPreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo !!!! na potrzeby testu zresteowanie preferencji
        // AppHandle.getHandle().getPrefs().clear();
        //todo sprawdzenie w preferencjach jaki język i ustawienie, jeśli inny niż w telefonie
        AppHandle.getHandle().getSettings().setUp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.set_up_preferences_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // todo usuń
        Navigation.findNavController(view).navigate(R.id.action_setUpPreferencesFragment_to_loginFragment);
        super.onViewCreated(view, savedInstanceState);
    }
}
