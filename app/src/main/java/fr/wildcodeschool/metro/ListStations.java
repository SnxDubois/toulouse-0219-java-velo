package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;
import static fr.wildcodeschool.metro.MapsActivity.SETTINGS;

public class ListStations extends AppCompatActivity {
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    public static final String SETTINGS_RETURN = "SETTINGS_RETURN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stations);
        Intent receiveMainActivity = getIntent();
        final Settings settings = receiveMainActivity.getParcelableExtra(SETTINGS);
        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);
        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extractStation(ListStations.this, settings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                ListView listView = findViewById(R.id.listView);
                StationAdapter stationAdapter = new StationAdapter(ListStations.this, stations);
                listView.setAdapter(stationAdapter);
                Switch switchButton = findViewById(R.id.switch1);
                switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Intent goMapsActivity = new Intent(ListStations.this, MapsActivity.class);
                        goMapsActivity.putExtra(SETTINGS_RETURN, (Parcelable) settings);
                        startActivity(goMapsActivity);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
