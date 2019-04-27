package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;

public class ListStationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SETTINGS_RETURN = "SETTINGS_RETURN";
    public static Settings mSettings;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    private ListView listView;
    private Switch switchButton;
    private Singleton settings;
    private Fragment fragment;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_station_drawer);
        switchActivity();
        getSettings();
        navigationDrawer();
        extractStationList();
    }

    private void switchActivity(){
        BottomNavigationView navigation =findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0,true, true);
        navigation.setSelectedItemId(R.id.navigation_list);
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void extractStationList() {
        extractStation(ListStationDrawer.this, mSettings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                //listView = findViewById(R.id.listView);
                //stationAdapter = new StationAdapter(ListStationDrawer.this, stations);
                //listView.setAdapter(stationAdapter);
                RecyclerView recycleListStations = findViewById(R.id.list_recycle_station);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListStationDrawer.this, LinearLayoutManager.VERTICAL, false);
                recycleListStations.setLayoutManager(layoutManager);
                StationsRecyclerAdapter adapter = new StationsRecyclerAdapter(stations);
                recycleListStations.setAdapter(adapter);

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent goMapsAcitvity = new Intent(ListStationDrawer.this, MapsActivity.class);
                    startActivity(goMapsAcitvity);
                    return true;
                case R.id.navigation_list:
                    return true;
            }
            return false;
        }
    };


}
