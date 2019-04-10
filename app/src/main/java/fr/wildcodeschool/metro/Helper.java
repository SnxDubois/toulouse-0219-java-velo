package fr.wildcodeschool.metro;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Helper {
    private final static String API_KEY = "6dcf293ec6f59ca711dd9d89646478ef2acb872d";
    private static ArrayList<Station> stations = new ArrayList<>();

    public static void extractStation(Context context, Boolean dropoff, int zoom, final BikeStationListener listener) {
        String url = "https://api.jcdecaux.com/vls/v1/stations?contract=Toulouse&apiKey=" + API_KEY;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequestRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray listStation = response;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject bikeStation = (JSONObject) listStation.get(i);
                                int number = (int) bikeStation.get("number");
                                String name = (String) bikeStation.get("name");
                                String address = (String) bikeStation.get("address");
                                JSONObject position = (JSONObject) bikeStation.get("position");
                                double latitude = (double) position.get("lat");
                                double longitude = (double) position.get("lng");
                                int availableStands = (int) bikeStation.get("available_bike_stands");
                                int availableBike = (int) bikeStation.get("available_bikes");
                                String status = (String) bikeStation.get("status");
                                Station station = new Station(number, name, address, latitude, longitude, availableStands, availableBike, status);
                                stations.add(station);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listener.onResult(stations);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonArrayRequestRequest);
    }

    public interface BikeStationListener {
        void onResult(ArrayList<Station> stations);
    }
}
