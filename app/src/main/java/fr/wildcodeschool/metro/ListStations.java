package fr.wildcodeschool.metro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Modell.extractStation;

public class ListStations extends AppCompatActivity {

    ArrayList<Station> stations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stations);


        stations = extractStation(ListStations.this);
        ListView listView = findViewById(R.id.listView);
        StationAdapter stationAdapter = new StationAdapter(ListStations.this,stations);
        listView.setAdapter(stationAdapter);
        Switch switchButton = findViewById(R.id.switch1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goMapsActivity = new Intent(ListStations.this,MapsActivity.class);
                startActivity(goMapsActivity);
            }
        });
    }
}



