package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dev4lazy.pricecollector.R;
//import com.dev4lazy.pricecollector.model.logic.auth.FirebaseAuthSupport;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.DataInitializer;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;
//mport com.dev4lazy.pricecollector.model.logic.OwnServerAuthServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthSupport.LoginCallback {

    // todo ViewModel...
    private UserViewModel userViewModel;

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
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            logIn(v);
        });
        return view;
    }

    void logIn( View view ) {
        EditText editTextUserLogin = view.findViewById( R.id.userlogin_edit_text);
        EditText editTextUserPassword = view.findViewById(R.id.password_edit_text);
        // todo zrób tu test jak login i hasło przeżywają bez viewmodelu i z viewmodelem
        userViewModel.setUser(new User());
        userViewModel.getUser().setLogin(editTextUserLogin.getText().toString());
        AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
        /**/authSupport.addCredential("USER_ID", "nowak_j" );
        /**/authSupport.addCredential("USER_PASSWORD", "qwerty");
        //authSupport.addCredential("USER_ID", userViewModel.getUser().getLogin() );
        //authSupport.addCredential("USER_PASSWORD", editTextUserPassword.getText().toString());
        authSupport.signIn();
    }


// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania

    @Override
    public void callIfSucessful() {
        AppHandle.getHandle().getSettings().setUser(userViewModel.getUser());
        userViewModel.clear();
        // todo jesli pierwsze uruchomienie, to incjalizacja danych w bazie lokalnej
        if (!AppHandle.getHandle().getPrefs().getLocalDatabaseInitialized()) {
            DataInitializer.getInstance().initializeLocalDatabase();
        }
        Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_mainFragment);
    }

    @Override
    public void callIfUnsucessful() {
            // todo kumnuikat jakiś :-)
        Toast.makeText(
                getContext(),
                "coś nie bangla...",
                Toast.LENGTH_SHORT).show();

    }
}
