package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.CustomTokenFirebaseAuthenticationServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogingFragment extends Fragment {

    // todo ViewModel...

    private CustomTokenFirebaseAuthenticationServices authenticationServices = null;

    public LogingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationServices = CustomTokenFirebaseAuthenticationServices.getInstance();
        authenticationServices.setActivity(getActivity());
        authenticationServices.bindToMockAuthService(); // todo czy na pewno w onCreate
        CustomTokenFirebaseAuthenticationServices.MockAuthServiceBroadcastReceiver ockAuthServiceBroadcastReceiver =
                new
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
                // todo test
                /*
                final ActivityManager activityManager = (android.app.ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
                String serviceName = null;
                for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                    serviceName = runningServiceInfo.service.getClassName();
                }
                */
                /**/
                 //FirebaseUser firebaseUser = authenticationServices.getCurrentFirebaseUser();
                 authenticationServices.signInCustomAuthServer("nowak_j", "qwerty");
                 authenticationServices.signInFirebase();
                 //firebaseUser = authenticationServices.getCurrentFirebaseUser();
                 authenticationServices.signOut();
                 /**/
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // todo czy na pewno w onCreate?
        authenticationServices.unbindFromMockAuthService();
    }
}
