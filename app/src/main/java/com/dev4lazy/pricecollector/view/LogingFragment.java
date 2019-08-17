package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
//import com.dev4lazy.pricecollector.model.logic.FirebaseAuthServices;
import com.dev4lazy.pricecollector.model.logic.MockCustomTokenOwnAuthServices;
import com.dev4lazy.pricecollector.model.logic.OwnServerAuthServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogingFragment extends Fragment {

    // todo ViewModel...

    // Logowanie/wylogowanie - Firebase
    //private CustomTokenFirebaseAuthServices firebaseAuthServices = null;
    // todo private FirebaseAuthServices firebaseAuthServices = null;

    // Logowanie/wylogowanie - własny serwer logowania
    private OwnServerAuthServices customTokenAuthService = null;

    public LogingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo firebaseAuthServices = CustomTokenFirebaseAuthServices.getInstance();
        customTokenAuthService = new MockCustomTokenOwnAuthServices();
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.loging_fragment, container, false);
        view.findViewById(R.id.loging_fragment_framelayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_logingFragment_to_mainFragment);
            }
        });
        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo test logowanie do mocka
                customTokenAuthService.addCredential("USER_ID", "nowak_j" );
                customTokenAuthService.addCredential("USER_PASSWORD", "qwerty");
                customTokenAuthService.signInOwnServer();
                // todo firebaseAuthServices.addCredential("TOKEN", customTokenAuthService.getCustomToken());
                // todo firebaseAuthServices.signInFirebase();
                 //firebaseUser = firebaseAuthServices.getCurrentFirebaseUser();
                //firebaseAuthServices.signOutFromFirebase();
                //customTokenAuthService.signOutCustomAuthServer();
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
        customTokenAuthService.signOutFromOwnServer();
    }
}
