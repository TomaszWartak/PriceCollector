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
import com.dev4lazy.pricecollector.model.logic.auth.MockCustomTokenOwnAuthSupport;
import com.dev4lazy.pricecollector.utils.AppHandle;
//mport com.dev4lazy.pricecollector.model.logic.OwnServerAuthServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthSupport.LoginCallback {

    // todo ViewModel...

    // obsługa logowania
    private AuthSupport authSupport = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //authSupport = new MockCustomTokenOwnAuthSupport();
        authSupport = AppHandle.getHandle().getAuthSupport();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            signIn();
        });
        /* todo usuń?
        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
         */
        return view;
    }

    void signIn() {
        authSupport.addCredential("USER_ID", "nowak_j" );
        authSupport.addCredential("USER_PASSWORD", "qwerty");
        w interfejsie AuthSupport spróbuj przeniesc setLoginCallback() z LoginCallback do AuthSupport
        Wtedy nie będzie potrzebne rzutowanie, jak niżej
        ((MockCustomTokenOwnAuthSupport) authSupport).setLoginCallbackService(this);
        authSupport.signIn();
    }

    // todo zakomentowałem, bo wylogowywyało przy obrocie ekranu
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();
        // todo !!!!!
        //  1. czy na pewno w onDestroy?
        //  2. czyli co? Jak fragment zostanie zamknięty, to nastąpi wylogowanie?
        //  niestety tak - przy obrocie wylogowuje...
        // Ad 2 to chyba dla mocka na Servisie tak powinno tylko byc...
        // można to dać w AppHandle przy zamknięciu?
        authSupport.signOut();
    }
    */

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
