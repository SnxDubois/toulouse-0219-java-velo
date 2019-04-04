package fr.wildcodeschool.metro;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Modell {
    static ArrayList<Station> stations = new ArrayList<>();
    private static GoogleMap mMap;


    public static ArrayList<Station> extractStation(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("velos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONArray root = new JSONArray(json);
            for(int i = 0 ; i < root.length() ; i++){
                JSONObject parcVelo = (JSONObject) root.get(i);
                int StationNumber = (int) parcVelo.get("number");
                String StationName = (String) parcVelo.get("name");
                String StationAddress = (String) parcVelo.get("address");
                double StationLatitude = (double) parcVelo.get("latitude");
                double StationLongitude = (double) parcVelo.get("longitude");
                Station station = new Station (StationNumber,StationName,StationAddress,StationLatitude,StationLongitude);
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return stations;
    }
}
