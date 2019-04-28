package fr.wildcodeschool.metro;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StationsRecyclerAdapter extends RecyclerView.Adapter<StationsRecyclerAdapter.ViewHolder> {

    private ArrayList<Station> mStations;

    public StationsRecyclerAdapter(ArrayList<Station> stations) {
        mStations = stations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameView, stationAddressView, distanceView, bikesView, standsView;
        ImageView favoriteView, makeWayView;

        public ViewHolder(View v) {
            super(v);
            this.stationNameView = v.findViewById(R.id.tvStationName);
            this.stationAddressView = v.findViewById(R.id.tvStationAddress);
            this.distanceView = v.findViewById(R.id.tvDistance);
            this.bikesView = v.findViewById(R.id.tvBikes);
            this.standsView = v.findViewById(R.id.tvStands);
            this.favoriteView = v.findViewById(R.id.ibFavorite);
            this.makeWayView = v.findViewById(R.id.ibMakeWay);
        }
    }

    @Override
    public StationsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_display_recycle_station, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StationsRecyclerAdapter.ViewHolder holder, final int position) {
        Station station = mStations.get(position);
        holder.stationNameView.setText(station.getName());
        holder.stationAddressView.setText(station.getAddress());
        holder.distanceView.setText((Integer.toString((int)station.getDistance())));
        holder.bikesView.setText((Integer.toString(station.getAvailableBikes())));
        holder.standsView.setText((Integer.toString(station.getAvailableStands())));
        holder.favoriteView.setTag(R.drawable.ic_favorite_unchecked);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference favoriteStationBase = database.getReference("favoriteStationBase");
        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Added to favorite !", Toast.LENGTH_SHORT).show();
                if(holder.favoriteView.getTag().equals(R.drawable.ic_favorite_checked)) {
                    holder.favoriteView.setImageResource(R.drawable.ic_favorite_unchecked);
                    holder.favoriteView.setTag(R.drawable.ic_favorite_unchecked);

                    favoriteStationBase.orderByKey().equalTo(mStations.get(position).getNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
                                String key = postsnapshot.getKey();
                                dataSnapshot.getRef().removeValue();
                            }

                }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                else {
                    holder.favoriteView.setImageResource(R.drawable.ic_favorite_checked);
                    holder.favoriteView.setTag(R.drawable.ic_favorite_checked);

                    String key = Integer.toString(mStations.get(position).getNumber());
                    favoriteStationBase.child(key).setValue(mStations.get(position).getNumber());
                }
            }
        });
        holder.makeWayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "The way is computing !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return mStations.size();
    }

}



