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
        TextView stationName = convertView.findViewById(R.id.textView);
        TextView stationNumber = convertView.findViewById(R.id.textView2);
        TextView stationAddress = convertView.findViewById(R.id.textView3);
        stationName.setText(stationItem.getStationName());
        stationNumber.setText(Integer.toString(stationItem.getStationNumber()));
        stationAddress.setText(stationItem.getStationAddress());
        return convertView;
    }
}