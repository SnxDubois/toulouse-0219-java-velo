package fr.wildcodeschool.metro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;
import static fr.wildcodeschool.metro.ListStations.SETTINGS_RETURN;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String SETTINGS = "Settings";
    private static final int REQUEST_LOCATION = 2000;
    public static ArrayList<Marker> stationMarkers = new ArrayList<Marker>();
    public static boolean init = false;
    public static boolean changeActivity = false;
    public static boolean theme = false;
    private static GoogleMap mMap;
    private static boolean dropOff = true;
    private static int zoom = 14;
    private static Settings settings;
    private static Location lastKnownlocation;
    private Intent receiveListActivity;
    private FloatingActionButton button;
    private Switch switchButton;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private Switch switchDarkMap;
    private AlertDialog.Builder builder;
    private LatLng newStation;
    private Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (!init) {
            checkPermission();
        } else {
            currentLocation();
        }
        receiveListActivity = getIntent();
        settings = receiveListActivity.getParcelableExtra(SETTINGS_RETURN);
        switchButton();
        floatingButton();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void floatingButton() {
        button = findViewById(R.id.buttonSettings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySettings();
            }
        });
    }

    private void switchButton() {
        switchButton = findViewById(R.id.switch1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeActivity = true;
                Intent goListStationAcitvity = new Intent(MapsActivity.this, ListStations.class);
                goListStationAcitvity.putExtra(SETTINGS, (Parcelable) settings);
                startActivity(goListStationAcitvity);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void currentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(MapsActivity.LOCATION_SERVICE);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lastKnownlocation = location;
                    if (!changeActivity) {
                        settings = new Settings(zoom, dropOff, lastKnownlocation, init, changeActivity, theme);
                    }
                    removeMarkers();
                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    createStationMarker(settings);
                }
            }
        });

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lastKnownlocation = location;
                if (!changeActivity) {
                    settings = new Settings(zoom, dropOff, lastKnownlocation, init, changeActivity, theme);
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        checkPermission();
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!changeActivity) {
            settings = new Settings(zoom, dropOff, lastKnownlocation, init, changeActivity, theme);
        }
        if (settings.isTheme()) {
            displayDarkTheme(googleMap);
        } else {
            displayDefaultTheme(googleMap);
        }
        switchTheme(googleMap);
    }

    private void switchTheme(final GoogleMap googleMap) {
        switchDarkMap = findViewById(R.id.switchMap);
        switchDarkMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dropOff = isChecked ? true : false;
                if (dropOff) {
                    displayDarkTheme(googleMap);
                } else {
                    displayDefaultTheme(googleMap);
                }
            }
        });

    }

    private void displayDefaultTheme(final GoogleMap googleMap) {
        settings.setTheme(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }


    }

    private void displayDarkTheme(final GoogleMap googleMap) {
        settings.setTheme(false);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.mapstyledark));
            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
    }

    private void displaySettings() {
        builder = new AlertDialog.Builder(this);
        String[] perimeter = {getString(R.string.perimeter1), getString(R.string.perimeter2), getString(R.string.perimeter3), getString(R.string.perimeter4), getString(R.string.perimeter5)};
        final boolean[] checkedItems = {false, false, false, false, false};
        View switchButtonView = LayoutInflater.from(this).inflate(R.layout.activity_toggle, null);
        Switch switchButton = switchButtonView.findViewById(R.id.switch2);
        builder.setTitle(R.string.settings);
        builder.setMultiChoiceItems(perimeter, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                zoom = checkedItems[0] ? 14 : checkedItems[1] ? 15 : checkedItems[2] ? 16 : checkedItems[3] ? 17 : 18;
            }
        });
        builder.setView(switchButtonView);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dropOff = isChecked ? true : false;
                if (dropOff) {
                    Toast.makeText(MapsActivity.this, getString(R.string.takeBike), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapsActivity.this, getString(R.string.dropBike), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settings.setDropOff(dropOff);
                settings.setZoom(zoom);
                currentLocation();
                Toast.makeText(MapsActivity.this, getString(R.string.appliedSettings), Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createStationMarker(Settings settings) {
        extractStation(MapsActivity.this, settings, new Helper.BikeStationListener() {
            @Override
            public void onResult(ArrayList<Station> stations) {
                for (int i = 0; i < stations.size(); i++) {
                    newStation = new LatLng(stations.get(i).getLatitude(), stations.get(i).getLongitude());
                    marker = mMap.addMarker((new MarkerOptions().position(newStation).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)).title(stations.get(i).getAddress()).snippet(stations.get(i).getName())));
                    stationMarkers.add(marker);
                    marker.showInfoWindow();
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                }
            }
        });
    }

    private void removeMarkers() {
        for (Marker marker : stationMarkers) {
            marker.remove();
        }
        stationMarkers.clear();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
        } else {
            currentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentLocation();
                } else {
                }
                return;
            }
        }
    }
}

