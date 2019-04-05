package fr.wildcodeschool.metro;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import static fr.wildcodeschool.metro.Helper.extractStation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        FloatingActionButton button = findViewById(R.id.floatingActionButton2);
        Switch switchButton = findViewById(R.id.switch1);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goListStationAcitvity = new Intent(MapsActivity.this, ListStations.class);
                startActivity(goListStationAcitvity);
            }
        });
        checkPermission();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySettings();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        createStationMarker();
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        checkPermission();
        googleMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void displaySettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings");

        String[] distance = {"3km", "2km", "1km", "500m", "200m"};
        builder.setItems(distance, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void createStationMarker() {
        ArrayList<Station> stations = extractStation(MapsActivity.this);
        for (Station station : stations) {
            LatLng newStation = new LatLng(station.getLatitude(), station.getLongitude());
            Marker marker = mMap.addMarker((new MarkerOptions().position(newStation).title(station.getAddress())));
        }
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

