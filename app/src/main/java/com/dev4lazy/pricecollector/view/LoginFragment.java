package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
//import com.dev4lazy.pricecollector.model.logic.auth.FirebaseAuthSupport;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.utils.AppHandle;
//mport com.dev4lazy.pricecollector.model.logic.OwnServerAuthServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthSupport.LoginCallback {

    // todo ViewModel...
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHandle.getHandle().getAuthSupport().setLoginCallbackService(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            logIn();
        });
        return view;
    }

    void logIn() {
        AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
        authSupport.addCredential("USER_ID", "nowak_j" );
        authSupport.addCredential("USER_PASSWORD", "qwerty");
        authSupport.signIn();
    }


// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania

    @Override
    public void callIfSucessful() {
        Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_mainFragment);
    }

    @Override
    public void callIfUnsucessful() {
            // todo kumnuikat jakiś :-)
    }
}
