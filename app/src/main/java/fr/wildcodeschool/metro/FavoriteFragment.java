package fr.wildcodeschool.metro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {
    private ArrayList<Integer> mFavoriteStationNumbers = new ArrayList<>();
    private int mFavoriteStationNumber;
    private Settings mSettings;
    private ListView mListView;
    private ArrayList<Station> mFavoriteStation = new ArrayList<>();
    private Singleton settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false);
        /*FirebaseDatabase favoriteStationBase = FirebaseDatabase.getInstance();
        DatabaseReference favoriteStationReference = favoriteStationBase.getReference("favoriteStationBase");
        settings = Singleton.getInstance();
        mSettings = settings.getSettings();
        favoriteStationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favoriteStationNumberData : dataSnapshot.getChildren()) {
                    mFavoriteStationNumber = Integer.parseInt(favoriteStationNumberData.getKey());
                    mFavoriteStationNumbers.add(mFavoriteStationNumber);
                }

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
                        RecyclerView recycleListStations = favoriteView.findViewById(R.id.fragment_recycle_station);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recycleListStations.setLayoutManager(layoutManager);
                        StationsRecyclerAdapter adapter = new StationsRecyclerAdapter(stations);
                        recycleListStations.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        });*/
        return favoriteView;
    }
}
