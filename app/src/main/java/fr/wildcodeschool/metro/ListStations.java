package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;

public class ListStations extends AppCompatActivity {
    private static boolean dropOff = true;
    private static int zoom = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stations);

        extractStation(ListStations.this, dropOff, zoom, new Helper.BikeStationListener() {
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
                        startActivity(goMapsActivity);
                    }
                });
            }
        });
    }
}
