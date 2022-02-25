package com.dev4lazy.pricecollector;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity { // OK

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_drawer);
        toolbarSetup();
        drawerSetup();
        AppHandle.getHandle().getSettings().setPrefs( getPreferences(Context.MODE_PRIVATE) );
    }

    private void toolbarSetup() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void drawerSetup() {
        drawerLayout = findViewById(R.id.main_activity_with_drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        // Hamburger icon on
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // https://developer.android.com/reference/androidx/appcompat/app/ActionBar#setDisplayHomeAsUpEnabled(boolean)
        // true - jeśli klawisz back ma spowodować powrót o jeden poziom?
        // false - jeśli klawisz back ma spowodować powrót do home?
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

    @Override
    protected void onStart() {
        super.onStart();
        AppHandle appHandle = AppHandle.getHandle();
        appHandle.getBatteryStateMonitor().checkBatteryLevelPercentage( appHandle );
        appHandle.getBatteryStateMonitor().startMonitoring( appHandle );
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppHandle appHandle = AppHandle.getHandle();
        appHandle.getNetworkAvailabilityMonitor().startMonitoring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppHandle appHandle = AppHandle.getHandle();
        appHandle.getNetworkAvailabilityMonitor().stopMonitoring();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppHandle appHandle = AppHandle.getHandle();
        appHandle.getBatteryStateMonitor().stopMonitoring( appHandle );
    }

}
