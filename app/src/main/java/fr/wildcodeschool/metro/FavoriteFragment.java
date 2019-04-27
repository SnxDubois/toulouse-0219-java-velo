package fr.wildcodeschool.metro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static fr.wildcodeschool.metro.Helper.extractStation;


public class FavoriteFragment extends Fragment {
    private ArrayList<Integer> favoriteStationNumbers = new ArrayList<>();
    private int favoriteStationNumber;
    private Settings mSettings;
    private ListView listView;
    private StationAdapter stationAdapter;
    private ArrayList<Station> favoriteStation = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false);
        final FirebaseDatabase favoriteStationBase = FirebaseDatabase.getInstance();
        DatabaseReference favoriteStationReference = favoriteStationBase.getReference("favoriteStationBase");

        favoriteStationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot favoriteStationNumberData : dataSnapshot.getChildren()){
                    favoriteStationNumber = Integer.parseInt(favoriteStationNumberData.getKey());
                    favoriteStationNumbers.add(favoriteStationNumber);                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });
        extractStation(getContext(), mSettings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                for (int index = 0 ; index < favoriteStationNumbers.size(); index++) {
                    for (Station searchFavoriteStation : stations) {
                        if (searchFavoriteStation.getNumber() == favoriteStationNumbers.get(index)) {
                            favoriteStation.add(searchFavoriteStation);
                        }
                    }
                }
                listView = favoriteView.findViewById(R.id.list_favorite_station);
                stationAdapter = new StationAdapter(getContext(), favoriteStation);
                listView.setAdapter(stationAdapter);

            }
        });



        return favoriteView;
    }
}
