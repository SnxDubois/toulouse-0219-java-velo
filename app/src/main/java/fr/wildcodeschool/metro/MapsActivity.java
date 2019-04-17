package fr.wildcodeschool.metro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static fr.wildcodeschool.metro.Helper.extractStation;
import static fr.wildcodeschool.metro.ListStations.SETTINGS_RETURN;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String SETTINGS = "Settings";
    public static final int REQUEST_IMAGE_CAPTURE = 1234;
    public ArrayList<Marker> mStationMarkers = new ArrayList<>();
    public boolean mInit = false;
    public boolean mTheme = false;
    public GoogleMap mMap;
    public boolean mDropOff = true;
    public int mZoom = 14;
    public Settings mSettings;
    public Location mLastKnownLocation;
    public Uri mFileUri = null;
    private static final int REQUEST_LOCATION = 2000;
    private SeekBar seekbar;
    private int mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkPermission();
        Intent receiveListActivity = getIntent();
        mSettings = receiveListActivity.getParcelableExtra(SETTINGS_RETURN);
        if (mSettings == null) {
            mSettings = new Settings(mZoom, mDropOff, mLastKnownLocation, mInit, false, mTheme);
        }
        switchButton();
        floatingButton();
        takePicIssues();
        seekBar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void seekBar() {
        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mProgress > 0 && mProgress < 20) {
                    seekBar.setProgress(0);
                    mZoom = 14;
                } else if (mProgress > 20 && mProgress < 40) {
                    seekBar.setProgress(25);
                    mZoom = 15;
                } else if (mProgress > 40 && mProgress < 60) {
                    seekBar.setProgress(50);
                    mZoom = 16;
                }else if (mProgress > 60 && mProgress < 80) {
                    seekBar.setProgress(75);
                    mZoom = 17;
                } else if (mProgress > 80 && mProgress < 100) {
                    seekBar.setProgress(100);
                    mZoom = 18;
                }
                currentLocation();
            }
        });
    }

    private void takePicIssues() {
        ImageButton takePic = findViewById(R.id.ibTakePicOfIssue);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        // ouvrir l'application de prise de photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // lors de la validation de la photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // créer le fichier contenant la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // TODO : gérer l'erreur
            }

            if (photoFile != null) {
                // récupèrer le chemin de la photo
                mFileUri = FileProvider.getUriForFile(this,
                        "fr.wildcodeschool.metro.fileprovider",
                        photoFile);
                // déclenche l'appel de onActivityResult
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView ivRecupPic = findViewById(R.id.ivRecupPic);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ivRecupPic.setImageURI(mFileUri);

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        return image;
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
                goListStationAcitvity.putExtra(SETTINGS, (Parcelable) mSettings);
                startActivity(goListStationAcitvity);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void currentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(MapsActivity.LOCATION_SERVICE);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && mMap != null) {
                    mLastKnownLocation = location;
                    removeMarkers();
                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), mZoom));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mSettings = new Settings(mZoom, mDropOff, mLastKnownLocation, mInit, false, mTheme);
                    createStationMarker(mSettings);
                }
            }
        });

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mLastKnownLocation = location;
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), mZoom));
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
        switchTheme(googleMap);
        if (mSettings.isTheme()) {
            displayDarkTheme(googleMap);
        } else {
            displayDefaultTheme(googleMap);
        }
    }

    private void switchTheme(final GoogleMap googleMap) {
        Switch switchDarkMap = findViewById(R.id.switchMap);
        switchDarkMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.setTheme(!mSettings.isTheme());
                if (mSettings.isTheme()) {
                    displayDarkTheme(googleMap);
                } else {
                    displayDefaultTheme(googleMap);
                }
            }
        });
    }

    private void displayDefaultTheme(final GoogleMap googleMap) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] perimeter = {getString(R.string.perimeter1), getString(R.string.perimeter2), getString(R.string.perimeter3), getString(R.string.perimeter4), getString(R.string.perimeter5)};
        final boolean[] checkedItems = {false, false, false, false, false};
        View switchButtonView = LayoutInflater.from(this).inflate(R.layout.activity_toggle, null);
        Switch switchButton = switchButtonView.findViewById(R.id.switch2);
        builder.setTitle(R.string.settings);
        builder.setMultiChoiceItems(perimeter, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                mZoom = checkedItems[0] ? 14 : checkedItems[1] ? 15 : checkedItems[2] ? 16 : checkedItems[3] ? 17 : 18;
            }
        });
        builder.setView(switchButtonView);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDropOff = isChecked;
                if (mDropOff) {
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
                mSettings.setDropOff(mDropOff);
                mSettings.setZoom(mZoom);
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
                    LatLng newStation = new LatLng(stations.get(i).getLatitude(), stations.get(i).getLongitude());
                    Marker marker = mMap.addMarker((new MarkerOptions().position(newStation).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)).title(stations.get(i).getAddress()).snippet(stations.get(i).getName())));
                    mStationMarkers.add(marker);
                    marker.showInfoWindow();
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                }
            }
        });
    }

    private void removeMarkers() {
        for (Marker marker : mStationMarkers) {
            marker.remove();
        }
        mStationMarkers.clear();
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

