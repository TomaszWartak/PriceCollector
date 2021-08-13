package com.dev4lazy.pricecollector.view.E1_login_screen;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.view.E5_article_screen.AnalysisArticlesPagerFragment;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthSupport.LoginCallback {

    private UserViewModel userViewModel;

    /* todo usuń
    public LoginFragment() {
        // Required empty public constructor
    }
    */

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHandle.getHandle().getAuthSupport().setLoginCallbackService(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        if (BuildConfig.DEBUG) {
            view.findViewById(R.id.login_fragment_layout).setOnClickListener((View v) -> {
                Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_testActionsFragment2);
            });
        }
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            logIn(view);
        });
        return view;
    }

    void logIn( View view ) {
        // TODO XXX pobranie danych usera (e-mail) z systemu
        // TODO TEST
        testAccounts();

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

    // TODO TEST
    private void testAccounts() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getContext()).getAccounts();
        int len = accounts.length;
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
            }
        }
    }
    // TODO END TEST

// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania

    @Override
    public void callIfSuccessful() {
        // todo RemoteUser remoteUser = new RemoteUser();
        // todo remoteUser.setLogin(userViewModel.getUser().getLogin());

        // Sprawdzenie, czy baza zdalna (ABC) jest utworzona i dostepna
        // TODO: sprawdzenie powoduje wywołanie RemoteDatabase.getInstance(), które tworzy bazę
        /*/  dlatego takie sprawdzenie nie ma sensu...
        if (!AppHandle.getHandle().getRemoteDatabase().getDatabaseCreated().getValue()) {
            Toast.makeText(
                getContext(),
                // todo PObranie napisu z zasobów res/... "Zdalna baza danych nie jest zainicjowana. Nie można pobrać dancyh użytkownika."

                "Zdalna baza danych nie jest zainicjowana. Nie można pobrać dancyh użytkownika.",
                Toast.LENGTH_SHORT).show();
                return;
        } */
        // Pobranie danych Użytkownika, który się zalogował, z bazy zdalnej (np.ABC)
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
                        // todo PObranie napisu z zasobów res/... "W zdalnej bazie danych nie znaleziono danych użytkownika"+userViewModel.getUser().getLogin()
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

    // TODO XXX Jeśli serwer nir odpowie, to nie ma przejścia do listy Badań
    //  i nie znika pleaseWaitSpinner.
    //  A moze przejście do listy badań zrobić bezwarunkowo, a w OnChanged tylko zniknięcie spinnera?
    //  Zrobiłem - działa. Przejście so Listy Badań zamyka (ukrywa?) spinner.
    //  UWAGA: spinner nie zniknie, jeśli nie będzie odpowiedzi z serwera...
    // TODO XXX jak zrobić, że w razie braku odpowiedzi jest komunikat?
    //  Może jakiś obiekt, który wysyła zapytanie i uruchamia odliczanie w wątku(?).
    //  Jeśli jest odpowiedź zanim odliczy - zwraca ją  do wywołującego, a wywołujący
    //  usuwa observera.
    //  Po odliczeniu, zwraca informację o niepowodzeniu, a wywołujący usuwa observera
    //  i wyświetla komunikat.
    //  Może rozszerzyć interface Observer o metodę z czasem? A może coś takiego jest?
    //  Zobacz zakomentowaną metodę poniżej onChanged()
    //  Zeby było ładnie, to zmiast ana sztywno, że  niepowodzenie jest tylko, gdy minie czas,
    //  to można by stworzyć klasę warunków, w których uznaje się, że jest niepowodzenie,
    //  której implementacją jest "TimeCondition"
    private void getNewAnalysisInfo() {
        ProgressBar pleaseWaitSpinner = this.getView().findViewById(R.id.please_wait_spinner);
        MutableLiveData<Boolean> serverRepliedResult = new MutableLiveData<>();
        Observer<Boolean> resultObserver = new Observer<Boolean>() {
            @Override
            public void onChanged( Boolean isServerReplied ) {
                pleaseWaitSpinner.setVisibility(View.GONE);
                /// Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_analyzesListFragment);
            }
            /* @Override
            public void notHappen() {
                pleaseWaitSpinner.setVisibility(View.GONE);
                Komunikat a la "Serwer nie odpowiedział"
                Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_analyzesListFragment);
            }
            */
        };
        pleaseWaitSpinner.setVisibility(View.VISIBLE);
        serverRepliedResult.observeForever( resultObserver );
        AnalysisDataUpdater analysisDataUpdater = getInstance();
        analysisDataUpdater.checkNewAnalysisToDownload( serverRepliedResult );
        Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_analyzesListFragment);
    }

    @Override
    public void callIfUnsuccessful() {
            // todo kumnuikat jakiś :-)
        Toast.makeText(
            getContext(),
            "coś nie bangla...",
            Toast.LENGTH_SHORT).show();
    }

    // TODO XXX LoginFragment nie obsługuje klawisza back (nie da się wyjść z aplikacji)
}
