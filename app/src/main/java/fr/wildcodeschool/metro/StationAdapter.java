package fr.wildcodeschool.metro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<Station> {

    public StationAdapter(Context context, ArrayList<Station> stations) {
        super(context, 0, stations);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Station stationItem = getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_display_station, parent, false);
        }
        TextView stationName = convertView.findViewById(R.id.tvStationName);
        TextView stationNumber = convertView.findViewById(R.id.tvStationNumber);
        TextView stationAddress = convertView.findViewById(R.id.tvStationAddress);
        TextView stationAvailableStands = convertView.findViewById(R.id.tvStands);
        TextView stationAvailableBikes = convertView.findViewById(R.id.tvBikes);
        TextView stationDistance = convertView.findViewById(R.id.tvDistance);
        System.out.println(stationItem.getName());
        stationName.setText(stationItem.getName());
        stationNumber.setText(Integer.toString(stationItem.getNumber()));
        stationAddress.setText(stationItem.getAddress());
        stationAvailableStands.setText(Integer.toString(stationItem.getAvailableStands()));
        stationAvailableBikes.setText(Integer.toString(stationItem.getAvailableBikes()));
        stationDistance.setText(Integer.toString((int) stationItem.getDistance()) + " meters");

        return convertView;
    }
}