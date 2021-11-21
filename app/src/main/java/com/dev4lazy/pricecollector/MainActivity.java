package com.dev4lazy.pricecollector;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_drawer);
        toolbarSetup();
        drawerSetup();
        // Inicalizacja obiektu preferencji
        AppHandle.getHandle().getSettings().setPrefs( getPreferences(Context.MODE_PRIVATE) );
    }

    private void toolbarSetup() {
        toolbar = findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        // TODO XXX toolbar.setVisibility( GONE );
        // TODO XXX toolbar.setTitle(R.string.app_name);
    }

    private void drawerSetup() {
        drawerLayout = findViewById(R.id.main_activity_with_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            // TODO XXX, czy te dwie metody sa potrzebne???
            /*public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // TODO XXX ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PriceCollector");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // TODO XXX ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            }
             */
        };
        drawerLayout.addDrawerListener(drawerToggle);
        // Hamburger icon on
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // TODO XXX drawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );

        // true - chyba jeśli klawisz back ma o jeden poziom robić
        // false - chyba jeśli klawisz back ma wracać do home
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout!=null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            }
        }
        super.onBackPressed();
    }

}
