package fr.wildcodeschool.metro;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StationsRecyclerAdapter extends RecyclerView.Adapter<StationsRecyclerAdapter.ViewHolder> {

    private ArrayList<Station> mStations;

    public StationsRecyclerAdapter(ArrayList<Station> stations) {
        mStations = stations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameView, stationAddressView, distanceView, bikesView, standsView;

        public ViewHolder(View v) {
            super(v);
            this.stationNameView = v.findViewById(R.id.tvStationName);
            this.stationAddressView = v.findViewById(R.id.tvStationAddress);
            this.distanceView = v.findViewById(R.id.tvDistance);
            this.bikesView = v.findViewById(R.id.tvBikes);
            this.standsView = v.findViewById(R.id.tvStands);
        }
    }

    @Override
    public StationsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_display_recycle_station, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationsRecyclerAdapter.ViewHolder holder, int position) {
        Station station = mStations.get(position);
        holder.stationNameView.setText(station.getName());
        holder.stationAddressView.setText(station.getAddress());
        holder.distanceView.setText((Integer.toString((int)station.getDistance())));
        holder.bikesView.setText(station.getAvailableBikes());
        holder.standsView.setText(station.getAvailableStands());
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

}



