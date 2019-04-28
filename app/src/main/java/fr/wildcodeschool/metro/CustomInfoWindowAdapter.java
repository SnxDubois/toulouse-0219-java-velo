package fr.wildcodeschool.metro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;
    private int bikeAvailable;
    private Station selectedMarker;
    private TextView stationTitle;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_windows_map, null);

        /*mWindow.setTag(R.drawable.ic_favorite_unchecked);
        final ImageButton heartButton = mWindow.findViewById(R.id.ibFavorite);
        mWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Added to favorite !", Toast.LENGTH_SHORT).show();
                if(mWindow.getTag().equals(R.drawable.ic_favorite_checked)) {
                    heartButton.setImageResource(R.drawable.ic_favorite_unchecked);
                    heartButton.setTag(R.drawable.ic_favorite_unchecked);
                }
                else {
                    heartButton.setImageResource(R.drawable.ic_favorite_checked);
                    heartButton.setTag(R.drawable.ic_favorite_checked);
                }
            }
        });*/
    }

    private void rendowWindowText(Marker marker, final View view) {
        selectedMarker = (Station) marker.getTag();
        String title = marker.getTitle();
        stationTitle = view.findViewById(R.id.tvTitle);
        if (!title.equals("")) {stationTitle.setText(title);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }


}
