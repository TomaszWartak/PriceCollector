package com.dev4lazy.pricecollector.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import android.content.Context;
import android.os.Bundle;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    //private AppBarConfiguration appBarConfiguration;

    public Toolbar toolbar;

    public DrawerLayout drawerLayout;

    public NavController navController;

    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupNavigation();

        // Inicalizacja obiektu preferencji
        AppHandle.getHandle().getPrefs().setPrefs(getPreferences(Context.MODE_PRIVATE));
    }

    private void setupNavigation(){
        /*toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        drawerLayout = findViewById(R.id.main_activity_layout);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView = findViewById(R.id.navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(findViewById(R.id.drawer_layout))
                .build();

         */
    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {


            case R.id.first:
                navController.navigate(R.id.firstFragment);
                break;

            case R.id.second:
                navController.navigate(R.id.secondFragment);
                break;

            case R.id.third:
                navController.navigate(R.id.thirdFragment);
                break;



        }
        return true;

    }

     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
    }

}
