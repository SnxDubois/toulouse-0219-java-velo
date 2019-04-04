package fr.wildcodeschool.metro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    static ArrayList<Station> stations = new ArrayList<>();
    LocationManager mLocationManager = null;
    static LatLng userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();


        checkPermission();
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
        checkPermission();
        googleMap.setMyLocationEnabled(true);
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // l'autorisation n'est pas acceptée
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
            } else {
                // l'autorisation n'a jamais été réclamée, on la demande à l'utilisateur
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
        }
    }

}

