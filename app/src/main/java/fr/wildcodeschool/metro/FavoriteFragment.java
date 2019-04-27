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
    private ArrayList<Integer> mFavoriteStationNumbers = new ArrayList<>();
    private int mFavoriteStationNumber;
    private Settings mSettings;
    private ListView mListView;
    private StationAdapter mStationAdapter;
    private ArrayList<Station> mFavoriteStation = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false);
        final FirebaseDatabase favoriteStationBase = FirebaseDatabase.getInstance();
        DatabaseReference favoriteStationReference = favoriteStationBase.getReference("favoriteStationBase");

        favoriteStationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favoriteStationNumberData : dataSnapshot.getChildren()) {
                    mFavoriteStationNumber = Integer.parseInt(favoriteStationNumberData.getKey());
                    mFavoriteStationNumbers.add(mFavoriteStationNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });
        extractStation(getContext(), mSettings, new Helper.BikeStationListener() {

            @Override
            public void onResult(ArrayList<Station> stations) {
                for (int index = 0; index < mFavoriteStationNumbers.size(); index++) {
                    for (Station searchFavoriteStation : stations) {
                        if (searchFavoriteStation.getNumber() == mFavoriteStationNumbers.get(index)) {
                            mFavoriteStation.add(searchFavoriteStation);
                        }
                    }
                }
                mListView = favoriteView.findViewById(R.id.list_favorite_station);
                mStationAdapter = new StationAdapter(getContext(), mFavoriteStation);
                mListView.setAdapter(mStationAdapter);

            }
        });
        return favoriteView;
    }
}
