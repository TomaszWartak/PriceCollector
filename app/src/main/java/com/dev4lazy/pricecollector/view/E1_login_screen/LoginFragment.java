package com.dev4lazy.pricecollector.view.E1_login_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_data.RemoteUser;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;

import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

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
        view.findViewById(R.id.login_fragment_layout).setOnClickListener((View v) -> {
            Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_testActionsFragment2);
        });
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            logIn(view);
        });
        return view;
    }

    void logIn( View view ) {
        EditText editTextUserLogin = view.findViewById( R.id.userlogin_edit_text);
        EditText editTextUserPassword = view.findViewById(R.id.password_edit_text);
        // todo zrób tu test jak login i hasło przeżywają bez viewmodelu i z viewmodelem
        User user = new User();
        /**/user.setLogin( editTextUserLogin.getText().toString() );
        user.setLogin("nowak_j");
        userViewModel.setUser( user );
        AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
        /**/authSupport.addCredential("USER_ID", "nowak_j" );
        /**/authSupport.addCredential("USER_PASSWORD", "nowak");
        //authSupport.addCredential("USER_ID", user.getLogin() );
        //authSupport.addCredential("USER_PASSWORD", editTextUserPassword.getText().toString());
        authSupport.signIn();
    }


// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania

    @Override
    public void callIfSucessful() {
        // todo RemoteUser remoteUser = new RemoteUser();
        // todo remoteUser.setLogin(userViewModel.getUser().getLogin());

        MutableLiveData<List<RemoteUser>> findRemoteUserResult = new MutableLiveData<>();
        Observer<List<RemoteUser>>findRemoteUserResultObserver = new Observer<List<RemoteUser>>() {
            @Override
            public void onChanged(List<RemoteUser> remoteUsers ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                findRemoteUserResult.removeObserver(this); // this = observer...
                if ( (!remoteUsers.isEmpty()) && (remoteUsers.get(0)!=null) ) {
                    RemoteUser remoteUser = remoteUsers.get(0);
                    AppHandle.getHandle().getSettings().setUser( new User( remoteUser ) );
                    // userViewModel.clear(); todo nie ma takiej metody...
                    // todo jesli pierwsze uruchomienie, to incjalizacja danych w bazie lokalnej
                    if (AppHandle.getHandle().getPrefs().isLocalDatabaseNotInitialized()) {
                        LocalDataInitializer.getInstance().initializeLocalDatabase();
                    }
                    //todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
                    getPreferencesInfo();
                    // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - pobranie
                    startRisingChain();
                } else {
                    Toast.makeText(
                        getContext(),
                        // todo
                            "coś nie bangla... Brak użytkowników",
                        Toast.LENGTH_SHORT).show();
                }
            }
        };
        findRemoteUserResult.observeForever(findRemoteUserResultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().findUserByLogin(userViewModel.getUser().getLogin(), findRemoteUserResult );
    }

    private void getPreferencesInfo() { // todo czy getSettingsInfo?
        //todo
        // język
        // użytkownik -> sklep amcierzysty, dział ew sektor
        // sprawdzenie co słychać w zdalnej bazie danych -> poniżej jest getNewAnalysisInfo()
        // ew info do użytkownika
        // zob. klasę .model.logic.AnalysisDataUpdater
        // zob. OneNote Kodowanie / Po zalogowaniu / ??Aktualizacja AnalyzesListFragment

    }

    private void startRisingChain() {
        // TODO !!! tutaj i wszędzie gdzie jest oczkiwanie na dane trzeba zrobić ograniczenie czasowe na odpowiedź...
        getNewAnalysisInfo();
    }

    private void getNewAnalysisInfo() {
        // todo to jest sztuka... bo może się kręcić w nieskończoność...
        MutableLiveData<Boolean> serverRepliedResult = new MutableLiveData<>();
        Observer<Boolean> resultObserver = new Observer<Boolean>() {
            @Override
            public void onChanged( Boolean isServerReplied ) {
                Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_analyzesListFragment);
            }
        };
        serverRepliedResult.observeForever( resultObserver );
        AnalysisDataUpdater analysisDataUpdater = getInstance();
        analysisDataUpdater.checkNewAnalysisToDownload( serverRepliedResult );
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
