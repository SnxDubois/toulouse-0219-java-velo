package fr.wildcodeschool.metro;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Modell {
    static ArrayList<Station> stations = new ArrayList<>();


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
                //tvHello.append(name.toString());
                String StationAdress = (String) parcVelo.get("adress");
                double StationLatitude = (double) parcVelo.get("latitude");
                double StationLongitude = (double) parcVelo.get("longitude");
                Station station = new Station (StationNumber,StationName,StationAdress,StationLatitude,StationLongitude);
                stations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return stations;
    }


}
