package com.dev4lazy.pricecollector;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    // todo ? private AppBarConfiguration appBarConfiguration;

    // todo ?
    public Toolbar toolbar; // todo Toolbar

    public DrawerLayout drawerLayout; // todo DrawerLayout

    public NavController navController; // todo NavController

    public NavigationView navigationView; // todo NavigationView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_with_drawer);
        setupToolbar();
        setDrawerHidden();
        setupNavigation(); // todo XXX <-- ta metoda jest pusta...
        // Inicalizacja obiektu preferencji
        AppHandle.getHandle().getSettings().setPrefs( getPreferences(Context.MODE_PRIVATE) );
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        toolbar.setTitle("PriceCollector");
        // todo XXX toolbar.setDisplayHomeAsUpEnabled(true);
    }

    private void setDrawerHidden() {
        drawerLayout = findViewById(R.id.main_activity_with_drawer_layout);
        /* TODO XXX
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("PriceCollector");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("");
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        // Hamburger icon off
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();
        */

        drawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
        // navigationView = findViewById(R.id.navigation_view);
        // navigationView.setNavigationItemSelectedListener(getOnNavigationItemSelectedListener());

        // true - chyba jeśli klawisz back ma o jeden poziom robić
        // false - chyba jeśli klawisz back ma wracać do home
        // TODO XXX getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    /*
    private NavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // return true to display the item as the selected item
                drawerLayout.closeDrawers();
                Menu nav_Menu = navigationView.getMenu();
                switch (item.getItemId()) {
                    case R.id.nav_1_1:
                        nav_Menu.findItem(R.id.nav_1_1).setVisible(false);
                        break;
                    case R.id.nav_1_2:
                        nav_Menu.findItem(R.id.nav_1_2).setVisible(false);
                        break;
                    case R.id.nav_2_1:
                        nav_Menu.findItem(R.id.nav_2_1).setVisible(false);
                        break;
                    case R.id.nav_2_2:
                        nav_Menu.findItem(R.id.nav_2_2).setVisible(false);
                        break;
                    case R.id.login_screen_1:
                        break;
                    case R.id.login_screen_2:
                        break;
                }
                return false;
            }
        };
    }
    */

    private void setupNavigation(){
        /* TODO XXX
        toolbar = findViewById(R.id.toolbar);
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
    /*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*/

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
                // todo return true;
            }
        }
    }
    */

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }
    /**/

    /**/
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
    /**/
}
