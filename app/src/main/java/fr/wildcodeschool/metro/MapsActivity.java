package fr.wildcodeschool.metro;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Modell.extractStation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude = 0.0;
    double longitude = 0.0;
    static ArrayList<Station> stations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();


        stations = extractStation(MapsActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> locateStations = new ArrayList<>();


        for(int index = 0; index < stations.size() ; index++){
            LatLng newStation = new LatLng(stations.get(index).getStationLatitude(), stations.get(index).getStationLongitude());
            locateStations.add(newStation);
        }


        for(int index = 0; index < locateStations.size(); index++){
            mMap.addMarker(new MarkerOptions().position(locateStations.get(index)).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locateStations.get(index)));
        }
    }
}

