package fr.wildcodeschool.metro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
    private static final int REQUEST_LOCATION = 2000;
    private static GoogleMap mMap;
    private static boolean dropOff = true;
    private static int zoom = 15;
    public static final String TAKE_BIKE = "Take a bike";
    public static final String DROP_BIKE = "Drop off a bike";
    public static final String CANCEL = "Cancel";
    public static final String OK = "OK";
    public static final String SETTING_APPLIED = "OK";
    public static final String SETTING = "Settings";
    public static final String PERIMETER1 = "1Km";
    public static final String PERIMETER2 = "700m";
    public static final String PERIMETER3 = "500m";
    public static final String PERIMETER4 = "200m";
    public static final String PERIMETER5 = "100m";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        switchButton();
        checkPermission();
        floatingButton();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void floatingButton() {
        FloatingActionButton button = findViewById(R.id.buttonSettings);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySettings();
            }
        });
    }

    private void switchButton() {
        Switch switchButton = findViewById(R.id.switch1);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent goListStationAcitvity = new Intent(MapsActivity.this, ListStations.class);
                startActivity(goListStationAcitvity);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        LocationManager mLocationManager = null;

        LocationListener locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            public void onLocationChanged(Location location) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setMyLocationEnabled(true);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        createStationMarker();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkPermission();
    }

    private void displaySettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] perimeter = {PERIMETER1, PERIMETER2, PERIMETER3, PERIMETER4, PERIMETER5};
        final boolean[] checkedItems = {false, false, false, true, false};
        View switchButtonView = LayoutInflater.from(this).inflate(R.layout.activity_toggle, null);
        Switch switchButton = switchButtonView.findViewById(R.id.switch2);

        builder.setTitle(SETTING);
        builder.setMultiChoiceItems(perimeter, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                zoom = checkedItems[0] ? 16 : checkedItems[1] ? 17 : checkedItems[2] ? 18 : checkedItems[3] ? 19 : 20;

            }
        });
        builder.setView(switchButtonView);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dropOff = isChecked ? true : false;
                if (dropOff) {
                    Toast.makeText(MapsActivity.this, TAKE_BIKE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapsActivity.this, DROP_BIKE, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL, null);
        builder.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
                Toast.makeText(MapsActivity.this, SETTING_APPLIED, Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createStationMarker() {
        extractStation(MapsActivity.this, dropOff, zoom, new Helper.BikeStationListener() {
            @Override
            public void onResult(ArrayList<Station> stations) {
                for (Station station : stations) {
                    LatLng newStation = new LatLng(station.getLatitude(), station.getLongitude());
                    Marker marker = mMap.addMarker((new MarkerOptions().position(newStation).title(station.getAddress())));
                }
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}

