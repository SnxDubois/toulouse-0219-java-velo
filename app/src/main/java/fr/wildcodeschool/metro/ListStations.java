package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;

public class ListStations extends AppCompatActivity {
    public static final String SETTINGS_RETURN = "SETTINGS_RETURN";
    public static Settings mSettings;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    private ListView listView;
    private StationAdapter stationAdapter;
    private Switch switchButton;
    private Singleton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stations);
        getSettings();
        navigationDrawer();
        extractStationList();
        sendIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSettings() {
        settings = Singleton.getInstance();
        mSettings = settings.getSettings();
    }

    private void navigationDrawer() {
        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);
        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void extractStationList() {
        extractStation(ListStations.this, mSettings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                listView = findViewById(R.id.listView);
                stationAdapter = new StationAdapter(ListStations.this, stations);
                listView.setAdapter(stationAdapter);

            }
        });
    }

    private void sendIntent(){
        switchButton = findViewById(R.id.switch1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goMapsActivity = new Intent(ListStations.this, MapsActivity.class);
                startActivity(goMapsActivity);
            }
        });

    }
}
