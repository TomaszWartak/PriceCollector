package com.dev4lazy.pricecollector.view.E1_login_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader.getInstance;

public class LoginFragment
        extends Fragment
        implements AuthSupport.LoginCallback {

    private UserViewModel userViewModel;
    private EditText userLoginEditText;
    private EditText userPasswordEditText;
    private ProgressBar pleaseWaitProgressBar;

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
        setOnBackPressedCallback();
        if (BuildConfig.DEBUG) {
            view.findViewById(R.id.login_fragment_layout).setOnClickListener((View v) -> {
                Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_testActionsFragment2);
            });
        }
        viewSetup( view );
        return view;
    }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    new LogoutQuestionDialog( getContext(), getActivity() ).get();
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        void viewSetup(View view) {
            userLoginEditText = view.findViewById(R.id.userlogin_edit_text);
            userPasswordEditText = view.findViewById(R.id.password_edit_text);
            Button loginButton = view.findViewById(R.id.login_button);
            if (BuildConfig.DEBUG) {
                loginButton.setFocusableInTouchMode(true);
                loginButton.requestFocus();
            }
            loginButton.setOnClickListener((View v) -> {
                logIn();
            });

        }

        void logIn( ) {
            showPleaseWaitProgressBar();
            setUserViewModelData( new User() );
            runAuthSupport();
        }

    private void showPleaseWaitProgressBar() {
        pleaseWaitProgressBar = this.getView().findViewById(R.id.please_wait_progress_bar);
        pleaseWaitProgressBar.setVisibility(View.VISIBLE);
    }

    private void setUserViewModelData(User user) {
        user.setLogin( userLoginEditText.getText().toString() );
        /* TODO TEST */ user.setLogin("nowak_j");
        user.setPassword( userPasswordEditText.getText().toString() );
        /* TODO TEST */ user.setPassword("nowak");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUser( user );
    }

    private void runAuthSupport() {
        AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
        authSupport.addCredential("USER_ID", userViewModel.getUser().getLogin() );
        authSupport.addCredential("USER_PASSWORD", userViewModel.getUser().getPassword() );
        /* TODO TEST */ userViewModel.getUser().setLogin( "nowak_j");
        /* TODO TEST */ userViewModel.getUser().setPassword( "nowak" );
        /* TODO TEST */ authSupport.addCredential("USER_ID", "nowak_j" );
        /* TODO TEST */ authSupport.addCredential("USER_PASSWORD", "nowak");
        authSupport.signIn();
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu callbacków logowania AuthSupport.LoginCallback
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
                    // sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
                    getSettingsInfo();
                    // sprawdzenie czy na serwerze zdalnym jest nowa analiza - pobranie
                    startRisingChain();
                    // Przejście do AnalyzesListFragment
                    Navigation.findNavController(getView()).navigate(R.id.action_logingFragment_to_analyzesListFragment);
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
        AppHandle.getHandle().getRepository().getRemoteDataRepository().findRemoteUserByLogin(userViewModel.getUser().getLogin(), findRemoteUserResult );
    }

        private void getSettingsInfo() { // todo czy getSettingsInfo?
            //todo
            // język
            // użytkownik -> sklep amcierzysty, dział ew sektor
            // sprawdzenie co słychać w zdalnej bazie danych -> poniżej jest getNewAnalysisInfo()
            // ew info do użytkownika
            // zob. klasę .model.logic.AnalysisDataDownloader
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
                MutableLiveData<Boolean> serverRepliedResult = new MutableLiveData<>();
                Observer<Boolean> resultObserver = new Observer<Boolean>() {
                    @Override
                    public void onChanged( Boolean isServerReplied ) {
                        pleaseWaitProgressBar.setVisibility(View.GONE);
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
                serverRepliedResult.observeForever( resultObserver );
                AnalysisDataDownloader analysisDataDownloader = getInstance();
                analysisDataDownloader.checkNewAnalysisToDownload( serverRepliedResult );
            }

    @Override
    public void callIfUnsuccessful( String reasonMessage ) {
        // TODO XXX nie rozróżnia przyczyny niepowodzenia
        //  jest tylko, że serwer niedostepny, a powinno jeszcze, ze nieprawidłowe dane logowania...
        //  Może parametrem powiniwn byc komunikat,a w bardziej zaawanoswanej wersji obiekt,
        //  który udosuepnia informację?
        pleaseWaitProgressBar.setVisibility(View.GONE);
        userPasswordEditText.setText("");
            // todo kumnuikat jakiś :-)
        Toast.makeText(
            getContext(),
            reasonMessage,
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
        setupNavigationViewMenu();
    }

        private void setupNavigationViewMenu() {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.login_screen_menu);
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.login_screen_logout_menu_item:
                            new LogoutQuestionDialog( getContext(), getActivity() ).get();
                            // TODO XXX getLogoutQuestionDialog();
                            break;
                    }
                    return false;
                }
            });
        }

    private void setupToolbar() {
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.hide();
    }

}
