package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
//import com.dev4lazy.pricecollector.model.logic.FirebaseAuthSupport;
import com.dev4lazy.pricecollector.model.logic.AuthSupport;
import com.dev4lazy.pricecollector.model.logic.MockCustomTokenOwnAuthSupport;
//mport com.dev4lazy.pricecollector.model.logic.OwnServerAuthServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogingFragment extends Fragment implements AuthSupport.LoginCallback {

    // todo ViewModel...

    // obsługa logowania
    private AuthSupport authService = null;

    public LogingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new MockCustomTokenOwnAuthSupport();
    }

    void signIn() {
        authService.addCredential("USER_ID", "nowak_j" );
        authService.addCredential("USER_PASSWORD", "qwerty");
        ((MockCustomTokenOwnAuthSupport) authService).setLoginCallbackService(this);
        authService.signIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.loging_fragment, container, false);
        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // todo
        //  1. czy na pewno w onDestroy?
        //  2. czyli co? Jak fragment zostanie zamknięty, to nastąpi wylogowanie?
        // Ad 2 to chyba dla mocka na Servisie tak powinno tylko byc...
        // można to dać w AppHandle przy zamknięciu?
        authService.signOut();
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania
    @Override
    public void setLoginCallbackService(AuthSupport.LoginCallback loginCallback) {
        // Ponieważ LOginFragment jest ostatnim (pierwszym?) ogniwem w wywołaniu callbacków obsługi
        // logowania, to nie trzeba tutaj nic robić.
    }

    @Override
    public void callIfSucessful() {
        Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_mainFragment);
    }

    @Override
    public void callIfUnsucessful() {
            // todo kumnuikat jakiś :-)
    }
}
