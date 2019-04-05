package fr.wildcodeschool.metro;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Helper {

    public static ArrayList<Station> extractStation(Context context,Boolean dropoff, int zoom){
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
            for(int i = 0 ; i < root.length() ; i++){
                JSONObject parcVelo = (JSONObject) root.get(i);
                int number = (int) parcVelo.get("number");
                String name = (String) parcVelo.get("name");
                String address = (String) parcVelo.get("address");
                double latitude = (double) parcVelo.get("latitude");
                double longitude = (double) parcVelo.get("longitude");
                Station station = new Station (number,name,address,latitude,longitude);
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return stations;
    }
}
