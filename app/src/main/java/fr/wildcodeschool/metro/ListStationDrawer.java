package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;

public class ListStationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SETTINGS_RETURN = "SETTINGS_RETURN";
    public static Settings mSettings;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    private ListView listView;
    private StationAdapter stationAdapter;
    private Switch switchButton;
    private Singleton settings;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_station_drawer);
        getSettings();
        navigationDrawer();
        extractStationList();
        sendIntent();
    }

    private void getSettings() {
        settings = Singleton.getInstance();
        mSettings = settings.getSettings();
    }
    private void importFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.drawer_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void extractStationList() {
        extractStation(ListStationDrawer.this, mSettings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                listView = findViewById(R.id.listView);
                stationAdapter = new StationAdapter(ListStationDrawer.this, stations);
                listView.setAdapter(stationAdapter);

            }
        });
    }

    private void sendIntent(){
        switchButton = findViewById(R.id.switch1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goMapsActivity = new Intent(ListStationDrawer.this, MapsActivity.class);
                startActivity(goMapsActivity);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_station_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            importFragment();
        } else if (id == R.id.nav_account) {
            fragment = new AccountFragment();
            importFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            importFragment();
        } else if (id == R.id.nav_favorite) {
            fragment = new FavoriteFragment();
            importFragment();
        } else if (id == R.id.nav_share) {
            fragment = new ShareFragment();
            importFragment();
        } else if (id == R.id.nav_send) {
            fragment = new SendFragment();
            importFragment();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
