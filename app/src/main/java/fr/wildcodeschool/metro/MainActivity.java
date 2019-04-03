package fr.wildcodeschool.metro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = null;
        try {
            InputStream is = getAssets().open("velos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        TextView tvHello = findViewById(R.id.tvHello);
        try {
            JSONArray root = new JSONArray(json);
            for(int i = 0 ; i < root.length() ; i++){
                JSONObject parcVelo = (JSONObject) root.get(i);
                int number = (int) parcVelo.get("number");
                String name = (String) parcVelo.get("name");
                //tvHello.append(name.toString());
                String adress = (String) parcVelo.get("adress");
                double latitude = (double) parcVelo.get("latitude");
                double longitude = (double) parcVelo.get("longitude");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
