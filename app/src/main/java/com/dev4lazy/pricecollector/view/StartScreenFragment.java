package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartScreenFragment extends Fragment {


    public StartScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.start_screen_fragment, container, false);
        // todo lambda
        view.findViewById(R.id.start_screen_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_startScreenFragment_to_setUpPreferncesFragment);
            }
        });
        return view;
    }

}
