package fr.wildcodeschool.metro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
    }
}
