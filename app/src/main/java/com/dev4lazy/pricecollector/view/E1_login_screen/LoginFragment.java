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
import com.dev4lazy.pricecollector.model.logic.AnalysisBasicDataDownloader;
import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisBasicDataDownloader.getInstance;

public class LoginFragment
        extends Fragment
        implements AuthSupport.LoginCallback {

    private AuthSupport authSupport = AppHandle.getHandle().getAuthSupport();
    private UserViewModel userViewModel;
    private EditText userLoginEditText;
    private EditText userPasswordEditText;
    private ProgressBar pleaseWaitProgressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
            /* TODO TEST - na potrzeby testu zakomentuj
            if (isLoginEmpty()) {
                Toast.makeText(
                        getContext(),
                        AppHandle.getHandle().getString( R.string.enter_user_ID ),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (isPasswordEmpty()){
                Toast.makeText(
                        getContext(),
                        AppHandle.getHandle().getString( R.string.enter_password ),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            */
            showPleaseWaitProgressBar();
            setUserViewModelData();
            runAuthSupport();
        }

    private boolean isLoginEmpty() {
        String login = userLoginEditText.getText().toString();
        if (login==null) {
            return true;
        }
        return login.equals("");
    }

    private boolean isPasswordEmpty() {
        String password = userPasswordEditText.getText().toString();
        if (password ==null) {
            return true;
        }
        return password.equals("");
    }

    private void showPleaseWaitProgressBar() {
        pleaseWaitProgressBar = getView().findViewById(R.id.please_wait_progress_bar);
        pleaseWaitProgressBar.setVisibility(View.VISIBLE);
    }

    private void setUserViewModelData() {
        User user = new User();
        user.setLogin( userLoginEditText.getText().toString().toLowerCase() );
        user.setPassword( userPasswordEditText.getText().toString() );
        /* TODO TEST */
        user.setLogin("nowak_j");
        user.setPassword("nowak");
        /* TODO END TEST */
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setUser( user );
    }

    private void runAuthSupport() {
        authSupport.addCredential("USER_ID", userViewModel.getUser().getLogin() );
        authSupport.addCredential("USER_PASSWORD", userViewModel.getUser().getPassword() );
        /* TODO TEST /
        userViewModel.getUser().setLogin( "nowak_j");
        userViewModel.getUser().setPassword( "nowak" );
        authSupport.addCredential("USER_ID", "nowak_j" );
        authSupport.addCredential("USER_PASSWORD", "nowak");
        / TODO END TEST */
        authSupport.setLoginCallback( this ); // zob. metody callIfSuccessful() i callIfUnsuccessful()
        authSupport.signIn();
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu callbacków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania

    @Override
    public void callIfSuccessful() {
        // Pobranie danych Użytkownika, który się zalogował, z bazy zdalnej (np.ABC)
        MutableLiveData<List<RemoteUser>> findRemoteUserResult = new MutableLiveData<>();
        Observer<List<RemoteUser>>findRemoteUserResultObserver = new Observer<List<RemoteUser>>() {
            @Override
            public void onChanged(List<RemoteUser> remoteUsers ) {
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
                    pleaseWaitProgressBar.setVisibility(View.GONE);
                    userPasswordEditText.setText("");
                    Toast.makeText(
                        getContext(),
                            AppHandle.getHandle().getString(
                                    R.string.user_not_found_in_the_remote_database
                            )+
                            " "+
                            userViewModel.getUser().getLogin(),
                        Toast.LENGTH_SHORT).show();
                }
            }
        };
        findRemoteUserResult.observeForever(findRemoteUserResultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().findRemoteUserByLogin( userViewModel.getUser().getLogin(), findRemoteUserResult );
    }

        private void getSettingsInfo() {
            // TODO
            //  język
            //  użytkownik -> sklep macierzysty, dział ew sektor
        }

        private void startRisingChain() {
            // TODO !!! tutaj i wszędzie gdzie jest oczkiwanie na dane trzeba zrobić ograniczenie czasowe na odpowiedź...
            getNewAnalysisInfo();
        }

            // TODO XXX Jeśli serwer nir odpowie, to nie ma przejścia do listy Badań
            //  i nie znika pleaseWaitSpinner.
            //  A moze przejście do listy badań zrobić bezwarunkowo, a w OnChanged tylko zniknięcie spinnera?
            //  Zrobiłem - działa. Przejście do Listy Badań zamyka (ukrywa?) spinner.
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
            //  której implementacją jest "TimeCondition".
            //  Może odliczanie, które powiadomi obserwvera (tego poniżej i ustawi isServerReplied na false?
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
                AnalysisBasicDataDownloader analysisBasicDataDownloader = getInstance();
                analysisBasicDataDownloader.checkNewAnalysisToDownload( serverRepliedResult );
            }

    @Override
    public void callIfUnsuccessful( String failureReasonMessage) {
        pleaseWaitProgressBar.setVisibility(View.GONE);
        userPasswordEditText.setText("");
        Toast.makeText(
            getContext(),
                failureReasonMessage,
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
