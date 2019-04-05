package fr.wildcodeschool.metro;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Helper {

    public static ArrayList<Station> extractStation(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

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
            for (int i = 0; i < root.length(); i++) {
                JSONObject parcVelo = (JSONObject) root.get(i);
                int stationNumber = (int) parcVelo.get("number");
                String stationName = (String) parcVelo.get("name");
                String stationAddress = (String) parcVelo.get("address");
                double stationLatitude = (double) parcVelo.get("latitude");
                double stationLongitude = (double) parcVelo.get("longitude");
                Station station = new Station(stationNumber, stationName, stationAddress, stationLatitude, stationLongitude);
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stations;
    }
}
