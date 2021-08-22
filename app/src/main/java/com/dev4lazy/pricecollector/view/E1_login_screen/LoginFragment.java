package com.dev4lazy.pricecollector.view.E1_login_screen;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AuthSupport.LoginCallback {

    // Ten ViewModel jest używany
    private UserViewModel userViewModel;
    private EditText userLoginEditText;
    private EditText userPasswordEditText;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHandle.getHandle().getAuthSupport().setLoginCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        setOnBackPressedCalback();
        if (BuildConfig.DEBUG) {
            view.findViewById(R.id.login_fragment_layout).setOnClickListener((View v) -> {
                Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_testActionsFragment2);
            });
        }
        view.findViewById(R.id.login_button).setOnClickListener((View v) -> {
            logIn(/* todo test viemodel view */);
        });
        viewSetup(view);
        viewSubscribtion();
        return view;
    }

    // todo test viewmodel
    void viewSetup(View view) {
        userLoginEditText = view.findViewById(R.id.userlogin_edit_text);
        userPasswordEditText = view.findViewById(R.id.password_edit_text);
    }

    private void viewSubscribtion() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userLoginEditTextSubscription();
        userPasswordEditTextSubscription();
    }

    private void userLoginEditTextSubscription() {
        userLoginEditText.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged (CharSequence s,int start, int count, int after){
             }

             @Override
             public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                 userViewModel.getUser().setLogin(charSequence.toString());
             }

             @Override
             public void afterTextChanged (Editable s){
             }
         });
    }

    private void userPasswordEditTextSubscription() {
        userPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                userViewModel.getUser().setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        });
    }
    // todo end test viewmodel

    void logIn( /* todo test viemodel View view*/ ) {
        // TODO XXX pobranie danych usera (e-mail) z systemu
        // TODO TEST
        // testAccounts();
        // TODO END TEST
        // todo test viewmodel
        // EditText userLoginEditText = view.findViewById( R.id.userlogin_edit_text);
        // EditText userPasswordEditText = view.findViewById(R.id.password_edit_text);
        // todo end test viewmodel
        // todo zrób tu test jak login i hasło przeżywają bez viewmodelu i z viewmodelem
        // todo test viewmodel
        /* User user = new User();
        user.setLogin( userLoginEditText.getText().toString() );
        user.setLogin("nowak_j");
        userViewModel.setUser( user ); */
        // todo end test viewmodel
        AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
        authSupport.addCredential("USER_ID", userViewModel.getUser().getLogin() );
        authSupport.addCredential("USER_PASSWORD", userViewModel.getUser().getPassword() );
        /**/ userViewModel.getUser().setLogin( "nowak_j");
        /**/ userViewModel.getUser().setPassword( "nowak" );
        /**/authSupport.addCredential("USER_ID", "nowak_j" );
        /**/authSupport.addCredential("USER_PASSWORD", "nowak");

        authSupport.signIn();
    }

    /*/ TODO TEST
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
     TODO END TEST
    /*/
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
                    userViewModel.clear();
                    // todo jesli pierwsze uruchomienie, to incjalizacja danych w bazie lokalnej
                    if (AppHandle.getHandle().getSettings().isLocalDatabaseNotInitialized()) {
                        LocalDataInitializer.getInstance().initializeLocalDatabase();
                    }
                    //todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
                    getSettingsInfo();
                    // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - pobranie
                    startRisingChain();
                } else {
                    userPasswordEditText.setText("");
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

    private void getSettingsInfo() { // todo czy getSettingsInfo?
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
        // TODO XXX nie rozróżnia przyczyny niepowodzenia
        //  jest tylko, że serwer niedostepny, a powinno jeszcze, ze nieprawidłowe dane logowania...
        //  Może parametrem powiniwn byc komunikat,a w bardziej zaawanoswanej wersji obiekt,
        //  który udosuepnia informację?
        userPasswordEditText.setText("");
            // todo kumnuikat jakiś :-)
        Toast.makeText(
            getContext(),
            R.string.login_server_unavailable,
            Toast.LENGTH_SHORT).show();
    }

    // TODO XXX LoginFragment nie obsługuje klawisza back (nie da się wyjść z aplikacji)
    private void setOnBackPressedCalback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                new MaterialAlertDialogBuilder(getContext())/*, R.style.AlertDialogStyle) */
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_yes), new LogOffListener() )
                        .setNegativeButton(getActivity().getString(R.string.caption_no),null)
                        .show();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private class LogOffListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finishApp();
        }
    }

    private void finishApp() {
        // TODO promotor: czy to można bardziej elegancko zrobić?
        AppHandle.getHandle().shutdown();
        getActivity().finishAndRemoveTask();
        System.exit(0);
    }
}
