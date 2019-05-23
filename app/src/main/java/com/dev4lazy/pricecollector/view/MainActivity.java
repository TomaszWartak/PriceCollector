package com.dev4lazy.pricecollector.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.view.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
